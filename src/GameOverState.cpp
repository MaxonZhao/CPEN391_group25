/*
 * GameOverState.cpp
 *
 *  Created on: Apr 2, 2021
 *      Author: patri
 */

#include "GameOverState.hpp"

namespace GameLogic {
	GameOverState::GameOverState(StateMachine* data, int score){
		this->_data = data;
		this->_score = score;
	}

	void GameOverState::Init(){
		// 1. draw the background
		*(RENDER_BASE + 4) = 0x6A;
		*(RENDER_BASE + 6) = 0x4F;

		// 2. displaying medal
		*(RENDER_BASE + 3) = 0;
		*(RENDER_BASE + 4) = 17;
		*(RENDER_BASE + 1) = 160;
		*(RENDER_BASE + 2) = 120;
		*(RENDER_BASE + 6) = 0x05;

		// 3. displaying score
		this->DrawScore();
	}

	void GameOverState::HandleInput(){
		if (*PUSHBUTTONS == 13){
			_data->AddState(new MainMenuState(_data));
		}
	}

	void GameOverState::Update(float dt){

	}

	void GameOverState::Draw(float dt){
		// I don't wanna use this function
	}

	void GameOverState::DrawScore(){
			int s = this->_score;
			std::vector<int> v;
			while(s != 0){
				v.push_back(s%10);
				s/=10;
			}

			int xPosition = 120;
			for(int i = v.size()-1; i>=0; i--){
				plot(v[i], xPosition);
				xPosition += WIDTH_DIGIT;
			}
		}

	void GameOverState::plot(int digit, int xPosition){
		*(RENDER_BASE + 3) = 0;
		*(RENDER_BASE + 4) = 7+digit;
		*(RENDER_BASE + 1) = xPosition;
		*(RENDER_BASE + 2) = 10;
		*(RENDER_BASE + 6) = 0x05;
	}

}
