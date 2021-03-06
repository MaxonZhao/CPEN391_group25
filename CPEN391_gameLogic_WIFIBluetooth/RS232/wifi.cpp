/*
 * wifi.c
 *
 *  Created on: Mar 1, 2021
 *      Author: Zhuoyi Li
 */
#include "serial.h"
#include "wifi.h"
#include <iostream>
#include <cstring>
using namespace std;

#define DEST_SIZE 40

/**
 * Send a HTTP PATCH request to the cloud server through sending commands to wifi dongle
 * to run the lua scripts inside the dongle.
 * Return 0 if request is successful, 1 if error occurs.
 */
int uploadScore(char* userName, int score){
	int bytes_received = 0;
	char buffer[6];

	RS232_Flush(WIFI_ReceiverFifo, WIFI_LineStatusReg);

    //send CRLF several times to let the wifi firmware match up the communication baud rate
	sendMultiChar( "\r\n",WIFI_LineStatusReg, WIFI_TransmitterFifo);
	RS232_Flush(WIFI_ReceiverFifo, WIFI_LineStatusReg);
	sendMultiChar( "\r\n",WIFI_LineStatusReg, WIFI_TransmitterFifo);
	RS232_Flush(WIFI_ReceiverFifo, WIFI_LineStatusReg);
	sendMultiChar( "\r\n",WIFI_LineStatusReg, WIFI_TransmitterFifo);
	RS232_Flush(WIFI_ReceiverFifo, WIFI_LineStatusReg);

	printf("Starting send command to wifi dongle\n");

	//send command to let the wifi dongle run the file containing uploadScore() function
//	sendMessage("dofile(\"init.lua\")",WIFI_LineStatusReg, WIFI_TransmitterFifo);
//	RS232_Flush(WIFI_ReceiverFifo, WIFI_LineStatusReg);
//	for(int i=0;i<7000000;i++){};

	sendMessage("dofile(\"uploadScore.lua\")",WIFI_LineStatusReg, WIFI_TransmitterFifo);
	RS232_Flush(WIFI_ReceiverFifo, WIFI_LineStatusReg);

    //command concatenation
    char command[DEST_SIZE] = "uploadScore(\"";
    char score_s[8];
	strcat(command, userName);
	strcat(command,"\",");
    snprintf(score_s, 8, "%d", score); // covert score from int to string 
    strcat(command,score_s);
    strcat(command,")");
    
	printf(command);

    sendMessage(command,WIFI_LineStatusReg, WIFI_TransmitterFifo);
    for(int i=0;i<7000000;i++){};

    RS232_Flush(WIFI_ReceiverFifo, WIFI_LineStatusReg);


	//check if we receive success status code 200. Return 1 if yes, 0 if PATCH request fail.
	bytes_received = checkReceivedStatus(buffer, WIFI_LineStatusReg, WIFI_ReceiverFifo);
	printf(buffer);
	
	if(strcmp(buffer,"200\r\n") == 0){
		return 0;
	}
	else return 1;
}

/**
 * Helper Function for uploadScore() to wait till receive something returned by the wifi dongle
 * Store received message on res and return number of bytes received
 */
int checkReceivedStatus(char * res, volatile unsigned char* LineStatusReg, volatile unsigned char* ReceiverFifo) {
	int j;
	int bytes_received = 0;

    while(!RS232_TestForReceivedData(LineStatusReg)){};

	for(j = 0; j < 5000; j++) {
		if(RS232_TestForReceivedData(LineStatusReg)) {
			res[bytes_received++] = (char) getcharRS232(ReceiverFifo,LineStatusReg);
			j = 0 ;
		}
	}

	printf("Received %d bytes\n", bytes_received);
	res[bytes_received] = '\0';
	return bytes_received;
}
