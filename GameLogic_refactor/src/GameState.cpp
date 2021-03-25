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

		this->_gameState = Ready;

		this->_clock = clock();
	}

	void GameState::HandleInput() {
		// do this later:
	}

	void GameState::Update(float dt) {
		std::cout<<"Updating GameState!!"<<std::endl;
		// update the location of textures:
		if (this->_gameState == Playing) {
			this->_pipe->MovePipes(dt);

			if (this->_clock + dt < clock()) {
				// generate pipe:
				this->_pipe->RandomizedPipeOffset();
				this->_pipe->SpawnPipe();

				this->_clock = clock();
			}
		}



	}

	void GameState::Draw(float dt) {
		// draw the pipe here:
		_pipe->DrawPipes();
	}
}
