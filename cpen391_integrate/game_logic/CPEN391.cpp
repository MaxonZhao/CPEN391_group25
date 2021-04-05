#include "Game.h"

using namespace GameLogic;

#include <iostream>
#include "DEFINITION.h"
#include "../RS232/serial.h"
int main() {
	//initialzied RFS board's BlueTooth and Wifi modules 
	std::cout<<"Initializing BlueTooth module!"<<std::endl;
	Init_RS232(BT_LineControlReg, BT_DivisorLatchLSB, BT_DivisorLatchMSB, BT_FifoControlReg);
	RS232_Flush(BT_ReceiverFifo, BT_LineStatusReg);
	Init_RS232(WIFI_LineControlReg, WIFI_DivisorLatchLSB, WIFI_DivisorLatchMSB, WIFI_FifoControlReg);

	std::cout<<"Begin the Game!!"<<std::endl;
	Game game = Game();

}
