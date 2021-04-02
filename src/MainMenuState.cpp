/*
 * MainMenuState.cpp
 *
 *  Created on: Mar 24, 2021
 *      Author: patri
 */


#include "MainMenuState.h"

#include <iostream>


namespace GameLogic {
	MainMenuState::MainMenuState(StateMachine* data) {
		this->_data = data;
		std::cout<<"Hi New!"<<std::endl;
	}

	void MainMenuState::Init() {
		// There is nothing to be initiated
		this->_clock  = clock();
		this->_colorChanged = clock();
		this->_animationIterator = 0;
		this->_chosenBirdColorIndex = 0;
	}

	void MainMenuState::HandleInput() {
		if(*PUSHBUTTONS == 14){
			// start the game.
			_data->AddState(new GameState(_data));
		}else if (*PUSHBUTTONS == 13){
			// change the color of the bird
			if(this->_colorChanged + COLORCHANGEDDURATION < clock()){
				std::cout<<"changing!"<<std::endl;
				this->_chosenBirdColorIndex++;
				this->_colorChanged = clock();
			}

		}

	}

	void MainMenuState::Update(float dt) {
		// there is nothing to be update in the mainmenu state.
//		if (this->_clock + SPLASH_STATE_SHOW_TIME < clock()) {
//			_data->AddState(new GameState(_data));
//		}
		// do nothing?


	}

	void MainMenuState::Draw(float dt) {
		// TODO:
		// 1. draw the background
		*(RENDER_BASE + 4) = 0x6A;
		*(RENDER_BASE + 6) = 0x4F;


		// 2. draw the "flappy bird" text


		// 3. draw the bird texture:
		*(RENDER_BASE + 3) = 0;
		*(RENDER_BASE + 4) = (this->_animationIterator++) % 4 + 1;
		*(RENDER_BASE + 1) = 160;
		*(RENDER_BASE + 2) = 120;
		*(RENDER_BASE + 7) = this->_birdColors[this->_chosenBirdColorIndex % 6];
		*(RENDER_BASE + 6) = 0x05;



	}
}

