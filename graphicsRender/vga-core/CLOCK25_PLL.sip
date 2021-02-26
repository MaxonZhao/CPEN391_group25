set_global_assignment -entity "CLOCK25_PLL" -library "lib_CLOCK25_PLL" -name IP_TOOL_NAME "altera_pll"
set_global_assignment -entity "CLOCK25_PLL" -library "lib_CLOCK25_PLL" -name IP_TOOL_VERSION "17.0"
set_global_assignment -entity "CLOCK25_PLL" -library "lib_CLOCK25_PLL" -name IP_TOOL_ENV "mwpim"
set_global_assignment -library "lib_CLOCK25_PLL" -name SPD_FILE [file join $::quartus(sip_path) "CLOCK25_PLL.spd"]

set_global_assignment -library "lib_CLOCK25_PLL" -name MISC_FILE [file join $::quartus(sip_path) "CLOCK25_PLL_sim/CLOCK25_PLL.vo"]
