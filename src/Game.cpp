/*
 * Game.cpp
 *
 *  Created on: Mar 24, 2021
 *      Author: patri
 */


#include "Game.h"
#include <iostream>
namespace GameLogic {
	Game::Game() {
		this->_data = new StateMachine();
		this->_data->AddState(new GameState(this->_data));
		this->Run();
	}

	void Game::Run() {
		// the most important thing in the game logic:

		float newTime;
		float currentTime = clock();

		float frameTime;
		float accumulator;

		while (true) {
			this->_data->ProcessStateChanges();
			newTime = clock();
			frameTime = newTime - currentTime;

			if (frameTime > 0.25f) frameTime = 0.25f;

			currentTime = newTime;
			accumulator += frameTime;

			while (accumulator >= dt) {

				this->_data->GetActiveState()->HandleInput();
				this->_data->GetActiveState()->Update(dt);

				accumulator -= dt;
			}

			this->_data->GetActiveState()->Draw(accumulator / dt);

		}
	}
}

