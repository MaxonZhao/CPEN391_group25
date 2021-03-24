/*
 * main.c
 *
 *  Created on: Mar 1, 2021
 *      Author: zoeyli
 */
#include <stdio.h>
#include <string.h>
#include "serial.h"
#include "wifi.h"
//int main(void){
//	int count = 0;
//    Init_RS232(BT_LineControlReg, BT_DivisorLatchLSB, BT_DivisorLatchMSB, BT_FifoControlReg);
//    RS232_Flush(BT_ReceiverFifo, BT_LineStatusReg);
//    Init_RS232(BT2_LineControlReg, BT2_DivisorLatchLSB, BT2_DivisorLatchMSB, BT2_FifoControlReg);
//    RS232_Flush(BT2_ReceiverFifo, BT2_LineStatusReg);
//
//    printf("Bluetooth initialized\n");
//
//    sendMessage("Hello from arm to device 1",BT_LineStatusReg, BT_TransmitterFifo);
//    sendMessage("Hello from arm to device 2",BT2_LineStatusReg, BT2_TransmitterFifo);
//
//	while(1){
//		count++;
//		char data_r1 = getcharRS232(BT_ReceiverFifo,BT_LineStatusReg);
//		printf("%d receive from device 1 in ascii:(hex) %X,(char) %c\n",count, data_r1,data_r1);
//		char data_r2 = getcharRS232(BT2_ReceiverFifo,BT2_LineStatusReg);
//		printf("%d receive from device 2 in ascii:(hex) %X,(char) %c\n",count, data_r2,data_r2);
//
//	}
//
//
//}

int main(void){


	Init_RS232(WIFI_LineControlReg, WIFI_DivisorLatchLSB, WIFI_DivisorLatchMSB, WIFI_FifoControlReg);

	Init_RS232(BT_LineControlReg, BT_DivisorLatchLSB, BT_DivisorLatchMSB, BT_FifoControlReg);
	RS232_Flush(BT_ReceiverFifo, BT_LineStatusReg);
	Init_RS232(BT2_LineControlReg, BT2_DivisorLatchLSB, BT2_DivisorLatchMSB, BT2_FifoControlReg);
	RS232_Flush(BT2_ReceiverFifo, BT2_LineStatusReg);

	printf("BlueTooth & WIFI are initialized\n");

	sendMessage("Hello from arm to device 1",BT_LineStatusReg, BT_TransmitterFifo);
	sendMessage("Hello from arm to device 2",BT2_LineStatusReg, BT2_TransmitterFifo);


	sendMultiChar( "\r\n",WIFI_LineStatusReg, WIFI_TransmitterFifo);
	RS232_Flush(WIFI_ReceiverFifo, WIFI_LineStatusReg);
	sendMultiChar( "\r\n",WIFI_LineStatusReg, WIFI_TransmitterFifo);
	RS232_Flush(WIFI_ReceiverFifo, WIFI_LineStatusReg);
	sendMultiChar( "\r\n",WIFI_LineStatusReg, WIFI_TransmitterFifo);
	RS232_Flush(WIFI_ReceiverFifo, WIFI_LineStatusReg);

	printf("Starting send command to wifi dongle\n");

	sendMessage("uploadScore(\"test\",60)",WIFI_LineStatusReg, WIFI_TransmitterFifo);
    RS232_Flush(WIFI_ReceiverFifo, WIFI_LineStatusReg);


}
