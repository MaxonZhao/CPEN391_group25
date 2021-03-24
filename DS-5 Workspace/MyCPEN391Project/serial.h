/*
 * serial.h
 *
 *  Created on: Mar 1, 2021
 *      Author: zoeyli
 */

#ifndef SERIAL_H_
#define SERIAL_H_

#define RS232_ReceiverFifo       ((volatile unsigned char *)(0xFF210200))
#define RS232_TransmitterFifo    ((volatile unsigned char *)(0xFF210200))
#define RS232_InterruptEnableReg ((volatile unsigned char *)(0xFF210202))
#define RS232_InterruptIdentificationReg        ((volatile unsigned char *)(0xFF210204))
#define RS232_FifoControlReg                    ((volatile unsigned char *)(0xFF210204))
#define RS232_LineControlReg                    ((volatile unsigned char *)(0xFF210206))
#define RS232_ModemControlReg                   ((volatile unsigned char *)(0xFF210208))
#define RS232_LineStatusReg                     ((volatile unsigned char *)(0xFF21020A))
#define RS232_ModemStatusReg                    ((volatile unsigned char *)(0xFF21020C))
#define RS232_ScratchReg                        ((volatile unsigned char *)(0xFF21020E))
#define RS232_DivisorLatchLSB                   ((volatile unsigned char *)(0xFF210200))
#define RS232_DivisorLatchMSB                   ((volatile unsigned char *)(0xFF210202))

#define WIFI_ReceiverFifo                  ((volatile unsigned char *)(0xFF210210))
#define WIFI_TransmitterFifo               ((volatile unsigned char *)(0xFF210210))
#define WIFI_InterruptEnableReg            ((volatile unsigned char *)(0xFF210212))
#define WIFI_InterruptIdentificationReg    ((volatile unsigned char *)(0xFF210214))
#define WIFI_FifoControlReg                ((volatile unsigned char *)(0xFF210214))
#define WIFI_LineControlReg                ((volatile unsigned char *)(0xFF210216))
#define WIFI_ModemControlReg               ((volatile unsigned char *)(0xFF210218))
#define WIFI_LineStatusReg                 ((volatile unsigned char *)(0xFF21021A))
#define WIFI_ModemStatusReg                ((volatile unsigned char *)(0xFF21021C))
#define WIFI_ScratchReg                    ((volatile unsigned char *)(0xFF21021E))
#define WIFI_DivisorLatchLSB               ((volatile unsigned char *)(0xFF210210))
#define WIFI_DivisorLatchMSB               ((volatile unsigned char *)(0xFF210212))

#define BT_ReceiverFifo       ((volatile unsigned char *)(0xFF210220))
#define BT_TransmitterFifo    ((volatile unsigned char *)(0xFF210220))
#define BT_InterruptEnableReg ((volatile unsigned char *)(0xFF210222))
#define BT_InterruptIdentificationReg        ((volatile unsigned char *)(0xFF210224))
#define BT_FifoControlReg                    ((volatile unsigned char *)(0xFF210224))
#define BT_LineControlReg                    ((volatile unsigned char *)(0xFF210226))
#define BT_ModemControlReg                   ((volatile unsigned char *)(0xFF210228))
#define BT_LineStatusReg                     ((volatile unsigned char *)(0xFF21022A))
#define BT_ModemStatusReg                    ((volatile unsigned char *)(0xFF21022C))
#define BT_ScratchReg                        ((volatile unsigned char *)(0xFF21022E))
#define BT_DivisorLatchLSB                   ((volatile unsigned char *)(0xFF210220))
#define BT_DivisorLatchMSB                   ((volatile unsigned char *)(0xFF210222))


#define BT2_ReceiverFifo       ((volatile unsigned char *)(0xFF210230))
#define BT2_TransmitterFifo    ((volatile unsigned char *)(0xFF210230))
#define BT2_InterruptEnableReg ((volatile unsigned char *)(0xFF210232))
#define BT2_InterruptIdentificationReg        ((volatile unsigned char *)(0xFF210234))
#define BT2_FifoControlReg                    ((volatile unsigned char *)(0xFF210234))
#define BT2_LineControlReg                    ((volatile unsigned char *)(0xFF210236))
#define BT2_ModemControlReg                   ((volatile unsigned char *)(0xFF210238))
#define BT2_LineStatusReg                     ((volatile unsigned char *)(0xFF21023A))
#define BT2_ModemStatusReg                    ((volatile unsigned char *)(0xFF21023C))
#define BT2_ScratchReg                        ((volatile unsigned char *)(0xFF21023E))
#define BT2_DivisorLatchLSB                   ((volatile unsigned char *)(0xFF210230))
#define BT2_DivisorLatchMSB                   ((volatile unsigned char *)(0xFF210232))


void Init_RS232(volatile unsigned char* , volatile unsigned char*,
				volatile unsigned char* , volatile unsigned char* );
int RS232_TestForReceivedData(volatile unsigned char* );
int putcharRS232(int, volatile unsigned char*,  volatile unsigned char* );
int getcharRS232(volatile unsigned char* , volatile unsigned char* );
void RS232_Flush(volatile unsigned char* , volatile unsigned char* );
void sendMessage(char* ,volatile unsigned char* ,volatile unsigned char* );
void sendMultiChar(char* ,volatile unsigned char* ,volatile unsigned char* );
int receiveBuffer(char* buffer,volatile unsigned char* ReceiverFifo, volatile unsigned char* LineStatusReg);
#endif /* SERIAL_H_ */