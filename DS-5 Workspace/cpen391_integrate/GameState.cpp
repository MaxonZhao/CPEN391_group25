/*
 * GameState.cpp
 *
 *  Created on: Mar 24, 2021
 *      Author: patri
 */

#include "GameState.h"
#include <iostream>

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
		// do this later:
		if(*PUSHBUTTONS == 14){
			this->_bird->Tap();
		}
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
