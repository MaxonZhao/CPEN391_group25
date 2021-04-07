/*
 * Game.cpp
 *
 *  Created on: Mar 24, 2021
 *      Author: patri
 */


#include "Game.h"
#include <iostream>
#include <stdlib.h>

namespace GameLogic {
	Game::Game() {
		std::srand(time(NULL));

		this->_data = new StateMachine();
		this->_data->AddState(new MainMenuState(this->_data));
		this->Run();
	}

	void Game::Run() {
		// the most important thing in the game logic:
		float currentTime = clock();
		float frameTime;

		while (true) {
			this->_data->ProcessStateChanges();
			frameTime = clock() - currentTime;

			if (frameTime > 0.25f) frameTime = 0.25f;

			currentTime = clock();

			this->_data->GetActiveState()->HandleInput();
			this->_data->GetActiveState()->Update(frameTime);


			///////////////////////////////////////////////////////////////////////////////////////////////////////////
			this->_data->GetActiveState()->HandleInput();

			this->_data->GetActiveState()->Draw();

		}

//		while (true) {
//			this->_data->ProcessStateChanges();
//			frameTime = clock() - currentTime;
//
//			if (frameTime > 0.25f) frameTime = 0.25f;
//
//			currentTime = clock();
//			accumulator += frameTime;
//
//			if(accumulator >= dt) {
//
//				this->_data->GetActiveState()->HandleInput();
//				this->_data->GetActiveState()->Update(accumulator/dt * dt);
//
//				accumulator -= accumulator/dt * dt;
//			}
//
//			///////////////////////////////////////////////////////////////////////////////////////////////////////////
//			this->_data->GetActiveState()->HandleInput();
//
//			this->_data->GetActiveState()->Draw();
//
//		}
	}
}

