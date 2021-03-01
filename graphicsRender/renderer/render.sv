module render(input logic clk, input logic rst_n,
           // slave (CPU-facing)
           output logic slave_waitrequest,
           input logic [3:0] slave_address,
           input logic slave_read, output logic [31:0] slave_readdata,
           input logic slave_write, input logic [31:0] slave_writedata,
           // master (memory-facing)
           input logic master_waitrequest,
           output logic [31:0] master_address,
           output logic master_read, input logic [31:0] master_readdata, input logic master_readdatavalid,
           output logic master_write, output logic [31:0] master_writedata
           
           // VGA pins
            output logic [7:0] VGA_R, output logic [7:0] VGA_G,  output logic [7:0] VGA_B,   
            output logic VGA_BLANK_N, output logic VGA_CLK,
            output logic VGA_HS, output logic VGA_SYNC_N, output logic VGA_VS);

    // We don't need to write to master here!
    assign master_write = 0;
    assign master_writedata = 0;

    wire [9:0] x;
    wire [8:0] y;
    reg [7:0] red_out, green_out, blue_out;

    // The video driver has active high reset!
    video_driver #(.WIDTH(320), .HEIGHT(240))
		v1 (.clk(clk), .reset(~rst_n), .x(x), .y(y), .r({red_out, 6'd0}), .g({green_out, 6'd0}), .b({blue_out, 6'd0}),
			 .VGA_R(VGA_R), .VGA_G(VGA_G), .VGA_B(VGA_B), .VGA_BLANK_N(VGA_BLANK_N),
			 .VGA_CLK(VGA_CLK), .VGA_HS(VGA_HS), .VGA_SYNC_N(VGA_SYNC_N), .VGA_VS(VGA_VS));

    // Frame data: video driver constantly writes this frame to screen
    // Data: RR_GGBB, first index: x-coordinate, second index: y-coordinate
    reg [5:0] frame_out [0:319][0:239];

    // Frame buffer: where we "draw" our pixels to
    // Same format as frame_out
    reg [5:0] frame_buffer [0:319][0:239]; 


    // Constantly flush frame data to VGA pins
    reg [8:0] x_prev;
    reg [7:0] y_prev;
    always_ff @(posedge clk, negedge rst_n) begin
        if (~rst_n) begin
            x_prev = 10'b1111111111;
            y_prev = 9'b111111111;
        end
        else begin
            // Set new color when color for new coordinate requested
            if (x_prev != x || y_prev != y) begin
                x_prev <= x;
                y_prev <= y;
                red_out <= frame_out[x][y][5:4];
                green_out <= frame_out[x][y][3:2];
                blue_out <= frame_out[x][y][1:0];
            end
        end
    end

    // Texture memory modules instantiations
    // Pipe (both up and down): width: 16px, height: 86px
    // Bird (all 4): width: 18px, height: 12px
    // Letter/number: width: 24px, height: 24px
    
    // TODO: REMAKE MEMORY MODULES
    // Pipe memory module
    reg [11:0] pipe_tex_addr;
    reg [5:0] pipe_tex_q;
    localparam [4:0] PIPE_MAX_X=17;
    localparam [6:0] PIPE_MAX_Y=87;
    pipes pipe_tex (.address(pipe_tex_addr), .clock(clk), .q(pipe_tex_q));

    // Bird memory module
    reg [9:0] bird_tex_addr;
    reg [5:0] bird_tex_q;
    localparam [4:0] BIRD_MAX_X=18;
    localparam [3:0] BIRD_MAX_Y=13;
    birds bird_tex (.address(bird_tex_addr), .clock(clk), .q(bird_tex_q));

    // Letter/number memory module
    reg [13:0] char_tex_addr;
    reg [5:0] char_tex_q;
    localparam [4:0] BIRD_MAX_X=25;
    localparam [4:0] BIRD_MAX_Y=25;
    chars char_tex (.address(char_tex_addr), .clock(clk), .q(char_tex_q));


    // Variables for displaying and flushing frame buffer
    reg [31:0] fps_clock_count;
    reg flushing;
    reg [9:0] x_flush;
    reg [8:0] y_flush;
    reg frame_plot_odd;


    // Variables for plotting
    reg [6:0] tex_code, prev_tex_code;
    reg [17:0] coordinates, prev_coor;
    reg multiplayer;        // Not much difference in final output: just a line down the middle of the screen
    reg dummy;
    reg plotting;
    reg [8:0] curr_x;
    reg [7:0] curr_y;

    // Variables for filling frame with one color
    reg fill_init;

    // Variables for plotting textures
    reg plot_init;

    // Always block for main logic
    // 50Mhz clock and 30fps output, so 1,666,666 cycles per frame
    always_ff @(posedge clk, negedge rst_n) begin
        if (~rst_n) begin
            fps_clock_count <= 0;
            flushing <= 0;
            x_flush <= 0;
            y_flush <= 0;
            frame_plot_odd <= 0;

            tex_code <= 0;
            coordinates <= 0;
            multiplayer <= 0;
            dummy <= 0;
            plotting <= 0;

            fill_init <= 0;
            plot_init <= 0;

            slave_waitrequest <= 0;
            slave_readdata <= 0;
        end
        else begin
            // Flush to frame buffer for 30fps display (1666666 - 320*240 - 240 = 1589626)
            if (fps_clock_count == 1589865) begin
                if (flushing) begin
                    if (x_flush < 320) begin
                        if (y_flush < 240) begin
                            frame_out[x_flush][y_flush] <= frame_buffer[x_flush][y_flush];
                            y_flush <= y_flush + 1;
                        end
                        else begin
                            x_flush <= x_flush + 1;
                            y_flush <= 0;
                        end
                    end
                    else begin
                        frame_plot_odd <= ~frame_plot_odd;
                        flushing <= 0;
                        fps_clock_count <= 0;
                        slave_waitrequest <= 0;
                    end
                end
                else begin
                    slave_waitrequest <= 1;
                    flushing <= 1;
                    x_flush <= 0;
                    y_flush <= 0;
                end

            end
            // Main logic
            else begin
                // Increment clock count no matter what
                fps_clock_count <= fps_clock_count + 1;


                // Plot texture
                if (plotting) begin
                    // Plot entire frame to color specified
                    if (tex_code[6]) begin
                        if (fill_init) begin
                            curr_x <= 0;
                            curr_y <= 0;
                            fill_init <= 0;
                        end
                        else begin
                            // Plot top down first, then increment from left to right
                            if (curr_x < 320 && curr_y < 240) begin
                                curr_y <= curr_y + 1;
                                frame_buffer[curr_x][curr_y] <= tex_code[5:0];
                            end
                            // Increment to next x
                            else if (curr_x < 320 && curr_y >= 240) begin
                                curr_x <= curr_x + 1;
                                curr_y <= 0;
                            end
                            // End plot
                            else begin
                                plotting <= 0;
                                slave_waitrequest <= 0;
                            end
                        end
                    end
                    
                    // Plot texture onto frame
                    else begin
                        // Not using a case statement because range of texture codes share same size
                        if (plot_init) begin
                            // TODO
                        end
                        else begin
                            // Plot bird
                            if (tex_code[5:0] >= 1 && tex_code[5:0] <= 4) begin
                                // TODO
                            end

                            // Plot pipe
                            else if (tex_code[5:0] == 5 || tex_code[5:0] == 6) begin
                                // TODO
                            end

                            // Plot letter/characters
                            else if (tex_code[5:0] >= 7 && tex_code[5:0] <= 30) begin
                                // TODO
                            end
                        end
                        
                    end
                end

                // Module config (singleplayer/multiplayer)
                else if (slave_write) begin
                    case (slave_address)
                        0: multiplayer <= slave_writedata[0];
                        1: coordinates <= slave_writedata[17:0];
                        2: tex_code <= slave_writedata[6:0];

                        // Initiate texture plotting
                        4: begin
                            slave_waitrequest <= 1;
                            plotting <= 1;
                            plot_init <= 1;
                            fill_init <= 1;

                            prev_tex_code <= tex_code;
                            prev_coor <= coordinate;
                        end
                        default: dummy <= dummy;
                    endcase
                end

                // Response to same frame check
                else if (slave_read && slave_address == 3) begin
                    slave_readdata <= frame_plot_odd;
                end
            end
        end
    end

endmodule: render
