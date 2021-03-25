# TCL File Generated by Component Editor 15.0
# Wed Mar 24 18:33:39 PDT 2021
# DO NOT MODIFY


# 
# renderer "renderer" v1.1
#  2021.03.24.18:33:39
# 
# 

# 
# request TCL package from ACDS 15.0
# 
package require -exact qsys 15.0


# 
# module renderer
# 
set_module_property DESCRIPTION ""
set_module_property NAME renderer
set_module_property VERSION 1.1
set_module_property INTERNAL false
set_module_property OPAQUE_ADDRESS_MAP true
set_module_property AUTHOR ""
set_module_property DISPLAY_NAME renderer
set_module_property INSTANTIATE_IN_SYSTEM_MODULE true
set_module_property EDITABLE true
set_module_property REPORT_TO_TALKBACK false
set_module_property ALLOW_GREYBOX_GENERATION false
set_module_property REPORT_HIERARCHY false


# 
# file sets
# 
add_fileset QUARTUS_SYNTH QUARTUS_SYNTH "" ""
set_fileset_property QUARTUS_SYNTH TOP_LEVEL render
set_fileset_property QUARTUS_SYNTH ENABLE_RELATIVE_INCLUDE_PATHS false
set_fileset_property QUARTUS_SYNTH ENABLE_FILE_OVERWRITE_MODE true
add_fileset_file CLOCK25_PLL.sv SYSTEM_VERILOG PATH RENDER_FILES/CLOCK25_PLL.sv
add_fileset_file CLOCK25_PLL_0002.v VERILOG PATH RENDER_FILES/CLOCK25_PLL_0002.v
add_fileset_file altera_up_avalon_video_vga_timing.sv SYSTEM_VERILOG PATH RENDER_FILES/altera_up_avalon_video_vga_timing.sv
add_fileset_file bird_texture.mif MIF PATH RENDER_FILES/bird_texture.mif
add_fileset_file birds.v VERILOG PATH RENDER_FILES/birds.v
add_fileset_file char_texture.mif MIF PATH RENDER_FILES/char_texture.mif
add_fileset_file chars.v VERILOG PATH RENDER_FILES/chars.v
add_fileset_file custom.v VERILOG PATH RENDER_FILES/custom.v
add_fileset_file custom_texture.mif MIF PATH RENDER_FILES/custom_texture.mif
add_fileset_file frame.v VERILOG PATH RENDER_FILES/frame.v
add_fileset_file frame_out.v VERILOG PATH RENDER_FILES/frame_out.v
add_fileset_file pipe_texture.mif MIF PATH RENDER_FILES/pipe_texture.mif
add_fileset_file pipes.v VERILOG PATH RENDER_FILES/pipes.v
add_fileset_file render.sv SYSTEM_VERILOG PATH RENDER_FILES/render.sv TOP_LEVEL_FILE
add_fileset_file video_driver.sv SYSTEM_VERILOG PATH RENDER_FILES/video_driver.sv

add_fileset SIM_VERILOG SIM_VERILOG "" ""
set_fileset_property SIM_VERILOG TOP_LEVEL render
set_fileset_property SIM_VERILOG ENABLE_RELATIVE_INCLUDE_PATHS false
set_fileset_property SIM_VERILOG ENABLE_FILE_OVERWRITE_MODE true
add_fileset_file birds.v VERILOG PATH RENDER_FILES/birds.v
add_fileset_file chars.v VERILOG PATH RENDER_FILES/chars.v
add_fileset_file custom.v VERILOG PATH RENDER_FILES/custom.v
add_fileset_file pipes.v VERILOG PATH RENDER_FILES/pipes.v
add_fileset_file frame.v VERILOG PATH RENDER_FILES/frame.v
add_fileset_file frame_out.v VERILOG PATH RENDER_FILES/frame_out.v
add_fileset_file render.sv SYSTEM_VERILOG PATH RENDER_FILES/render.sv
add_fileset_file CLOCK25_PLL.sv SYSTEM_VERILOG PATH RENDER_FILES/CLOCK25_PLL.sv
add_fileset_file CLOCK25_PLL_0002.v VERILOG PATH RENDER_FILES/CLOCK25_PLL_0002.v
add_fileset_file altera_up_avalon_video_vga_timing.sv SYSTEM_VERILOG PATH RENDER_FILES/altera_up_avalon_video_vga_timing.sv
add_fileset_file bird_texture.mif MIF PATH RENDER_FILES/bird_texture.mif
add_fileset_file char_texture.mif MIF PATH RENDER_FILES/char_texture.mif
add_fileset_file custom_texture.mif MIF PATH RENDER_FILES/custom_texture.mif
add_fileset_file pipe_texture.mif MIF PATH RENDER_FILES/pipe_texture.mif
add_fileset_file video_driver.sv SYSTEM_VERILOG PATH RENDER_FILES/video_driver.sv


# 
# parameters
# 


# 
# display items
# 


# 
# connection point clock
# 
add_interface clock clock end
set_interface_property clock clockRate 0
set_interface_property clock ENABLED true
set_interface_property clock EXPORT_OF ""
set_interface_property clock PORT_NAME_MAP ""
set_interface_property clock CMSIS_SVD_VARIABLES ""
set_interface_property clock SVD_ADDRESS_GROUP ""

add_interface_port clock clk clk Input 1


# 
# connection point slave
# 
add_interface slave avalon end
set_interface_property slave addressUnits WORDS
set_interface_property slave associatedClock clock
set_interface_property slave associatedReset reset
set_interface_property slave bitsPerSymbol 8
set_interface_property slave burstOnBurstBoundariesOnly false
set_interface_property slave burstcountUnits WORDS
set_interface_property slave explicitAddressSpan 0
set_interface_property slave holdTime 0
set_interface_property slave linewrapBursts false
set_interface_property slave maximumPendingReadTransactions 0
set_interface_property slave maximumPendingWriteTransactions 0
set_interface_property slave readLatency 0
set_interface_property slave readWaitTime 1
set_interface_property slave setupTime 0
set_interface_property slave timingUnits Cycles
set_interface_property slave writeWaitTime 0
set_interface_property slave ENABLED true
set_interface_property slave EXPORT_OF ""
set_interface_property slave PORT_NAME_MAP ""
set_interface_property slave CMSIS_SVD_VARIABLES ""
set_interface_property slave SVD_ADDRESS_GROUP ""

add_interface_port slave slave_waitrequest waitrequest Output 1
add_interface_port slave slave_address address Input 4
add_interface_port slave slave_read read Input 1
add_interface_port slave slave_readdata readdata Output 32
add_interface_port slave slave_write write Input 1
add_interface_port slave slave_writedata writedata Input 32
set_interface_assignment slave embeddedsw.configuration.isFlash 0
set_interface_assignment slave embeddedsw.configuration.isMemoryDevice 0
set_interface_assignment slave embeddedsw.configuration.isNonVolatileStorage 0
set_interface_assignment slave embeddedsw.configuration.isPrintableDevice 0


# 
# connection point conduit
# 
add_interface conduit conduit end
set_interface_property conduit associatedClock clock
set_interface_property conduit associatedReset ""
set_interface_property conduit ENABLED true
set_interface_property conduit EXPORT_OF ""
set_interface_property conduit PORT_NAME_MAP ""
set_interface_property conduit CMSIS_SVD_VARIABLES ""
set_interface_property conduit SVD_ADDRESS_GROUP ""

add_interface_port conduit VGA_R vga_r Output 8
add_interface_port conduit VGA_G vga_g Output 8
add_interface_port conduit VGA_B vga_b Output 8
add_interface_port conduit VGA_BLANK_N vga_blank_n Output 1
add_interface_port conduit VGA_CLK vga_clk Output 1
add_interface_port conduit VGA_HS vga_hs Output 1
add_interface_port conduit VGA_SYNC_N vga_sync_n Output 1
add_interface_port conduit VGA_VS vga_vs Output 1


# 
# connection point reset
# 
add_interface reset reset end
set_interface_property reset associatedClock clock
set_interface_property reset synchronousEdges DEASSERT
set_interface_property reset ENABLED true
set_interface_property reset EXPORT_OF ""
set_interface_property reset PORT_NAME_MAP ""
set_interface_property reset CMSIS_SVD_VARIABLES ""
set_interface_property reset SVD_ADDRESS_GROUP ""

add_interface_port reset rst_n reset_n Input 1

