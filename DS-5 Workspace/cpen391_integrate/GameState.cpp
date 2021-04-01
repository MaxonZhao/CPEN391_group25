/*
 * GameState.cpp
 *
 *  Created on: Mar 24, 2021
 *      Author: patri
 */

#include "GameState.h"
#include <iostream>
#include "./RS232/serial.h"

namespace GameLogic {
	GameState::GameState(StateMachine* data) {
		this->_data = data;
	}

	void GameState::Init() {
		// TODO: need to initiate a pipe class later:
		this->_pipe = new Pipe();

		this->_bird = new Bird();

		//this->_gameState = Ready;
		this->_gameState = Playing;

		this->_clock = clock();
	}

	void GameState::HandleInput() {
		//check if bluetooth's input buffer has data, if yes, let the bird jump
		int bytes_received = 0;
		char buffer[8];
		bytes_received = getSignal(buffer, BT_LineStatusReg, BT_ReceiverFifo);

//		std::cout << "received:" << buffer<<std::endl;;

		if(bytes_received != 0){
			this->_bird->Tap();
		}
//		if(*PUSHBUTTONS == 14){
//			this->_bird->Tap();
//		}
	}

	void GameState::Update(float dt) {
		// update the location of textures:
		if (this->_gameState == Playing) {
			this->_pipe->MovePipes(dt);

			if (this->_clock + PIPE_SPAWN_FREQUENCY < clock()) {
				// generate pipe:
				this->_pipe->RandomizedPipeOffset();
				this->_pipe->SpawnPipe();

				this->_clock = clock();
			}

			this->_bird->Update(dt);
		}



	}

	void GameState::Draw(float dt) {
		// draw the pipe here:
		 this->ReDrawBackground();
		 _pipe->DrawPipes();
		 _bird->Draw();
	}

	void GameState::ReDrawBackground(){
		*(RENDER_BASE + 4) = 0x6A;
		*(RENDER_BASE + 6) = 0x4F;
	}
}
