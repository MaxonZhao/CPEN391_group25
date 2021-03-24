/*
 * wifi.h
 *
 *  Created on: Mar 16, 2021
 *      Author: zoeyli
 */

#ifndef WIFI_H_
#define WIFI_H_

int runLUAScript(char * message, char * res, volatile unsigned char* LineStatusReg, volatile unsigned char* TransmitterFifo, volatile unsigned char* ReceiverFifo);

#endif /* WIFI_H_ */
