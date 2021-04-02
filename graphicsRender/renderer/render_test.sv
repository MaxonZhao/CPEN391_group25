// THIS FILE TESTS PLOTTING TEXTURES ONTO THE SCREEN
// IT PLOTS ALL AVAILABLE TEXTURES ONTO SCREEN, WITH SOME BEING OUT OF BOUNDS ON PURPOSE

module render_test (HEX0, HEX1, HEX2, HEX3, HEX4, HEX5, KEY, LEDR, SW,
					 CLOCK_50, VGA_R, VGA_G, VGA_B, VGA_BLANK_N, VGA_CLK, VGA_HS, VGA_SYNC_N, VGA_VS);
	output logic [6:0] HEX0, HEX1, HEX2, HEX3, HEX4, HEX5;
	output logic [9:0] LEDR;
	input logic [3:0] KEY;
	input logic [9:0] SW;

	input logic CLOCK_50;
	output logic [7:0] VGA_R;
	output logic [7:0] VGA_G;
	output logic [7:0] VGA_B;
	output logic VGA_BLANK_N;
	output logic VGA_CLK;
	output logic VGA_HS;
	output logic VGA_SYNC_N;
	output logic VGA_VS;

	wire slave_waitrequest;
	reg [3:0] slave_address;
	reg slave_read, slave_write;
	wire [31:0] slave_readdata;
	reg [31:0] slave_writedata;
	
	render renderer (.clk(CLOCK_50), .rst_n(KEY[0]),

			.slave_waitrequest(slave_waitrequest), .slave_address(slave_address),
			.slave_read(slave_read), .slave_readdata(slave_readdata),
			.slave_write(slave_write), .slave_writedata(slave_writedata),

			.VGA_R(VGA_R), .VGA_G(VGA_G), .VGA_B(VGA_B),
			.VGA_BLANK_N(VGA_BLANK_N), .VGA_CLK(VGA_CLK),
			.VGA_HS(VGA_HS), .VGA_SYNC_N(VGA_SYNC_N), .VGA_VS(VGA_VS));

	reg [8:0] macro_state;
	reg [8:0] micro_state;
	reg [4:0] done_micro;
	reg drawing;
	
	always_ff @(posedge CLOCK_50, negedge KEY[0]) begin
		if (~KEY[0]) begin
			macro_state <= 0;
			micro_state <= 0;
			done_micro <= 0;
			drawing <= 0;

			// Default VGA screen is black
		end
		else begin
			if (~KEY[1] && ~drawing) begin
				drawing <= 1;
				if (macro_state < 6 ) macro_state <= macro_state + 1;
				else if (macro_state > 6) macro_state <= 0;
				micro_state <= 0;
				done_micro <= 31;
			end

			else if (~KEY[2] && ~drawing) begin
				drawing <= 1;
				if (macro_state < 7) macro_state <= 7;
				else if (macro_state >= 7 && macro_state < 30) macro_state <= macro_state + 1;
				micro_state <= 0;
				done_micro <= 31;
			end

			/////////////////////////////////////////////////////////////////////////////////////////
			// TEST 1: Test out of bounds behavior

			// Make background grey
			else if (macro_state == 1 && drawing) begin
				case (micro_state)
					// Write texture code
					0: begin
						if (~slave_waitrequest && done_micro == 31) begin
							slave_address <= 4;
							slave_write <= 1;
							slave_writedata <= 'b110_1010;
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
							slave_writedata <= 'b000_0110;
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

			// Plot 1st bird at side of screen
			else if (macro_state == 3 && drawing) begin
				case (micro_state)
					// Write texture code
					0: begin
						if (~slave_waitrequest && done_micro == 31) begin
							slave_address <= 4;
							slave_write <= 1;
							slave_writedata <= 'b001_0001;
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
							slave_writedata <= 20;
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

			// Plot game over title at bottom of screen
			else if (macro_state == 4 && drawing) begin
				case (micro_state)
					// Write texture code
					0: begin
						if (~slave_waitrequest && done_micro == 31) begin
							slave_address <= 4;
							slave_write <= 1;
							slave_writedata <= 'b001_0100;
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
							slave_writedata <= 239;
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

			// Plot silver medal at top left of screen
			else if (macro_state == 5 && drawing) begin
				case (micro_state)
					// Write texture code
					0: begin
						if (~slave_waitrequest && done_micro == 31) begin
							slave_address <= 4;
							slave_write <= 1;
							slave_writedata <= 'b001_0010;
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
							slave_writedata <= 19;
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
							slave_writedata <= 13;
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

			// Plot 2nd bird at side of screen
			else if (macro_state == 6 && drawing) begin
				case (micro_state)
					// Write texture code
					0: begin
						if (~slave_waitrequest && done_micro == 31) begin
							slave_address <= 4;
							slave_write <= 1;
							slave_writedata <= 'b000_0001;
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
							slave_writedata <= 80;
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
							slave_writedata <= 100;
							done_micro <= 2;
						end
						else if (done_micro == 2) begin
							slave_write <= 0;
							micro_state <= 3;
						end
					end

					// Write bird color
					3: begin
						if (~slave_waitrequest && done_micro == 2) begin
							slave_address <= 7;
							slave_write <= 1;
							slave_writedata <= 'b000000;
							done_micro <= 3;
						end
						else if (done_micro == 3) begin
							slave_write <= 0;
							micro_state <= 4;
						end
					end

					// Plot
					4: begin
						if (~slave_waitrequest && done_micro == 3) begin
							slave_address <= 6;
							slave_write <= 1;
							done_micro <= 4;
						end
						else if (done_micro == 4) begin
							slave_write <= 0;
							micro_state <= 5;
						end
					end

					// Done
					5: begin
						drawing <= 0;
					end
				endcase
			end

			// Plot pipe above screen
			else if (macro_state == 7 && drawing) begin
				case (micro_state)
					// Write texture code
					0: begin
						if (~slave_waitrequest && done_micro == 31) begin
							slave_address <= 4;
							slave_write <= 1;
							slave_writedata <= 5;
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
							slave_writedata <= 150;
							done_micro <= 1;
						end
						else if (done_micro == 1) begin
							slave_write <= 0;
							micro_state <= 2;
						end
					end

					// Set negative coor
					2: begin
						if (~slave_waitrequest && done_micro == 1) begin
							slave_address <= 3;
							slave_write <= 1;
							slave_writedata <= 1;
							done_micro <= 2;
						end
						else if (done_micro == 2) begin
							slave_write <= 0;
							micro_state <= 3;
						end
					end

					// Write y coor
					3: begin
						if (~slave_waitrequest && done_micro == 2) begin
							slave_address <= 2;
							slave_write <= 1;
							slave_writedata <= 10;
							done_micro <= 3;
						end
						else if (done_micro == 3) begin
							slave_write <= 0;
							micro_state <= 4;
						end
					end

					// Set positive coor
					4: begin
						if (~slave_waitrequest && done_micro == 3) begin
							slave_address <= 3;
							slave_write <= 0;
							slave_writedata <= 0;
							done_micro <= 4;
						end
						else if (done_micro == 4) begin
							slave_write <= 0;
							micro_state <= 5;
						end
					end

					// Plot
					5: begin
						if (~slave_waitrequest && done_micro == 4) begin
							slave_address <= 6;
							slave_write <= 1;
							done_micro <= 5;
						end
						else if (done_micro == 5) begin
							slave_write <= 0;
							micro_state <= 6;
						end
					end

					// Done
					6: begin
						drawing <= 0;
					end
				endcase
			end

			////////////////////////////////////////////////////////////////////////////////////////
			// TEST 2: Check all textures

			// Make background green
			else if (macro_state == 7 && drawing) begin
				case (micro_state)
					// Write texture code
					0: begin
						if (~slave_waitrequest && done_micro == 31) begin
							slave_address <= 4;
							slave_write <= 1;
							slave_writedata <= 'b100_1100;
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

			///////////////////////////////////////////////////////////////////////////////////////
			// PLOT BIRDS

			// 1st bird (black)
			else if (macro_state == 8 && drawing) begin
				case (micro_state)
					// Write texture code
					0: begin
						if (~slave_waitrequest && done_micro == 31) begin
							slave_address <= 4;
							slave_write <= 1;
							slave_writedata <= 'b000_0001;
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
							slave_writedata <= 10;
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
							slave_writedata <= 7;
							done_micro <= 2;
						end
						else if (done_micro == 2) begin
							slave_write <= 0;
							micro_state <= 3;
						end
					end

					// Write bird color
					3: begin
						if (~slave_waitrequest && done_micro == 2) begin
							slave_address <= 7;
							slave_write <= 1;
							slave_writedata <= 'b000000;
							done_micro <= 3;
						end
						else if (done_micro == 3) begin
							slave_write <= 0;
							micro_state <= 4;
						end
					end

					// Plot
					4: begin
						if (~slave_waitrequest && done_micro == 3) begin
							slave_address <= 6;
							slave_write <= 1;
							done_micro <= 4;
						end
						else if (done_micro == 4) begin
							slave_write <= 0;
							micro_state <= 5;
						end
					end

					// Done
					5: begin
						drawing <= 0;
					end
				endcase
			end

			// 2nd bird (orange)
			else if (macro_state == 9 && drawing) begin
				case (micro_state)
					// Write texture code
					0: begin
						if (~slave_waitrequest && done_micro == 31) begin
							slave_address <= 4;
							slave_write <= 1;
							slave_writedata <= 'b000_0010;
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
							slave_writedata <= 10;
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
							slave_writedata <= 20;
							done_micro <= 2;
						end
						else if (done_micro == 2) begin
							slave_write <= 0;
							micro_state <= 3;
						end
					end

					// Write bird color
					3: begin
						if (~slave_waitrequest && done_micro == 2) begin
							slave_address <= 7;
							slave_write <= 1;
							slave_writedata <= 'b111000;
							done_micro <= 3;
						end
						else if (done_micro == 3) begin
							slave_write <= 0;
							micro_state <= 4;
						end
					end

					// Plot
					4: begin
						if (~slave_waitrequest && done_micro == 3) begin
							slave_address <= 6;
							slave_write <= 1;
							done_micro <= 4;
						end
						else if (done_micro == 4) begin
							slave_write <= 0;
							micro_state <= 5;
						end
					end

					// Done
					5: begin
						drawing <= 0;
					end
				endcase
			end

			// 3rd bird (yellow)
			else if (macro_state == 10 && drawing) begin
				case (micro_state)
					// Write texture code
					0: begin
						if (~slave_waitrequest && done_micro == 31) begin
							slave_address <= 4;
							slave_write <= 1;
							slave_writedata <= 'b000_0011;
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
							slave_writedata <= 10;
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
							slave_writedata <= 33;
							done_micro <= 2;
						end
						else if (done_micro == 2) begin
							slave_write <= 0;
							micro_state <= 3;
						end
					end

					// Write bird color
					3: begin
						if (~slave_waitrequest && done_micro == 2) begin
							slave_address <= 7;
							slave_write <= 1;
							slave_writedata <= 'b111100;
							done_micro <= 3;
						end
						else if (done_micro == 3) begin
							slave_write <= 0;
							micro_state <= 4;
						end
					end

					// Plot
					4: begin
						if (~slave_waitrequest && done_micro == 3) begin
							slave_address <= 6;
							slave_write <= 1;
							done_micro <= 4;
						end
						else if (done_micro == 4) begin
							slave_write <= 0;
							micro_state <= 5;
						end
					end

					// Done
					5: begin
						drawing <= 0;
					end
				endcase
			end

			// 4th bird (pink)
			else if (macro_state == 11 && drawing) begin
				case (micro_state)
					// Write texture code
					0: begin
						if (~slave_waitrequest && done_micro == 31) begin
							slave_address <= 4;
							slave_write <= 1;
							slave_writedata <= 'b000_0100;
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
							slave_writedata <= 10;
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
							slave_writedata <= 46;
							done_micro <= 2;
						end
						else if (done_micro == 2) begin
							slave_write <= 0;
							micro_state <= 3;
						end
					end

					// Write bird color
					3: begin
						if (~slave_waitrequest && done_micro == 2) begin
							slave_address <= 7;
							slave_write <= 1;
							slave_writedata <= 'b110011;
							done_micro <= 3;
						end
						else if (done_micro == 3) begin
							slave_write <= 0;
							micro_state <= 4;
						end
					end

					// Plot
					4: begin
						if (~slave_waitrequest && done_micro == 3) begin
							slave_address <= 6;
							slave_write <= 1;
							done_micro <= 4;
						end
						else if (done_micro == 4) begin
							slave_write <= 0;
							micro_state <= 5;
						end
					end

					// Done
					5: begin
						drawing <= 0;
					end
				endcase
			end

			///////////////////////////////////////////////////////////////////////////////////////
			// PLOT NUMBERS

			// Plot 0
			else if (macro_state == 12 && drawing) begin
				case (micro_state)
					// Write texture code
					0: begin
						if (~slave_waitrequest && done_micro == 31) begin
							slave_address <= 4;
							slave_write <= 1;
							slave_writedata <= 7;
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
							slave_writedata <= 13;
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
							slave_writedata <= 59;
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

			// Plot 1
			else if (macro_state == 13 && drawing) begin
				case (micro_state)
					// Write texture code
					0: begin
						if (~slave_waitrequest && done_micro == 31) begin
							slave_address <= 4;
							slave_write <= 1;
							slave_writedata <= 8;
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
							slave_writedata <= 13;
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
							slave_writedata <= 84;
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

			// Plot 2
			else if (macro_state == 14 && drawing) begin
				case (micro_state)
					// Write texture code
					0: begin
						if (~slave_waitrequest && done_micro == 31) begin
							slave_address <= 4;
							slave_write <= 1;
							slave_writedata <= 9;
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
							slave_writedata <= 13;
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
							slave_writedata <= 109;
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

			// Plot 3
			else if (macro_state == 15 && drawing) begin
				case (micro_state)
					// Write texture code
					0: begin
						if (~slave_waitrequest && done_micro == 31) begin
							slave_address <= 4;
							slave_write <= 1;
							slave_writedata <= 10;
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
							slave_writedata <= 13;
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
							slave_writedata <= 134;
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

			// Plot 4
			else if (macro_state == 16 && drawing) begin
				case (micro_state)
					// Write texture code
					0: begin
						if (~slave_waitrequest && done_micro == 31) begin
							slave_address <= 4;
							slave_write <= 1;
							slave_writedata <= 11;
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
							slave_writedata <= 13;
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
							slave_writedata <= 159;
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

			// Plot 5
			else if (macro_state == 17 && drawing) begin
				case (micro_state)
					// Write texture code
					0: begin
						if (~slave_waitrequest && done_micro == 31) begin
							slave_address <= 4;
							slave_write <= 1;
							slave_writedata <= 12;
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
							slave_writedata <= 13;
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
							slave_writedata <= 184;
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

			// Plot 6
			else if (macro_state == 18 && drawing) begin
				case (micro_state)
					// Write texture code
					0: begin
						if (~slave_waitrequest && done_micro == 31) begin
							slave_address <= 4;
							slave_write <= 1;
							slave_writedata <= 13;
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
							slave_writedata <= 13;
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
							slave_writedata <= 209;
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

			// Plot 7
			else if (macro_state == 19 && drawing) begin
				case (micro_state)
					// Write texture code
					0: begin
						if (~slave_waitrequest && done_micro == 31) begin
							slave_address <= 4;
							slave_write <= 1;
							slave_writedata <= 14;
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
							slave_writedata <= 38;
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
							slave_writedata <= 13;
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

			// Plot 8
			else if (macro_state == 20 && drawing) begin
				case (micro_state)
					// Write texture code
					0: begin
						if (~slave_waitrequest && done_micro == 31) begin
							slave_address <= 4;
							slave_write <= 1;
							slave_writedata <= 15;
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
							slave_writedata <= 38;
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
							slave_writedata <= 38;
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

			// Plot 9
			else if (macro_state == 21 && drawing) begin
				case (micro_state)
					// Write texture code
					0: begin
						if (~slave_waitrequest && done_micro == 31) begin
							slave_address <= 4;
							slave_write <= 1;
							slave_writedata <= 16;
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
							slave_writedata <= 38;
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
							slave_writedata <= 63;
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

			/////////////////////////////////////////////////////////////////////////
			// Plot gold and silver medals

			// Plot gold medal
			else if (macro_state == 22 && drawing) begin
				case (micro_state)
					// Write texture code
					0: begin
						if (~slave_waitrequest && done_micro == 31) begin
							slave_address <= 4;
							slave_write <= 1;
							slave_writedata <= 17;
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
							slave_writedata <= 40;
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
							slave_writedata <= 90;
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

			// Plot silver medal
			else if (macro_state == 23 && drawing) begin
				case (micro_state)
					// Write texture code
					0: begin
						if (~slave_waitrequest && done_micro == 31) begin
							slave_address <= 4;
							slave_write <= 1;
							slave_writedata <= 18;
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
							slave_writedata <= 40;
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

			////////////////////////////////////////////////////////////////////////////////
			// Plot pipes

			// Plot pipe up
			else if (macro_state == 24 && drawing) begin
				case (micro_state)
					// Write texture code
					0: begin
						if (~slave_waitrequest && done_micro == 31) begin
							slave_address <= 4;
							slave_write <= 1;
							slave_writedata <= 5;
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
							slave_writedata <= 63;
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

			// Plot pipe down
			else if (macro_state == 25 && drawing) begin
				case (micro_state)
					// Write texture code
					0: begin
						if (~slave_waitrequest && done_micro == 31) begin
							slave_address <= 4;
							slave_write <= 1;
							slave_writedata <= 6;
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
							slave_writedata <= 80;
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

			///////////////////////////////////////////////////////////////////////
			// PLOT CUSTOM TEXTURES

			// Plot flappy bird title
			else if (macro_state == 26 && drawing) begin
				case (micro_state)
					// Write texture code
					0: begin
						if (~slave_waitrequest && done_micro == 31) begin
							slave_address <= 4;
							slave_write <= 1;
							slave_writedata <= 19;
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
							slave_writedata <= 164;
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
							slave_writedata <= 21;
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

			// Plot game over sign
			else if (macro_state == 27 && drawing) begin
				case (micro_state)
					// Write texture code
					0: begin
						if (~slave_waitrequest && done_micro == 31) begin
							slave_address <= 4;
							slave_write <= 1;
							slave_writedata <= 20;
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
							slave_writedata <= 164;
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
							slave_writedata <= 56;
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
	
	assign HEX0 = 7'b1000010;
	assign HEX1 = 7'b1000001;
	assign HEX2 = 7'b0000000;
	assign HEX3 = 7'b0000110;
	assign HEX4 = 7'b1000000;
	assign LEDR = 10'b0101010101;
	assign HEX5 = 7'b1111111;
	
endmodule
