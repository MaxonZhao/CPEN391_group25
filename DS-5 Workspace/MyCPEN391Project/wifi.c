#include "serial.h"
#include <stdio.h>
#include <string.h>

//not working yet.
#define DEST_SIZE 40
void uploadScore(char* userName, int score){
	RS232_Flush(WIFI_ReceiverFifo, WIFI_LineStatusReg);

    //send CRLF several times to let the wifi firmware match up the communication baud rate
	sendMultiChar( "\r\n",WIFI_LineStatusReg, WIFI_TransmitterFifo);
	RS232_Flush(WIFI_ReceiverFifo, WIFI_LineStatusReg);
	sendMultiChar( "\r\n",WIFI_LineStatusReg, WIFI_TransmitterFifo);
	RS232_Flush(WIFI_ReceiverFifo, WIFI_LineStatusReg);
	sendMultiChar( "\r\n",WIFI_LineStatusReg, WIFI_TransmitterFifo);
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

	printf("Starting send command to wifi dongle\n");

    sendMessage(command,WIFI_LineStatusReg, WIFI_TransmitterFifo);
	// sendMessage("uploadScore(\"test\",60)",WIFI_LineStatusReg, WIFI_TransmitterFifo);
    RS232_Flush(WIFI_ReceiverFifo, WIFI_LineStatusReg);

}
