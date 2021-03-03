module render_test (HEX0, HEX1, HEX2, HEX3, HEX4, HEX5, KEY, LEDR, SW,
					 CLOCK_50, VGA_R, VGA_G, VGA_B, VGA_BLANK_N, VGA_CLK, VGA_HS, VGA_SYNC_N, VGA_VS);
	output logic [6:0] HEX0, HEX1, HEX2, HEX3, HEX4, HEX5;
	output logic [9:0] LEDR;
	input logic [3:0] KEY;
	input logic [9:0] SW;

	input CLOCK_50;
	output [7:0] VGA_R;
	output [7:0] VGA_G;
	output [7:0] VGA_B;
	output VGA_BLANK_N;
	output VGA_CLK;
	output VGA_HS;
	output VGA_SYNC_N;
	output VGA_VS;

	wire slave_waitrequest;
	reg [3:0] slave_address;
	reg slave_read, slave_write;
	wire [31:0] slave_readdata;
	reg [31:0] slave_writedata;
	
	render (.clk(CLOCK_50), .rst_n(~KEY[0]),

			.slave_waitrequest(slave_waitrequest), .slave_address(slave_address),
			.slave_read(slave_read), .slave_readdata(slave_readdata),
			.slave_write(slave_write), .slave_writedata(slave_writedata),

			.VGA_R(VGA_R), .VGA_G(VGA_G), .VGA_B(VGA_B),
			.VGA_BLANK_N(VGA_BLANK_N), .VGA_CLK(VGA_CLK),
			.VGA_HS(VGA_HS), .VGA_SYNC_N(VGA_SYNC_N), .VGA_VS(VGA_VS));

	reg [4:0] macro_state;
	reg [4:0] micro_state;
	reg [4:0] done_micro;
	reg drawing;
	
	always_ff @(posedge CLOCK_50) begin
		if (~KEY[0]) begin
			macro_state <= 0;
			micro_state <= 0;
			done_micro <= 0;
			drawing <= 0;

			// Default VGA screen is black
		end
		else begin
			if (KEY[1] && ~drawing) begin
				drawing <= 1;
				macro_state <= macro_state + 1;
				micro_state <= 0;
				done_micro <= 31;
			end

			// Make background yellow
			else if (macro_state == 1 && drawing) begin
				case (micro_state)
					// Write texture code
					0: begin
						if (~slave_waitrequest && done_micro == 31) begin
							slave_address <= 4;
							slave_write <= 1;
							slave_writedata <= 'b111100;
							done_micro <= 0;
						end
						else if (done_micro == 0) begin
							slave_write <= 0;
							micro_state <= 1;
						end
					end
					// Plot background
					1: begin
						if (~slave_waitrequest && done_micro == 0) begin
							slave_address <= 6;
							slave_write <= 1;
							done_micro <= 1;
						end
						else if (done_micro == 1) begin
							slave_write <= 0;
							micro_state <= 2;
						end
					end
					// Ready for next action
					2: begin
						drawing <= 0;
					end
				endcase
			end

			// Plot pipe at center of screen
			else if (macro_state == 2 && drawing) begin
				case (micro_state)
					// Write texture code
					0: begin
						if (~slave_waitrequest && done_micro == 31) begin
							slave_address <= 4;
							slave_write <= 1;
							slave_writedata <= 'b0000101;
							done_micro <= 0;
						end
						else if (done_micro == 0) begin
							slave_write <= 0;
							micro_state <= 1;
						end
					end

					// Write x coordinate
					1: begin
						if (~slave_waitrequest && done_micro == 0) begin
							slave_address <= 1;
							slave_write <= 1;
							slave_writedata <= 159;
							done_micro <= 1;
						end
						else if (done_micro == 1) begin
							slave_write <= 0;
							micro_state <= 2;
						end
					end

					// Write y coordinate
					2: begin
						if (~slave_waitrequest && done_micro == 1) begin
							slave_address <= 2;
							slave_write <= 1;
							slave_writedata <= 119;
							done_micro <= 2;
						end
						else if (done_micro == 2) begin
							slave_write <= 0;
							micro_state <= 3;
						end
					end

					// Plot
					3: begin
						if (~slave_waitrequest && done_micro == 2) begin
							slave_address <= 6;
							slave_write <= 1;
							done_micro <= 3;
						end
						else if (done_micro == 3) begin
							slave_write <= 0;
							micro_state <= 4;
						end
					end

					// Done
					4: begin
						drawing <= 0;
					end
				endcase
			end
		end
	end
	
	assign HEX0 = 7'b0100001;
	assign HEX1 = 7'b1000001;
	assign HEX2 = 7'b0000000;
	assign HEX3 = 7'b0110000;
	assign HEX4 = 7'b0000001;
	assign LEDR = 10'b0101010101;
	assign HEX5 = 7'b1;
	
endmodule
