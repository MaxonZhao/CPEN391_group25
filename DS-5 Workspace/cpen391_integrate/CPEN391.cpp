#include "Game.h"

using namespace GameLogic;

#include <iostream>
#include "DEFINITION.h"
#include "./RS232/serial.h"
int main() {


	Init_RS232(BT_LineControlReg, BT_DivisorLatchLSB, BT_DivisorLatchMSB, BT_FifoControlReg);
	RS232_Flush(BT_ReceiverFifo, BT_LineStatusReg);
	std::cout<<"BlueTooth module Initialized!"<<std::endl;
	std::cout<<"Begin the Game!!"<<std::endl;
	Game game = Game();

	// 100 clock = 1 second

//	clock_t current = clock();
//
//	while(clock()-current < 500){
//		// Plot pipe
//		*(RENDER_BASE + 4) = 0x06; // Set texture code to pipe up
//		*(RENDER_BASE + 1) = 159;  // Set x-coor to center of screen
//		*(RENDER_BASE + 2) = 119;  // Set y-coor to center of screen
//		*(RENDER_BASE + 6) = 0x05;    // Plot to buffer
//	}
//
//	*(RENDER_BASE + 4) = 0x6A;
//	*(RENDER_BASE + 6) = 0x4F;

}
