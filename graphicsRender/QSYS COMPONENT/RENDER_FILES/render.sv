module render(
            input logic clk, input logic rst_n,

           // slave (CPU-facing)
           output logic slave_waitrequest,
           input logic [3:0] slave_address,
           input logic slave_read, output logic [31:0] slave_readdata,
           input logic slave_write, input logic [31:0] slave_writedata,

        //    // master (memory-facing) (REMOVE IF NOT USED AT THE END)
        //    input logic master_waitrequest,
        //    output logic [31:0] master_address,
        //    output logic master_read, input logic [31:0] master_readdata, input logic master_readdatavalid,
        //    output logic master_write, output logic [31:0] master_writedata
           
           // VGA pins
            output logic [7:0] VGA_R, output logic [7:0] VGA_G,  output logic [7:0] VGA_B,   
            output logic VGA_BLANK_N, output logic VGA_CLK,
            output logic VGA_HS, output logic VGA_SYNC_N, output logic VGA_VS

            );

    wire [9:0] x;
    wire [8:0] y;
    reg [7:0] red_out, green_out, blue_out;

    // TODO: Set proper color ouptut: multiply X_out by 85 (255/3 = 85)

    // The video driver has active high reset!
    video_driver #(.WIDTH(320), .HEIGHT(240))
		v1 (.clk(clk), .reset(~rst_n), .x(x), .y(y), .r(red_out), .g(green_out), .b(blue_out),
			 .VGA_R(VGA_R), .VGA_G(VGA_G), .VGA_B(VGA_B), .VGA_BLANK_N(VGA_BLANK_N),
			 .VGA_CLK(VGA_CLK), .VGA_HS(VGA_HS), .VGA_SYNC_N(VGA_SYNC_N), .VGA_VS(VGA_VS));

    // Frame data: video driver constantly writes this frame to screen
    // DUAL PORT RAM
    // Data: RR_GGBB, first index: x-coordinate, second index: y-coordinate
    wire [16:0] frame_out_rdaddr;
    reg [16:0] frame_out_wraddr;
    reg [5:0] frame_out_data;
    reg frame_out_wren;
    wire [5:0] frame_out_q;
    frame_out frame_out (.clock(clk), .data(frame_out_data), .rdaddress(frame_out_rdaddr), .wraddress(frame_out_wraddr), .wren(frame_out_wren), .q(frame_out_q));

    // Frame buffer: where we "draw" our pixels to
    // Same format as frame_out
    reg [16:0] frame_buffer_addr;
    reg [5:0] frame_buffer_data;
    reg frame_buffer_wren;
    wire [5:0] frame_buffer_q;
    frame frame_buffer (.address(frame_buffer_addr), .clock(clk), .data(frame_buffer_data), .wren(frame_buffer_wren), .q(frame_buffer_q));

    // Texture memory modules instantiations
    // Pipe (both up and down): width: 16px, height: 86px
    // Bird (all 4): width: 18px, height: 12px
    // Letter/number: width: 24px, height: 24px
    // Texture RGB values: T_RR_GG_BB, T for transparency
    //      1: Plot color, 0: Transparent
    
    // Pipe memory module
    reg [12:0] pipe_tex_addr;
    wire [6:0] pipe_tex_q;
    localparam signed [5:0] PIPE_MAX_X=16;
    localparam signed [8:0] PIPE_MAX_Y=180;
    pipes pipe_tex (.address(pipe_tex_addr), .clock(clk), .q(pipe_tex_q));

    // Bird memory module
    reg [9:0] bird_tex_addr;
    wire [6:0] bird_tex_q;
    localparam signed [5:0] BIRD_MAX_X=18;
    localparam signed [4:0] BIRD_MAX_Y=12;
    birds bird_tex (.address(bird_tex_addr), .clock(clk), .q(bird_tex_q));

    // Numbers memory module
    reg [12:0] char_tex_addr;
    wire [6:0] char_tex_q;
    localparam signed [5:0] CHAR_MAX_X=24;
    localparam signed [5:0] CHAR_MAX_Y=24;
    chars char_tex (.address(char_tex_addr), .clock(clk), .q(char_tex_q));

    // Memory module with other textures
    reg [13:0] custom_tex_addr;
    wire [6:0] custom_tex_q;
    reg  signed [8:0] custom_max_x;
    reg signed [6:0] custom_max_y;
    custom custom_tex (.address(custom_tex_addr), .clock(clk), .q(custom_tex_q));


    // Variables for displaying and flushing frame buffer
    reg [31:0] fps_clock_count;
    reg flushing;
    reg frame_plot_odd;
    reg flush_now;
    reg [1:0] flush_wait;


    // Variables for plotting
    reg [6:0] tex_code;
    reg signed [13:0] mid_x;
    reg signed [13:0] mid_y;
    reg negative_coordinates;
    reg dummy;
    reg plotting;
    reg signed [13:0] curr_x;
    reg signed [13:0] curr_y;
    reg do_plot;
    reg [16:0] buf_addr_save;
    reg [5:0] bird_color;

    // Constantly write pixel data to video adapter
    reg [8:0] x_prev;
    reg [7:0] y_prev;
    reg reading;
    always_ff @(posedge clk, negedge rst_n) begin
        if (~rst_n) begin
            red_out <= 0;
            green_out <= 0;
            blue_out <= 0;
            reading <= 0;

            x_prev = 10'b1111111111;
            y_prev = 9'b111111111;
        end
        else begin
            // Set new color when color for new coordinate requested
            if (x_prev != x || y_prev != y) begin
                x_prev <= x;
                y_prev <= y;
                reading <= 1;
            end
            else if (reading) begin
                red_out <= frame_out_q[5:4] * 85;
                green_out <= frame_out_q[3:2] * 85;
                blue_out <= frame_out_q[1:0] * 85;
                reading <= 0;
            end
        end
    end

    // Variables for filling frame with one color
    reg fill_init;

    // Variables for plotting textures
    reg plot_init;

    // Use different register for address for different case
    assign frame_out_rdaddr = x * 240 + y;

    // Always block for main logic
    // 50Mhz clock and 30fps output, so 1,666,666 cycles per frame
    always_ff @(posedge clk, negedge rst_n) begin
        if (~rst_n) begin
            frame_out_wraddr <= 0;
            frame_out_data <= 0;
            frame_out_wren <= 0;
            frame_buffer_addr <= 0;
            frame_buffer_data <= 0;
            frame_buffer_wren <= 0;

            bird_tex_addr <= 0;
            pipe_tex_addr <= 0;
            char_tex_addr <= 0;
            custom_tex_addr <= 0;
            custom_max_x <= 0;
            custom_max_y <= 0;

            fps_clock_count <= 0;
            flushing <= 0;
            frame_plot_odd <= 0;
            flush_now <= 0;
            flush_wait <= 0;

            tex_code <= 0;
            mid_x <= 0;
            mid_y <= 0;
            negative_coordinates <= 0;
            dummy <= 0;
            plotting <= 0;
            curr_x <= 0;
            curr_y <= 0;
            do_plot <= 0;
            buf_addr_save <= 0;
            bird_color <= 'b111000;

            fill_init <= 0;
            plot_init <= 0;

            slave_waitrequest <= 0;
            slave_readdata <= 0;
        end
        else begin
            // Flush to frame buffer at set intervals
            if ((fps_clock_count >= 800000 && ~slave_waitrequest) || flushing || (flush_now && ~plotting)) begin
                if (flushing) begin
                    if (flush_wait != 0) begin
                        flush_wait <= flush_wait - 1;
                        frame_buffer_addr <= frame_buffer_addr + 1;
                        frame_out_data <= frame_buffer_q;
                        if (flush_wait == 1) frame_out_wren <= 1;
                    end
                    else if (frame_buffer_addr < 76801) begin
                        frame_out_wren <= 1;
                        frame_out_data <= frame_buffer_q;
                        frame_buffer_addr <= frame_buffer_addr + 1;
                        frame_out_wraddr <= frame_out_wraddr + 1;
                    end
                    else begin
                        frame_plot_odd <= ~frame_plot_odd;
                        flushing <= 0;
                        fps_clock_count <= 0;
                        slave_waitrequest <= 0;
                        flush_now <= 0;

                        frame_out_wren <= 0;
                        frame_buffer_addr <= 0;     // NOTE: UNKNOWN BEHAVIOR WHEN EXCEEDING MEMORY RANGE
                    end
                end
                else begin
                    slave_waitrequest <= 1;
                    flushing <= 1;
                    frame_out_wraddr <= 0;
                    frame_buffer_addr <= 0;
                    frame_out_wren <= 0;
                    frame_buffer_wren <= 0;
                    flush_wait <= 2;
                end

            end
            // Main logic
            else begin
                // Increment clock count no matter what (except when it is already scheduled)
                if (~flush_now) fps_clock_count <= fps_clock_count + 1;

                // Plot texture
                if (plotting) begin
                    // Flush to frame buffer after plotting this texture
                    if (fps_clock_count == 400000) flush_now <= 1;

                    // Plot entire frame to color specified
                    if (tex_code[6]) begin
                        if (fill_init) begin
                            frame_buffer_addr <= 0;
                            frame_buffer_wren <= 1;
                            frame_buffer_data <= tex_code[5:0];
                            fill_init <= 0;
                        end
                        else begin
                            if (frame_buffer_addr < 76800) begin
                                frame_buffer_addr <= frame_buffer_addr + 1;
                            end
                            // End plot
                            else begin
                                frame_buffer_wren <= 0;
                                frame_buffer_addr <= 0;     // NOTE: UNKNOWN BEHAVIOR WHEN EXCEEDING MEMORY RANGE

                                plotting <= 0;
                                if (~flush_now) slave_waitrequest <= 0;
                            end
                        end
                    end
                    
                    // Plot texture onto frame
                    else begin
                        // Plot birds
                        if (tex_code[5:0] >= 1 && tex_code[5:0] <= 4) begin
                            if (do_plot && bird_tex_q[6]) begin
                                // Added custom bird color support
                                if (bird_tex_q[5:0] == 'b111000)
                                    frame_buffer_data <= bird_color;
                                frame_buffer_addr <= buf_addr_save;
                                frame_buffer_wren <= 1;
                            end
                            else frame_buffer_wren <= 0;

                            if (plot_init) begin
                                plot_init <= 0;
                                // Avoid repeating code for all birds, since only difference is where the texture location starts
                                // Use base texture code of birds (1) as reference and incrementing by size of bird texture (216)
                                bird_tex_addr <= 216 * (tex_code[5:0] - 1);
                                curr_x <= mid_x - (BIRD_MAX_X >> 1);
                                curr_y <= mid_y - (BIRD_MAX_Y >> 1);
                            end
                            else begin
                                if (curr_x < mid_x + (BIRD_MAX_X >> 1) && curr_y < mid_y + (BIRD_MAX_Y >> 1)) begin
                                    bird_tex_addr <= bird_tex_addr + 1;
                                    curr_y <= curr_y + 1;
                                    // Add condition to check if bird_tex_q[6] is high or not
                                    if (curr_x >= 0 && curr_x < 320 && curr_y >= 0 && curr_y < 240) begin
                                        do_plot <= 1;
                                        buf_addr_save <= curr_x * 240 + curr_y;
                                    end
                                    else begin
                                        do_plot <= 0;
                                    end
                                end
                                else if (curr_x < mid_x + (BIRD_MAX_X >> 1) && curr_y >= mid_y + (BIRD_MAX_Y >> 1)) begin
                                    curr_x <= curr_x + 1;
                                    curr_y <= mid_y - (BIRD_MAX_Y >> 1);
                                    do_plot <= 0;
                                end
                                else begin
                                    plotting <= 0;
                                    if (~flush_now) slave_waitrequest <= 0;
                                end
                            end
                        end

                        // Plot pipe
                        else if (tex_code[5:0] == 5 || tex_code[5:0] == 6) begin
                            if (do_plot && pipe_tex_q[6]) begin
                                frame_buffer_data <= pipe_tex_q[5:0];
                                frame_buffer_addr <= buf_addr_save;
                                frame_buffer_wren <= 1;
                            end
                            else frame_buffer_wren <= 0;

                            if (plot_init) begin
                                plot_init <= 0;
                                // Avoid extra code by same method, only that base texture code is 5 and pipe texture size is 1376
                                pipe_tex_addr <= 2880 * (tex_code[5:0] - 5);
                                curr_x <= mid_x - (PIPE_MAX_X >> 1);
                                curr_y <= mid_y - (PIPE_MAX_Y >> 1);
                            end
                            else begin
                                if (curr_x < mid_x + (PIPE_MAX_X >> 1) && curr_y < mid_y + (PIPE_MAX_Y >> 1)) begin
                                    pipe_tex_addr <= pipe_tex_addr + 1;
                                    curr_y <= curr_y + 1;
                                    if (curr_x >= 0 && curr_x < 320 && curr_y >= 0 && curr_y < 240) begin
                                        do_plot <= 1;
                                        buf_addr_save <= curr_x * 240 + curr_y;
                                    end
                                    else begin
                                        do_plot <= 0;
                                    end
                                end
                                else if (curr_x < mid_x + (PIPE_MAX_X >> 1) && curr_y >= mid_y + (PIPE_MAX_Y >> 1)) begin
                                    curr_x <= curr_x + 1;
                                    curr_y <= mid_y - (PIPE_MAX_Y >> 1);
                                    do_plot <= 0;
                                end
                                else begin
                                    plotting <= 0;
                                    if (~flush_now) slave_waitrequest <= 0;
                                end
                            end
                        end

                        // Plot numbers
                        else if (tex_code[5:0] >= 7 && tex_code[5:0] <= 16) begin
                            if (do_plot && char_tex_q[6]) begin
                                frame_buffer_data <= char_tex_q[5:0];
                                frame_buffer_addr <= buf_addr_save;
                                frame_buffer_wren <= 1;
                            end
                            else frame_buffer_wren <= 0;

                            if (plot_init) begin
                                plot_init <= 0;
                                // Avoid extra code by same method, only that base texture code is 7 and pipe texture size is 576
                                char_tex_addr <= 576 * (tex_code[5:0] - 7);
                                curr_x <= mid_x - (CHAR_MAX_X >> 1);
                                curr_y <= mid_y - (CHAR_MAX_Y >> 1);
                            end
                            else begin
                                if (curr_x < mid_x + (CHAR_MAX_X >> 1) && curr_y < mid_y + (CHAR_MAX_Y >> 1)) begin
                                    char_tex_addr <= char_tex_addr + 1;
                                    curr_y <= curr_y + 1;
                                    if (curr_x >= 0 && curr_x < 320 && curr_y >= 0 && curr_y < 240) begin
                                        do_plot <= 1;
                                        buf_addr_save <= curr_x * 240 + curr_y;
                                    end
                                    else begin
                                        do_plot <= 0;
                                    end
                                end
                                else if (curr_x < mid_x + (CHAR_MAX_X >> 1) && curr_y >= mid_y + (CHAR_MAX_Y >> 1)) begin
                                    curr_x <= curr_x + 1;
                                    curr_y <= mid_y - (CHAR_MAX_Y >> 1);
                                    do_plot <= 0;
                                end
                                else begin
                                    plotting <= 0;
                                    if (~flush_now) slave_waitrequest <= 0;
                                end
                            end
                        end

                        // Plot custom textures
                        else if (tex_code[5:0] >= 17 && tex_code[5:0] <= 20) begin
                            if (do_plot && custom_tex_q[6]) begin
                                frame_buffer_data <= custom_tex_q[5:0];
                                frame_buffer_addr <= buf_addr_save;
                                frame_buffer_wren <= 1;
                            end
                            else frame_buffer_wren <= 0;

                            if (plot_init) begin
                                plot_init <= 0;
                                
                                case(tex_code[5:0])
                                    // Values for gold medal
                                    17: begin
                                        custom_tex_addr <= 0;
                                        custom_max_x <= 28;
                                        custom_max_y <= 28;
                                        curr_x <= mid_x - 14;
                                        curr_y <= mid_y - 14;
                                    end
                                    // Values for silver medal
                                    18: begin
                                        custom_tex_addr <= 784;
                                        custom_max_x <= 28;
                                        custom_max_y <= 28;
                                        curr_x <= mid_x - 14;
                                        curr_y <= mid_y - 14;
                                    end
                                    // Values for title texture
                                    19: begin
                                        custom_tex_addr <= 1568;
                                        custom_max_x <= 150;
                                        custom_max_y <= 40;
                                        curr_x <= mid_x - 75;
                                        curr_y <= mid_y - 20;
                                    end
                                    // Values for game over texture
                                    20: begin
                                        custom_tex_addr <= 7568;
                                        custom_max_x <= 134;
                                        custom_max_y <= 30;
                                        curr_x <= mid_x - 67;
                                        curr_y <= mid_y - 15;
                                    end
                                endcase
                            end
                            else begin
                                if (curr_x < mid_x + (custom_max_x >> 1) && curr_y < mid_y + (custom_max_y >> 1)) begin
                                    custom_tex_addr <= custom_tex_addr + 1;
                                    curr_y <= curr_y + 1;
                                    if (curr_x >= 0 && curr_x < 320 && curr_y >= 0 && curr_y < 240) begin
                                        do_plot <= 1;
                                        buf_addr_save <= curr_x * 240 + curr_y;
                                    end
                                    else begin
                                        do_plot <= 0;
                                    end
                                end
                                else if (curr_x < mid_x + (custom_max_x >> 1) && curr_y >= mid_y + (custom_max_y >> 1)) begin
                                    curr_x <= curr_x + 1;
                                    curr_y <= mid_y - (custom_max_y >> 1);
                                    do_plot <= 0;
                                end
                                else begin
                                    plotting <= 0;
                                    if (~flush_now) slave_waitrequest <= 0;
                                end
                            end
                        end

                        // Plot splitscreen line
                        else if (tex_code[5:0] == 31) begin
                            if (plot_init) begin
                                plot_init <= 0;
                                // curr_x <= 159;   // Don't need this: use 38160 directly below
                                curr_y <= 0;
                                frame_buffer_data <= 'b000000;
                            end
                            else begin
                                if (curr_y < 240) begin
                                    curr_y <= curr_y + 1;
                                    frame_buffer_wren <= 1;
                                    frame_buffer_addr <= 38160 + curr_y;
                                end
                                else begin
                                    frame_buffer_wren <= 0;
                                    plotting <= 0;
                                    if (~flush_now) slave_waitrequest <= 0;
                                end
                            end
                        end

                        // Do nothing if bogus texture code
                    end
                end

                // Module config
                else if (slave_write) begin
                    case (slave_address)
                        1:  begin 
                                if (negative_coordinates) mid_x <= -1 * slave_writedata[8:0]; 
                                else mid_x <= slave_writedata[8:0]; 
                            end
                        2:  begin
                                if (negative_coordinates) mid_y <= -1 * slave_writedata[7:0];
                                else mid_y <= slave_writedata[7:0];
                            end
                        3: negative_coordinates <= slave_writedata[0];
                        4: tex_code <= slave_writedata[6:0];

                        // Initiate texture plotting
                        6: begin
                            slave_waitrequest <= 1;
                            plotting <= 1;
                            plot_init <= 1;
                            fill_init <= 1;
                            bird_tex_addr <= 0;
                            pipe_tex_addr <= 0;
                            char_tex_addr <= 0;
                            custom_tex_addr <= 0;
                            do_plot <= 0;
                        end

                        7: bird_color <= slave_writedata[5:0];

                        default: dummy <= ~dummy;
                    endcase
                end

                // Response to same frame check
                else if (slave_read && slave_address == 5) begin
                    slave_readdata <= frame_plot_odd;
                end
            end
        end
    end

endmodule: render
