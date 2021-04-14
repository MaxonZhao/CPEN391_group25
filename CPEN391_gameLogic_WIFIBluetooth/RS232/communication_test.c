/*
 * communication_test.c
 *
 *  Created on: Mar 1, 2021
 *      Author: Zhuoyi Li
 */
#include <stdio.h>
#include <string.h>
#include "serial.h"
#include "wifi.h"

/**
* This file is a testbench used for testing if the WiFi serial communication and 
* the Bluetooth serial Communication is working before integrate with game logic codes.
*/

/**
* Test if we can successfully send a message from the RFS board to an blueTooth device(e.g. an Android mobile phone)
* and if we can successfully read a message from an blueTooth device.
* The test can be done in 2 ways:
*	with a physical Android phone through a Bluetooth serial terminal app avaliable in 
* 	Google Play Store(used in the early developement stage). Tests can also be done with the actual Andriod App
* 	we develop in the later integration stage.
*	with Python codes(in BlueTooth_hw_test folder) that simulates the player generates the signal in a random time interval to let the bird jump. 
*/
int testBTCommunication(){
 	int count = 0;
     Init_RS232(BT_LineControlReg, BT_DivisorLatchLSB, BT_DivisorLatchMSB, BT_FifoControlReg);
     RS232_Flush(BT_ReceiverFifo, BT_LineStatusReg);
	  Init_RS232(BT2_LineControlReg, BT2_DivisorLatchLSB, BT2_DivisorLatchMSB, BT2_FifoControlReg);
	  RS232_Flush(BT2_ReceiverFifo, BT2_LineStatusReg);

     printf("Bluetooth initialized\n");

    sendMessage("Hello from arm to device 1",BT_LineStatusReg, BT_TransmitterFifo);
//      sendMessage("Hello from arm to device 2",BT2_LineStatusReg, BT2_TransmitterFifo);

 	for(int i=0;i<1;i++){
 		count++;
		char data_r1 = getcharRS232(BT_ReceiverFifo,BT_LineStatusReg);
		printf("%d receive from device 1 in ascii:(hex) %X,(char) %c\n",count, data_r1,data_r1);
//  		char data_r2 = getcharRS232(BT2_ReceiverFifo,BT2_LineStatusReg);
//  		printf("%d receive from device 1 in ascii:(hex) %X,(char) %c\n",count, data_r2,data_r2);
 	}
 	 sendMessage("44",BT2_LineStatusReg, BT2_TransmitterFifo);

 }

/**
* Test if we can successfully invoke the lua script inside the RFS board's Wifi Chip to update the current score for user "test" to 44.
*/
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
/**
* Main function to run tests.
*/
int main(void){

	testBTCommunication();

	int err;
	int errorCount = 0;
	err = testUploadScore();
	if (err){
		errorCount++;
	}
	if(errorCount == 0){
			printf("All tests for wifi are passed!\n");
	}
}
