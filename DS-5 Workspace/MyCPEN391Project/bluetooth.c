/*
 * bluetooth.c
 *
 *  Created on: Feb 28, 2021
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

void Init_BT(void){
    // set bit 7 of Line Control Register to 1, to gain access to the baud rate registers
    BT_LineControlReg = BT_LineControlReg | 0x80;
    // set Divisor latch (LSB and MSB) with correct value for required baud rate
    //baud rate divisor value=freq of BR_clk/ (desired buad rate*16)
    //here buad rate = 115200
    BT_DivisorLatchLSB = 0x1B;
    BT_DivisorLatchMSB = 0x00;
    // set bit 7 of Line control register back to 0 and
    BT_LineControlReg = BT_LineControlReg & 0x7F;
    // program other bits in that reg for 8 bit data, 1 stop bit, no parity etc
    //bit 1-0 = 11, bit 2 =0, bit 3 = 0
    BT_LineControlReg = 0x03;
    // Reset the Fifo in the FiFo Control Reg by setting bits 1 & 2 to 1
    BT_FifoControlReg = BT_FifoControlReg | 0x06;
    // Now Clear all bits in the FiFo control registers
    BT_FifoControlReg = 0x00;

}

// the following function polls the UART to determine if any character
// has been received. It doesn't wait for one,or read it, it simply tests
// to see if one is available to read from the FIFO

int BT_TestForReceivedData(void){
    // if BT_LineStatusReg bit 0 is set to 1
    //return TRUE, otherwise return FALSE
    if((BT_LineStatusReg & 0x01)== 0x01){
        return 1;
    }
    else return 0;

}

int putcharBT(int c){
    // wait for Transmitter Holding Register bit (5) of line status register to be '1'
    // indicating we can write to the device
    while((BT_LineStatusReg  & 0x20)!= 0x20);

    // write character to Transmitter fifo register
    BT_TransmitterFifo = c;
    // return the character we printed
    return c;
}

int getcharBT( void){
	int data;
    // wait for Data Ready bit (0) of line status register to be '1'
    while(!BT_TestForReceivedData()){}
    // read new character from ReceiverFiFo register
    data = BT_ReceiverFifo;
    // return new character
    return data;
}

//
// Remove/flush the UART receiver buffer by removing any unread characters
//
void BT_Flush(void){
    int unreadChar;
    // while bit 0 of Line Status Register == "1"
    while(BT_TestForReceivedData()){
        // read unwanted char out of fifo receiver buffer
        unreadChar = BT_ReceiverFifo;
    }

    return;
    // no more characters so return
}


void BTOutMessage(char* message){
	for(int i = 0; i < strlen(message); i ++){
		int c = putcharBT((int)message[i]);
	}
	putcharBT('\r');
	putcharBT('\n');
}

//
//int main(void){
//    //testing
//	int i;
//	int u;
//
//    Init_BT();
//    BT_Flush();
//
//    printf("Bluetooth initialized!\n");
//
//	int data = 0x31;
//	putcharBT(data);
//	for(i=0; i<1000; i++){
//		u++;
//	}
//	int data_r = getcharBT();
//	printf("receive: %d, %c",data_r,data_r);
//
//    return 0;
//}

// void bluetoothListen() {
// 	while (1){
// 		int data = getcharBT();
// 		printf("%d",data);
// 	}
// }

