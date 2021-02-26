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
		v1 (.clk(clk), .reset(~rst_n), .x(x), .y(y), .r(red_out), .g(green_out), .b(blue_out),
			 .VGA_R(VGA_R), .VGA_G(VGA_G), .VGA_B(VGA_B), .VGA_BLANK_N(VGA_BLANK_N),
			 .VGA_CLK(VGA_CLK), .VGA_HS(VGA_HS), .VGA_SYNC_N(VGA_SYNC_N), .VGA_VS(VGA_VS));

    // Frame data: video driver constantly writes this frame to screen
    // Data: RRRR_GGGG_BBBB, first index: x-coordinate, second index: y-coordinate
    reg [11:0] frame_out [0:639][0:479]; 

    // Frame buffer: where we "draw" our pixels to
    // Same format as frame_out
    reg [11:0] frame_buffer [0:639][0:479]; 


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
                red_out <= frame_out[x][y][11:8];
                green_out <= frame_out[x][y][7:4];
                blue_out <= frame_out[x][y][3:0];
            end
        end
    end

    // Variables for displaying and flushing frame buffer
    reg [31:0] fps_clock_count;
    reg flushing;
    reg [9:0] x_flush;
    reg [8:0] y_flush;
    reg frame_plot_odd;


    // Variables for plotting textures
    reg [12:0] tex_code, prev_tex_code;
    reg [17:0] coordinates, prev_coor;
    reg multiplayer;
    reg dummy;

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
            prev_tex_code <= 0;
            coordinates <= 0;
            prev_coor <= 0;
            multiplayer <= 0;
            dummy <= 0;

            slave_waitrequest <= 0;
            slave_readdata <= 0;
        end
        else begin
            fps_clock_count <= fps_clock_count + 1;

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
            // Other operation
            else begin
                // For configring module (singleplayer/multiplayer)
                if (slave_write) begin
                    case (slave_address)
                        0: multiplayer <= slave_writedata;
                        1: coordinates <= slave_writedata;
                        2: tex_code <= slave_writedata;

                        // PLOT TEXTURE HERE
                        4: begin
                            
                        end
                        default: dummy <= dummy;
                    endcase
                end
                // Response to availability check
                else if (slave_read && slave_address == 3) begin
                    slave_readdata <= frame_plot_odd;
                end
            end
        end
    end

    // TEXTURES
    // Texture format: see google doc!


endmodule: render
