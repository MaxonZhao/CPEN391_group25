/*
 * main.c
 *
 *  Created on: Mar 1, 2021
 *      Author: zoeyli
 */
#include <stdio.h>
#include <string.h>
#include "serial.h"
int main(void){
	int count = 0;
    Init_RS232(BT_LineControlReg, BT_DivisorLatchLSB, BT_DivisorLatchMSB, BT_FifoControlReg);
    RS232_Flush(BT_ReceiverFifo, BT_LineStatusReg);
    Init_RS232(BT2_LineControlReg, BT2_DivisorLatchLSB, BT2_DivisorLatchMSB, BT2_FifoControlReg);
    RS232_Flush(BT2_ReceiverFifo, BT2_LineStatusReg);

    printf("Bluetooth initialized\n");

    sendMessage("Hello from arm processor to device 1",BT_LineStatusReg, BT_TransmitterFifo);
    sendMessage("Hello from arm processor to device 2",BT2_LineStatusReg, BT2_TransmitterFifo);

	while(1){
		count++;
		char data_r1 = getcharRS232(BT_ReceiverFifo,BT_LineStatusReg);
		printf("%d receive from device 1 in ascii:(hex) %X,(char) %c\n",count, data_r1,data_r1);
		char data_r2 = getcharRS232(BT2_ReceiverFifo,BT2_LineStatusReg);
		printf("%d receive from device 2 in ascii:(hex) %X,(char) %c\n",count, data_r2,data_r2);

	}


}
