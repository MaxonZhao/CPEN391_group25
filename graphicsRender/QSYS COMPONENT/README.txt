Do install the renderer into QSYS as a compoenent:

1. Copy RENDER_FILES folder and renderer_hw.tcl into the QSYS project directory.

2. In QSYS, create a renderer component.

3. Connect "clk" to the system clock.

4. Connect "rst_n" to the system reset.

5. Connect "slave" to "h2f_lw_axi_master".

6. Export "conduit" as "vga".

7. If you want to use the provided sample C code as is for demo, set the following map addresses:

ARM_A9_HPS.h2f_lw_axi_master: 0x0000_2080

JTAG_To_FPGA_Bridge.master: 0xff20_2080