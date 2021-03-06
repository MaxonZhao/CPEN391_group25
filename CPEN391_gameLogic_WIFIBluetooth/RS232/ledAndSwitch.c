/*
 * ledAndSwitch.c
 *
 *  Created on: Mar 1, 2021
 *      Author: Zhuoyi Li
 */

/**
 * This file is used to test if the built HPS system works
 * It will read the switches and drive the LEDS and hex display
 */

#include <stdio.h>
// ALL parallel IO ports created by QSYS have a 32 bit wide interface as far as the processor,
// that is, it reads and writes 32 bit data to the port, even though the
// port itself might only be configures as an 8 or 10 or even 4 bit wide port
//
#define SWITCHES (volatile unsigned int *)(0xFF200000)
#define PUSHBUTTONS (volatile unsigned int *)(0xFF200010)

#define LEDS (volatile unsigned int *)(0xFF200020)
#define HEX0_1 (volatile unsigned int *)(0xFF200030)
#define HEX2_3 (volatile unsigned int *)(0xFF200040)
#define HEX4_5 (volatile unsigned int *)(0xFF200050)

//
//void main(void)
//{
//    int switches ;
//	printf("Hello from the CPEN 391 System\n");
//
//    while(1)    {
//        switches = *SWITCHES ;
//        *LEDS = switches;
//        *HEX0_1 = switches;
//        *HEX2_3 = switches;
//        *HEX4_5 = switches;
//
//        printf("Switches = %x\n", switches) ;
//        printf("Buttons = %x\n", *PUSHBUTTONS) ;
//    }
//}
//