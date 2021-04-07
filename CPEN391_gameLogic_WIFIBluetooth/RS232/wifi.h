/*
 * wifi.h
 *
 *  Created on: Mar 1, 2021
 *      Author: Zhuoyi Li
 */

#ifndef WIFI_H_
#define WIFI_H_

int uploadScore(char* userName, int score);
int checkReceivedStatus(char * res, volatile unsigned char* LineStatusReg, volatile unsigned char* ReceiverFifo);
#endif /* WIFI_H_ */
