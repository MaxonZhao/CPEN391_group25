/*
 * serial.h
 *
 *  Created on: Mar 1, 2021
 *      Author: Zhuoyi Li
 */

#ifndef SERIAL_H_
#define SERIAL_H_


/**
* The CPU can control the Wifi and Bluetooth modules on the RFS board by writing to or reading from
* certain memory location addresses using pointers in C/C++ code. These addresses are matched to the address range defined in serialIODecoder.v
* 	BlueTooth serial port address range: 0xFF21_0230 - 0xFF21_023F;
*	WiFi serial port address range: 0xFF21_0210 - 0xFF21_021F
*/
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

#define BT_ReceiverFifo       ((volatile unsigned char *)(0xFF210230))
#define BT_TransmitterFifo    ((volatile unsigned char *)(0xFF210230))
#define BT_InterruptEnableReg ((volatile unsigned char *)(0xFF210232))
#define BT_InterruptIdentificationReg        ((volatile unsigned char *)(0xFF210234))
#define BT_FifoControlReg                    ((volatile unsigned char *)(0xFF210234))
#define BT_LineControlReg                    ((volatile unsigned char *)(0xFF210236))
#define BT_ModemControlReg                   ((volatile unsigned char *)(0xFF210238))
#define BT_LineStatusReg                     ((volatile unsigned char *)(0xFF21023A))
#define BT_ModemStatusReg                    ((volatile unsigned char *)(0xFF21023C))
#define BT_ScratchReg                        ((volatile unsigned char *)(0xFF21023E))
#define BT_DivisorLatchLSB                   ((volatile unsigned char *)(0xFF210230))
#define BT_DivisorLatchMSB                   ((volatile unsigned char *)(0xFF210232))

//Below are the address range for a rs232 serial port and a 2nd bluetooth serial port that are implemented in quartus but not used in the final project
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

#define BT2_ReceiverFifo       ((volatile unsigned char *)(0xFF210220))
#define BT2_TransmitterFifo    ((volatile unsigned char *)(0xFF210220))
#define BT2_InterruptEnableReg ((volatile unsigned char *)(0xFF210222))
#define BT2_InterruptIdentificationReg        ((volatile unsigned char *)(0xFF210224))
#define BT2_FifoControlReg                    ((volatile unsigned char *)(0xFF210224))
#define BT2_LineControlReg                    ((volatile unsigned char *)(0xFF210226))
#define BT2_ModemControlReg                   ((volatile unsigned char *)(0xFF210228))
#define BT2_LineStatusReg                     ((volatile unsigned char *)(0xFF21022A))
#define BT2_ModemStatusReg                    ((volatile unsigned char *)(0xFF21022C))
#define BT2_ScratchReg                        ((volatile unsigned char *)(0xFF21022E))
#define BT2_DivisorLatchLSB                   ((volatile unsigned char *)(0xFF210220))
#define BT2_DivisorLatchMSB                   ((volatile unsigned char *)(0xFF210222))



//Function prototypes
void Init_RS232(volatile unsigned char* , volatile unsigned char*,
				volatile unsigned char* , volatile unsigned char* );
int RS232_TestForReceivedData(volatile unsigned char* );
int putcharRS232(int, volatile unsigned char*,  volatile unsigned char* );
int getcharRS232(volatile unsigned char* , volatile unsigned char* );
void RS232_Flush(volatile unsigned char* , volatile unsigned char* );
void sendMessage(char* ,volatile unsigned char* ,volatile unsigned char* );
void sendMultiChar(char* ,volatile unsigned char* ,volatile unsigned char* );
int getSignal(char * res, volatile unsigned char* LineStatusReg, volatile unsigned char* ReceiverFifo);
int receiveBuffer(char * res, volatile unsigned char* LineStatusReg, volatile unsigned char* ReceiverFifo);
#endif /* SERIAL_H_ */


