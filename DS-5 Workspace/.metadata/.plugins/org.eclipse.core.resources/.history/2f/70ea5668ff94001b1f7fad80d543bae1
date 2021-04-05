/*
 * main.c
 *
 *  Created on: Mar 1, 2021
 *      Author: Zhuoyi Li
 */
#include <stdio.h>
#include <string.h>
#include "serial.h"
#include "wifi.h"

int testBTCommunication(){
 	int count = 0;
     Init_RS232(BT_LineControlReg, BT_DivisorLatchLSB, BT_DivisorLatchMSB, BT_FifoControlReg);
     RS232_Flush(BT_ReceiverFifo, BT_LineStatusReg);
	  Init_RS232(BT2_LineControlReg, BT2_DivisorLatchLSB, BT2_DivisorLatchMSB, BT2_FifoControlReg);
	  RS232_Flush(BT2_ReceiverFifo, BT2_LineStatusReg);

     printf("Bluetooth initialized\n");

     sendMessage("Hello from arm to device 1",BT_LineStatusReg, BT_TransmitterFifo);
     sendMessage("Hello from arm to device 2",BT2_LineStatusReg, BT2_TransmitterFifo);

 	for(int i=0;i<1;i++){
 		count++;
 		char data_r1 = getcharRS232(BT_ReceiverFifo,BT_LineStatusReg);
 		printf("%d receive from device 1 in ascii:(hex) %X,(char) %c\n",count, data_r1,data_r1);
 		char data_r2 = getcharRS232(BT2_ReceiverFifo,BT2_LineStatusReg);
 		printf("%d receive from device 1 in ascii:(hex) %X,(char) %c\n",count, data_r2,data_r2);
 	}
 }

int testUploadScore(){

	Init_RS232(WIFI_LineControlReg, WIFI_DivisorLatchLSB, WIFI_DivisorLatchMSB, WIFI_FifoControlReg);


	int err = uploadScore("test", 44);
	if(err){
		printf("Error occurs to send a PATCH request\n");
		return 1;
	}else{
		printf("Successfully send a PATCH request to upload player's score\n");
		return 0;
	}

}
int main(void){
	int err;
	int errorCount = 0;
	err = testUploadScore();
	if (err){
		errorCount++;
	}
	if(errorCount == 0){
			printf("All tests for wifi are passed!\n");
	}
	testBTCommunication();

}
