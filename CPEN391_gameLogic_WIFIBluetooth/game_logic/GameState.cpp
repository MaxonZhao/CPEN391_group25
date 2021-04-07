/*
 * GameState.cpp
 *
 *  Created on: Mar 24, 2021
 *      Author: patri
 */

#include "GameState.h"
#include "../RS232/serial.h"
#include <vector>
#include <iostream>

namespace GameLogic {
	GameState::GameState(StateMachine* data) {
		this->_data = data;
	}

	void GameState::Init() {
		this->_pipe = new Pipe();

		this->_bird = new Bird();

		this->isPlaying = true;

		this->_clock = 0;

		this->_score = 0;

		this->bytes_received = 0;
	}

	void GameState::HandleInput() {
//		check if bluetooth's input buffer has data, if yes, let the bird jump
		bytes_received = getSignal(buffer, BT_LineStatusReg, BT_ReceiverFifo);

		if(bytes_received != 0){
			this->_bird->Tap();
		}

///////////////////////////////////////////////////////////////////////////////////////////////////////
//		 if(*PUSHBUTTONS == 14){
//			this->_bird->Tap();
//		 }
	}

	void GameState::Update(float dt) {
		if (this->isPlaying) {

			this->_score += this->_pipe->MovePipes(dt);

			if (this->_clock + this->_data->pipe_spawn_frequency < clock()) {
				// generate pipe:
				this->_pipe->RandomizedPipeOffset();
				this->_pipe->SpawnPipe();

				this->_clock = clock();
			}

			this->_bird->Update(dt);

			// collision detection:
			if(this->_bird->birdYPosition <= 12 || this->_bird->birdYPosition >= 227 ||
					 this->_pipe->CheckCollision(this->_bird->birdYPosition)){
				this->isPlaying = false;
				this->_data->score = this->_score;
				this->_waitscreenClock = clock();
			}
		}else{
			if(this->_waitscreenClock + WAITUNTILGAMEOVER < clock()){
				_data->AddState(new GameOverState(_data));
			}
		}


	}

	void GameState::Draw() {
		if(!this->isPlaying) return;
		// draw the pipe here:
		 this->ReDrawBackground();
		 _pipe->DrawPipes();
		 _bird->Draw();

		 this->DrawScore();

	}

	void GameState::ReDrawBackground(){
		*(RENDER_BASE + 4) = 0x6A;
		*(RENDER_BASE + 6) = 0x4F;
	}

	void GameState::DrawScore(){
		int score = this->_score;
		int xPosition = 300;
		do{
			plot(score%10, xPosition);
			xPosition -= WIDTH_DIGIT;
			score /= 10;
		}while(score != 0);

	}

	void GameState::plot(int digit, int xPosition){
		*(RENDER_BASE + 4) = 7+digit;
		*(RENDER_BASE + 1) = xPosition;
		*(RENDER_BASE + 2) = 10;
		*(RENDER_BASE + 6) = 0x05;
	}
}
