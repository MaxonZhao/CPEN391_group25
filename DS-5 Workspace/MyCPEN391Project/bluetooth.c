///*
// * bluetooth.c
// *
// *  Created on: Feb 28, 2021
// *      Author: zoeyli
// */
//#include <time.h>
//#include <stdio.h>
//#include <string.h>
//#include "serial.h"
////NOT WORKING, TRY TO SET UP WITH DIRECTLY
//
//void BT_setName(char *name, volatile unsigned char* LineStatusReg, volatile unsigned char* TransmitterFifo) {
//	for(int i=0;i<200000;i++){};
//
//	sendMultiChar("$$$",LineStatusReg, TransmitterFifo);
//	//sleep(HALF_SEC);
//	for(int i=0;i<200000;i++){};
//
//	// send the command to change the name
//	char command[25] = "SN,";
//	strcat(command, name);
//	strcat(command, "\r\n");
//
//	sendMultiChar(command,LineStatusReg, TransmitterFifo);
//
//	for(int i=0;i<200000;i++){};
//	sendMultiChar("---",LineStatusReg, TransmitterFifo);
//	//sleep(HALF_SEC);
//	for(int i=0;i<200000;i++){};
//}
//
//void BT_setPassword(char *password,  volatile unsigned char* LineStatusReg, volatile unsigned char* TransmitterFifo) {
//
//	//sleep(HALF_SEC);
//	sendMultiChar("$$$",LineStatusReg, TransmitterFifo);
//	//sleep(HALF_SEC);
//
//
//	// send the command to change the password
//	char command[25] = "SP,";
//	strcat(command, password);
//	strcat(command, "\r\n");
//
//	sendMultiChar(command,LineStatusReg, TransmitterFifo);
//
//	for(int i=0;i<200000;i++){};
//	sendMultiChar("---",LineStatusReg, TransmitterFifo);
//	//sleep(HALF_SEC);
//	for(int i=0;i<200000;i++){};
//
//}
//
