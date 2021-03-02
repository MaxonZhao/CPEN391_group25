/*
 * wifi.c
 *
 *  Created on: Mar 1, 2021
 *      Author: zoeyli
 */

#include "serial.h"

void Init_WIFI(void){
    // set bit 7 of Line Control Register to 1, to gain access to the baud rate registers
    WIFI_LineControlReg = WIFI_LineControlReg | 0x80;
    // set Divisor latch (LSB and MSB) with correct value for required baud rate
    //baud rate divisor value=freq of BR_clk/ (desired buad rate*16)
    //here buad rate = 115200
    WIFI_DivisorLatchLSB = 0x1B;
    WIFI_DivisorLatchMSB = 0x00;
    // set bit 7 of Line control register back to 0 and
    WIFI_LineControlReg = WIFI_LineControlReg & 0x7F;
    // program other bits in that reg for 8 bit data, 1 stop bit, no parity etc
    //bit 1-0 = 11, bit 2 =0, bit 3 = 0
    WIFI_LineControlReg = 0x03;
    // Reset the Fifo in the FiFo Control Reg by setting bits 1 & 2 to 1
    WIFI_FifoControlReg = WIFI_FifoControlReg | 0x06;
    // Now Clear all bits in the FiFo control registers
    WIFI_FifoControlReg = 0x00;

}

// the following function polls the UART to determine if any character
// has been received. It doesn't wait for one,or read it, it simply tests
// to see if one is available to read from the FIFO

int WIFI_TestForReceivedData(void){
    // if WIFI_LineStatusReg bit 0 is set to 1
    //return TRUE, otherwise return FALSE
    if((WIFI_LineStatusReg & 0x01)== 0x01){
        return 1;
    }
    else return 0;

}

int putcharWIFI(int c){
    // wait for Transmitter Holding Register bit (5) of line status register to be '1'
    // indicating we can write to the device
    while((WIFI_LineStatusReg  & 0x20)!= 0x20);

    // write character to Transmitter fifo register
    WIFI_TransmitterFifo = c;
    // return the character we printed
    return c;
}

int getcharWIFI( void){
	int data;
    // wait for Data Ready bit (0) of line status register to be '1'
    while(!WIFI_TestForReceivedData()){}
    // read new character from ReceiverFiFo register
    data = WIFI_ReceiverFifo;
    // return new character
    return data;
}

//
// Remove/flush the UART receiver buffer by removing any unread characters
//
void WIFI_Flush(void){
    int unreadChar;
    // while bit 0 of Line Status Register == "1"
    while(WIFI_TestForReceivedData()){
        // read unwanted char out of fifo receiver buffer
        unreadChar = WIFI_ReceiverFifo;
    }

    return;
    // no more characters so return
}


void WIFIOutMessage(char* message){
	for(int i = 0; i < strlen(message); i ++){
		int c = putcharWIFI((int)message[i]);
	}
	putcharWIFI('\r');
	putcharWIFI('\n');
}
