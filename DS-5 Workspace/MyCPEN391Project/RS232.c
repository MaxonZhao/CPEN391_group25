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
**  Subroutine to initialize the RS232 Port by writing some data
**  to the internal registers.
**  Call this function at the start of the program before you attempt
**  to read or write to data via the RS232 port
**
**  Refer to UARTdata sheet for details of registers and programming
***************************************************************************/

void Init_RS232(volatile unsigned char* LineControlReg, volatile unsigned char* DivisorLatchLSB,
		volatile unsigned char* DivisorLatchMSB, volatile unsigned char* FifoControlReg){
    // set bit 7 of Line Control Register to 1, to gain access to the baud rate registers
	*LineControlReg = 0x80;

    // set Divisor latch (LSB and MSB) with correct value for required baud rate
    //baud rate divisor value=freq of BR_clk/ (desired buad rate*16)
    //here buad rate = 57600 (0x1B and 0x00 for 115200)

	*DivisorLatchLSB = 0x36;
	*DivisorLatchMSB = 0x00;

    // set bit 7 of Line control register back to 0 and
    // program other bits in that reg for 8 bit data, 1 stop bit, no parity etc
    //bit 1-0 = 11, bit 2 =0, bit 3 = 0
    *LineControlReg = 0x03;

      // Reset the Fifo in the FiFo Control Reg by setting bits 1 & 2 to 1
    *FifoControlReg = 0x07;

    // // Now Clear all bits in the FiFo control registers
    *FifoControlReg = 0x01;

}

// the following function polls the UART to determine if any character
// has been received. It doesn't wait for one,or read it, it simply tests
// to see if one is available to read from the FIFO
int RS232_TestForReceivedData(volatile unsigned char* LineStatusReg){
    // if BT_LineStatusReg bit 0 is set to 1
    //return TRUE, otherwise return FALSE
    //printf("BT_LineStatusReg:%X\n",BT_LineStatusReg);
    if((*LineStatusReg & 0x01)== 0x01){
        return 1;
    }
    else return 0;

}

int putcharRS232(int c, volatile unsigned char* LineStatusReg,  volatile unsigned char* TransmitterFifo){
    // wait for Transmitter Holding Register bit (5) of line status register to be '1'
    // indicating we can write to the device
    while((*LineStatusReg & 0x20)!= 0x20);

    // write character to Transmitter fifo register
    *TransmitterFifo = c;
    // return the character we printed
    return c;
}

int getcharRS232(volatile unsigned char* ReceiverFifo, volatile unsigned char* LineStatusReg){
	int data;
    // wait for Data Ready bit (0) of line status register to be '1'
    while(!RS232_TestForReceivedData(LineStatusReg)){}
    // read new character from ReceiverFiFo register
    data = *ReceiverFifo;
    // return new character
    return data;
}

//
// Remove/flush the UART receiver buffer by removing any unread characters
//
void RS232_Flush(volatile unsigned char* ReceiverFifo, volatile unsigned char* LineStatusReg){
    int unreadChar;
    // while bit 0 of Line Status Register == "1"
    while(RS232_TestForReceivedData(LineStatusReg)){
        // read unwanted char out of fifo receiver buffer
        unreadChar = *ReceiverFifo;
    }

    return;
    // no more characters so return
}


void sendMessage(char* message, volatile unsigned char* LineStatusReg, volatile unsigned char* TransmitterFifo){
	sendMultiChar(message,LineStatusReg,TransmitterFifo);

	putcharRS232('\r', LineStatusReg,TransmitterFifo);
	putcharRS232('\n', LineStatusReg,TransmitterFifo);
}

void sendMultiChar(char* message, volatile unsigned char* LineStatusReg, volatile unsigned char* TransmitterFifo){
	printf("send:%s\n",message);
	for(int i = 0; i < strlen(message); i ++){
		int c = putcharRS232((int)message[i], LineStatusReg,TransmitterFifo );
	}

}
