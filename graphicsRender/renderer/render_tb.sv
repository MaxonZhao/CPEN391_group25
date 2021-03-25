`timescale 1ns / 1ns
module render_tb();

    logic clk, rst_n;

	wire slave_waitrequest;
	wire [31:0] slave_readdata;
	reg [3:0] slave_address;
	reg slave_read, slave_write;
	reg [31:0] slave_writedata;

    logic [7:0] VGA_R;
	logic [7:0] VGA_G;
	logic [7:0] VGA_B;
	logic VGA_BLANK_N;
	logic VGA_CLK;
	logic VGA_HS;
	logic VGA_SYNC_N;
	logic VGA_VS;
	
	render dut (.clk(clk), .rst_n(rst_n),

			.slave_waitrequest(slave_waitrequest), .slave_address(slave_address),
			.slave_read(slave_read), .slave_readdata(slave_readdata),
			.slave_write(slave_write), .slave_writedata(slave_writedata),

			.VGA_R(VGA_R), .VGA_G(VGA_G), .VGA_B(VGA_B),
			.VGA_BLANK_N(VGA_BLANK_N), .VGA_CLK(VGA_CLK),
			.VGA_HS(VGA_HS), .VGA_SYNC_N(VGA_SYNC_N), .VGA_VS(VGA_VS));

    initial forever begin
        clk <= 1'b0;    #10;
        clk <= 1'b1;    #10;
    end
    
    initial begin
        slave_address <= 0;
        slave_read <= 0;
        slave_write <= 0;
        slave_writedata <= 0;
        rst_n <= 1;
        #20;
        rst_n <= 0;
        #60;
        rst_n <= 1;
        #20;

        // START

        //---------------------------------------------------------------------------
        // Plot yellow background
        slave_address <= 4;
        slave_writedata <= 'b1_11_11_00;    // color code
        #20;
        slave_write <= 1;
        #20;
        slave_write <= 0;
        #20;

        assert (dut.tex_code == 'b1_11_11_00) 
        else   $error("BAD TEXTURE VALUE");

        wait(~slave_waitrequest);
        wait(clk == 0);
        slave_address <= 6;
        slave_write <= 1;   // initiate plot
        #20; 

        assert (dut.slave_waitrequest == 1) 
        else   $error("BAD WAITREQUEST");
        assert (dut.plotting == 1 && dut.plot_init == 1 && dut.fill_init == 1 && dut.bird_tex_addr == 0 && dut.pipe_tex_addr == 0 && dut.char_tex_addr == 0 && dut.custom_tex_addr == 0 && dut.do_plot == 0) 
        else   $error("BAD INITIAL PLOT VALUES");

        slave_write <= 0;
        #20;

        assert (dut.frame_buffer_addr == 0 && dut.frame_buffer_wren == 1 && dut.frame_buffer_data == 'b11_11_00) 
        else   $error("BAD INIT FILL");

        #20;

        assert (dut.fill_init == 0) 
        else   $error("BAD POST INIT FILL");

        wait(dut.frame_buffer_addr == 76800);
        wait(clk == 0);
        #40;

        assert (dut.frame_buffer_wren == 0 && dut.frame_buffer_addr == 0 && dut.plotting == 0) 
        else   $error("BAD END OF BG PLOT");

        wait(~slave_waitrequest);
        wait(clk == 0);

        //---------------------------------------------------------------------------
        // Plot first bird
        slave_address <= 4;
        slave_writedata <= 'b0_00_0001;     // texture code
        #20;
        slave_write <= 1;
        #20;
        slave_write <= 0;
        #20;

        assert (dut.tex_code == 'b0_00_0001) 
        else   $error("BAD TEXTURE VALUE");

        wait(~slave_waitrequest);
        wait(clk == 0);

        slave_address <= 1;
        slave_writedata <= 20;      // midpoint x coordinate
        slave_write <= 1;
        #20;
        slave_write <= 0;
        #20;

        assert (dut.mid_x == 20) 
        else   $error("BAD X COOR VALUE");

        wait(~slave_waitrequest);
        wait(clk == 0);

        slave_address <= 2;
        slave_writedata <= 20;      // midpoint y coordinate
        slave_write <= 1;
        #20;
        slave_write <= 0;
        #20;

        assert (dut.mid_y == 20) 
        else   $error("BAD X COOR VALUE");

        wait(~slave_waitrequest);
        wait(clk == 0);

        slave_address <= 6;
        slave_write <= 1;       // initiate plot
        #20;

        assert (dut.slave_waitrequest == 1) 
        else   $error("BAD WAITREQUEST");
        assert (dut.plotting == 1 && dut.plot_init == 1 && dut.fill_init == 1 && dut.bird_tex_addr == 0 && dut.pipe_tex_addr == 0 && dut.char_tex_addr == 0 && dut.custom_tex_addr == 0 && dut.do_plot == 0) 
        else   $error("BAD INITIAL PLOT VALUES");

        slave_write <= 0;
        #20;

        assert (dut.plot_init == 0 && dut.bird_tex_addr == 0 && dut.curr_x == 20 - 9 && dut.curr_y == 20 - 6) 
        else   $error("BAD PLOT INIT VALUES");

        #20;

        assert (dut.bird_tex_addr == 1 && dut.curr_y == 20 - 5 && dut.do_plot == 1 && dut.buf_addr_save == 11 * 240 + 14) 
        else   $error("BAD PLOT VALUES");

        #100;

        assert (dut.frame_buffer_data == 0 && dut.frame_buffer_addr == 11 * 240 + 18 && dut.frame_buffer_wren == 1) 
        else   $error("BAD FRAME BUFFER VALUES");

        wait(~slave_waitrequest);
        wait(clk == 0);

        //---------------------------------------------------------------------------
        // Plot pipe at top left of screen (down)
        slave_address <= 4;
        slave_writedata <= 'b0_00_0110;     // texture code
        #20;
        slave_write <= 1;
        #20;
        slave_write <= 0;
        #20;

        assert (dut.tex_code == 'b0_00_0110) 
        else   $error("BAD TEXTURE VALUE");

        wait(~slave_waitrequest);
        wait(clk == 0);

        slave_address <= 3;
        slave_writedata <= 1;     // negative coordinate indicator
        slave_write <= 1;
        #20;
        slave_write <= 0;
        #20;

        assert (dut.negative_coordinates == 1) 
        else   $error("BAD NEG COOR INDICATOR");

        wait(~slave_waitrequest);
        wait(clk == 0);

        slave_address <= 1;
        slave_writedata <= 1;      // midpoint x coordinate
        slave_write <= 1;
        #20;
        slave_write <= 0;
        #20;

        assert (dut.mid_x == -1) 
        else   $error("BAD X COOR VALUE");

        wait(~slave_waitrequest);
        wait(clk == 0);

        slave_address <= 2;
        slave_writedata <= 1;      // midpoint y coordinate
        slave_write <= 1;
        #20;
        slave_write <= 0;
        #20;

        assert (dut.mid_y == -1) 
        else   $error("BAD Y COOR VALUE");

        wait(~slave_waitrequest);
        wait(clk == 0);

        slave_address <= 3;
        slave_writedata <= 0;     // revert negative coordinate indicator
        slave_write <= 1;
        #20;
        slave_write <= 0;
        #20;

        assert (dut.negative_coordinates == 0) 
        else   $error("BAD NEG COOR INDICATOR");

        wait(~slave_waitrequest);
        wait(clk == 0);

        slave_address <= 6;
        slave_write <= 1;       // initiate plot
        #20;

        assert (dut.slave_waitrequest == 1) 
        else   $error("BAD WAITREQUEST");
        assert (dut.plotting == 1 && dut.plot_init == 1 && dut.fill_init == 1 && dut.bird_tex_addr == 0 && dut.pipe_tex_addr == 0 && dut.char_tex_addr == 0 && dut.custom_tex_addr == 0 && dut.do_plot == 0) 
        else   $error("BAD INITIAL PLOT VALUES");

        slave_write <= 0;
        #20;

        assert (dut.plot_init == 0 && dut.pipe_tex_addr == 2880 && dut.curr_x == -1 - 8 && dut.curr_y == -1 - 90) 
        else   $error("BAD PLOT INIT VALUES");

        #20;

        assert (dut.pipe_tex_addr == 2881 && dut.curr_y == -1 - 89 && dut.do_plot == 0) 
        else   $error("BAD PLOT VALUES");

        #20;

        assert (dut.frame_buffer_wren == 0) 
        else   $error("BAD FRAME BUFFER VALUES");

        wait(~slave_waitrequest);
        wait(clk == 0);

        assert (dut.frame_buffer.altsyncram_component.m_default.altsyncram_inst.mem_data[0] == 'b011100) 
        else   $error("BAD OUTPUT");

        //---------------------------------------------------------------------------
        // Plot multiplayer line (line sould overlap existing pipe)
        slave_address <= 4;
        slave_writedata <= 'b0_01_1111;     // texture code
        #20;
        slave_write <= 1;
        #20;
        slave_write <= 0;
        #20;

        assert (dut.tex_code == 'b0_01_1111) 
        else   $error("BAD TEXTURE VALUE");

        wait(~slave_waitrequest);
        wait(clk == 0);
        
        slave_address <= 6;
        slave_write <= 1;       // initiate plot
        #20;

        assert (dut.slave_waitrequest == 1) 
        else   $error("BAD WAITREQUEST");
        assert (dut.plotting == 1 && dut.plot_init == 1 && dut.fill_init == 1 && dut.bird_tex_addr == 0 && dut.pipe_tex_addr == 0 && dut.char_tex_addr == 0 && dut.custom_tex_addr == 0 && dut.do_plot == 0) 
        else   $error("BAD INITIAL PLOT VALUES");

        slave_write <= 0;
        #20;

        assert (dut.plot_init == 0 && dut.curr_y == 0 && dut.frame_buffer_data == 0) 
        else   $error("BAD PLOT INIT VALUES");

        #20;

        assert (dut.curr_y == 1 && dut.frame_buffer_wren == 1 && dut.frame_buffer_addr == 38160)
        else   $error("BAD FRAME BUFFER VALUES");

        wait(dut.curr_y == 240);
        wait(clk == 0);
        #20;

        assert (dut.frame_buffer_wren == 0 && dut.plotting == 0) 
        else   $error("BAD LINE END VALUES");

        wait(~slave_waitrequest);
        wait(clk == 0);

        //---------------------------------------------------------------------------
        // Plot character "9" (half of the character should be out of frame)
        slave_address <= 4;
        slave_writedata <= 'b0_01_0000;     // texture code
        #20;
        slave_write <= 1;
        #20;
        slave_write <= 0;
        #20;

        assert (dut.tex_code == 'b0_01_0000) 
        else   $error("BAD TEXTURE VALUE");

        wait(~slave_waitrequest);
        wait(clk == 0);

        slave_address <= 1;
        slave_writedata <= 159;      // midpoint x coordinate
        slave_write <= 1;
        #20;
        slave_write <= 0;
        #20;

        assert (dut.mid_x == 159) 
        else   $error("BAD X COOR VALUE");

        wait(~slave_waitrequest);
        wait(clk == 0);

        slave_address <= 2;
        slave_writedata <= 239;      // midpoint y coordinate
        slave_write <= 1;
        #20;
        slave_write <= 0;
        #20;

        assert (dut.mid_y == 239) 
        else   $error("BAD Y COOR VALUE");

        wait(~slave_waitrequest);
        wait(clk == 0);

        slave_address <= 6;
        slave_write <= 1;       // initiate plot
        #20;

        assert (dut.slave_waitrequest == 1) 
        else   $error("BAD WAITREQUEST");
        assert (dut.plotting == 1 && dut.plot_init == 1 && dut.fill_init == 1 && dut.bird_tex_addr == 0 && dut.pipe_tex_addr == 0 && dut.char_tex_addr == 0 && dut.custom_tex_addr == 0 && dut.do_plot == 0) 
        else   $error("BAD INITIAL PLOT VALUES");

        slave_write <= 0;
        #20;

        assert (dut.plot_init == 0 && dut.char_tex_addr == 5184 && dut.curr_x == 159 - 12 && dut.curr_y == 239 - 12) 
        else   $error("BAD PLOT INIT VALUES");

        #20;

        assert (dut.char_tex_addr == 5185 && dut.curr_y == 239 - 11 && dut.do_plot == 1 && dut.buf_addr_save == (159 - 12) * 240 + (239 - 12)) 
        else   $error("BAD PLOT VALUES");

        #20;

        assert (dut.frame_buffer_wren == 1 && dut.frame_buffer_addr == (159 - 12) * 240 + (239 - 12) && dut.frame_buffer_data == 'b111111) 
        else   $error("BAD FRAME BUFFER VALUES");

        wait(~slave_waitrequest);
        wait(clk == 0);
        
        #100;
        
        wait(dut.flushing == 1);
        wait(dut.flushing == 0);

        assert (dut.frame_out.altsyncram_component.m_default.altsyncram_inst.mem_data[0] == 'b011100) 
        else   $error("BAD OUTPUT");

        wait(dut.x == 319 && dut.y == 239);
        #20;

        assert (dut.x_prev == 319 && dut.y_prev == 239) 
        else   $error("BAD VIDEO VALUE");

        #20;

        assert (dut.reading == 0 && dut.frame_out_q == 'b011100) 
        else   $error("BAD VIDEO VALUE");

        $display("TESTS DONE!");
        $stop;

    end

	
endmodule
