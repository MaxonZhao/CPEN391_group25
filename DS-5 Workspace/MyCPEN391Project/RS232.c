/*
 * RS232.c
 *
 *  Created on: Mar 1, 2021
 *      Author: zoeyli
 */

#include <stdio.h>
#include <string.h>

#include "serial.h"
/**************************************************************************
**  Subroutine to initialize the BT Port by writing some data
**  to the internal registers.
**  Call this function at the start of the program before you attempt
**  to read or write to data via the BT port
**
**  Refer to UARTdata sheet for details of registers and programming
***************************************************************************/

void Init_RS232(void){
    // set bit 7 of Line Control Register to 1, to gain access to the baud rate registers
    RS232_LineControlReg =  0x80;
    // set Divisor latch (LSB and MSB) with correct value for required baud rate
    //baud rate divisor value=freq of BR_clk/ (desired buad rate*16)
    //here buad rate = 9600
    RS232_DivisorLatchLSB = 0x45;
    RS232_DivisorLatchMSB = 0x01;
    // set bit 7 of Line control register back to 0 and
    //RS232_LineControlReg = BT_LineControlReg & 0x7F;
    // program other bits in that reg for 8 bit data, 1 stop bit, no parity etc
    //bit 1-0 = 11, bit 2 =0, bit 3 = 0
    RS232_LineControlReg = 0x03;
    // Reset the Fifo in the FiFo Control Reg by setting bits 1 & 2 to 1
    RS232_FifoControlReg = 0x06;
    // Now Clear all bits in the FiFo control registers
    RS232_FifoControlReg = 0x00;

}

// the following function polls the UART to determine if any character
// has been received. It doesn't wait for one,or read it, it simply tests
// to see if one is available to read from the FIFO

int RS232_TestForReceivedData(void){
    // if BT_LineStatusReg bit 0 is set to 1
    //return TRUE, otherwise return FALSE
    if((RS232_LineStatusReg & 0x01)== 0x01){
        return 1;
    }
    else return 0;

}

int putcharRS232(int c){
    // wait for Transmitter Holding Register bit (5) of line status register to be '1'
    // indicating we can write to the device
    while((RS232_LineStatusReg  & 0x20)!= 0x20);

    // write character to Transmitter fifo register
    RS232_TransmitterFifo = (char)c;
    // return the character we printed
    return c;
}

int getcharRS232( void){
	int data;
    // wait for Data Ready bit (0) of line status register to be '1'
    while(!RS232_TestForReceivedData()){}
    // read new character from ReceiverFiFo register
    data = RS232_ReceiverFifo;
    // return new character
    return data;
}

//
// Remove/flush the UART receiver buffer by removing any unread characters
//
void RS232_Flush(void){
    int unreadChar;
    // while bit 0 of Line Status Register == "1"
    while(RS232_TestForReceivedData()){
        // read unwanted char out of fifo receiver buffer
        unreadChar = RS232_ReceiverFifo;
    }

    return;
    // no more characters so return
}



