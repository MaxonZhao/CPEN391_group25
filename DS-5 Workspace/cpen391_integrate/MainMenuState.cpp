/*
 * MainMenuState.cpp
 *
 *  Created on: Mar 24, 2021
 *      Author: patri
 */


#include "MainMenuState.h"


namespace GameLogic {
	MainMenuState::MainMenuState(StateMachine* data) {
		this->_data = data;
	}

	void MainMenuState::Init() {
		// There is nothing to be initiated
	}

	void MainMenuState::HandleInput() {
		// handle user input here
		// TODO: call the function implemented by Zoey
	}

	void MainMenuState::Update(float dt) {
		// there is nothing to be update in the mainmenu state.
		if (this->_clock + SPLASH_STATE_SHOW_TIME < clock()) {
			_data->AddState(new GameState(_data));
		}
	}

	void MainMenuState::Draw(float dt) {
		// TODO:
		// 1. draw the background
		// 2. draw the "flappy bird" text
		// 3. draw the bird texture:
		*(RENDER_BASE + 4) = 0x3C;
		*(RENDER_BASE + 6) = 0x05;
	}
}

