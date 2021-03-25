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
        clk <= 1'b0; #10;
        clk <= 1'b1; #10;
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
        wait(~slave_waitrequest);
        slave_write <= 1;
        #20;
        slave_write <= 0;
        #20;

        wait(~slave_waitrequest);
        slave_address <= 6;
        slave_write <= 1;   // initiate plot
        #20; 
        slave_write <= 0;

        wait(~slave_waitrequest);

        //---------------------------------------------------------------------------
        // Plot first bird
        slave_address <= 4;
        slave_writedata <= 'b0_00_0001;     // texture code
        #20;
        wait(~slave_waitrequest);
        slave_write <= 1;
        #20;
        slave_write <= 0;
        #20;

        wait(~slave_waitrequest);
        slave_address <= 1;
        slave_writedata <= 20;      // midpoint x coordinate
        slave_write <= 1;
        #20;
        slave_write <= 0;
        #20;

        wait(~slave_waitrequest);
        slave_address <= 2;
        slave_writedata <= 20;      // midpoint y coordinate
        slave_write <= 1;
        #20;
        slave_write <= 0;
        #20;

        wait(~slave_waitrequest);
        slave_address <= 6;
        slave_write <= 1;       // initiate plot
        #20;
        slave_write <= 0;
        #20;

        wait(~slave_waitrequest);

        //---------------------------------------------------------------------------
        // Plot pipe (down)
        slave_address <= 4;
        slave_writedata <= 'b0_00_0110;     // texture code
        #20;
        wait(~slave_waitrequest);
        slave_write <= 1;
        #20;
        slave_write <= 0;
        #20;

        wait(~slave_waitrequest);
        slave_address <= 1;
        slave_writedata <= 159;      // midpoint x coordinate
        slave_write <= 1;
        #20;
        slave_write <= 0;
        #20;

        wait(~slave_waitrequest);
        slave_address <= 2;
        slave_writedata <= 230;      // midpoint y coordinate
        slave_write <= 1;
        #20;
        slave_write <= 0;
        #20;

        wait(~slave_waitrequest);
        slave_address <= 6;
        slave_write <= 1;       // initiate plot
        #20;
        slave_write <= 0;
        #20;

        wait(~slave_waitrequest);

        //---------------------------------------------------------------------------
        // Plot multiplayer line (line sould overlap existing pipe)
        slave_address <= 4;
        slave_writedata <= 'b0_01_1111;     // texture code
        #20;
        wait(~slave_waitrequest);
        slave_write <= 1;
        #20;
        slave_write <= 0;
        #20;

        wait(~slave_waitrequest);
        slave_address <= 6;
        slave_write <= 1;       // initiate plot
        #20;
        slave_write <= 0;
        #20;

        wait(~slave_waitrequest);

        //---------------------------------------------------------------------------
        // Plot character "a" (half of the character should be out of frame)
        slave_address <= 4;
        slave_writedata <= 'b0_00_1001;     // texture code
        #20;
        wait(~slave_waitrequest);
        slave_write <= 1;
        #20;
        slave_write <= 0;
        #20;

        wait(~slave_waitrequest);
        slave_address <= 1;
        slave_writedata <= 159;      // midpoint x coordinate
        slave_write <= 1;
        #20;
        slave_write <= 0;
        #20;

        wait(~slave_waitrequest);
        slave_address <= 2;
        slave_writedata <= 239;      // midpoint y coordinate
        slave_write <= 1;
        #20;
        slave_write <= 0;
        #20;

        wait(~slave_waitrequest);
        slave_address <= 6;
        slave_write <= 1;       // initiate plot
        #20;
        slave_write <= 0;
        #20;

        wait(~slave_waitrequest);
        

    end

	
endmodule
