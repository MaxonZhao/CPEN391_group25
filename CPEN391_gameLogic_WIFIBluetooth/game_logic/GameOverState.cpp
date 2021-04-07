/*
 * GameOverState.cpp
 *
 *  Created on: Apr 2, 2021
 *      Author: patri
 */

#include "GameOverState.hpp"
#include "../RS232/serial.h"
#include "../RS232/wifi.h"
#include <iostream>
#include<cstring>  

namespace GameLogic {
	GameOverState::GameOverState(StateMachine* data){
		this->_data = data;
		this->_score = data->score;
	}

	void GameOverState::Init(){
		// 1. draw the background
		*(RENDER_BASE + 3) = 0;
		*(RENDER_BASE + 4) = 0x5F;
		*(RENDER_BASE + 6) = 0x1F;

		// 2. displaying medal
		if(this->_score < 10){
			*(RENDER_BASE + 4) = 18;
			*(RENDER_BASE + 1) = 160;
			*(RENDER_BASE + 2) = 120;
			*(RENDER_BASE + 6) = 0x05;
		}else{
			*(RENDER_BASE + 4) = 17;
			*(RENDER_BASE + 1) = 160;
			*(RENDER_BASE + 2) = 120;
			*(RENDER_BASE + 6) = 0x05;
		}


		// 3. displaying score
		this->DrawScore();

		// 4. Game Over:
		*(RENDER_BASE + 4) = 20;
		*(RENDER_BASE + 1) = 160;
		*(RENDER_BASE + 2) = 60;
		*(RENDER_BASE + 6) = 0x05;

		//send score to the App as a closing signal
		char score_s [8] ;
		std::snprintf(score_s, 8, "%d", this->_score); // covert score from int to string
		sendMessage(score_s,BT_LineStatusReg, BT_TransmitterFifo);

		//upload score to the cloud Database
		//TODO: replace username "test" with prestored username.
		//Only upload if not PlayInGuestMode
		if(this->_data->PlayInGuestMode) return;
		int err = uploadScore(this->_data->userName, this->_score);
		if (!err){
				std::cout<< "Successfully upload score to the cloud Database."<<std::endl;
		}


	}

	void GameOverState::HandleInput(){
		if (*PUSHBUTTONS == 13){
			_data->AddState(new MainMenuState(_data));
		}

	}

	void GameOverState::Update(float dt){

	}

	void GameOverState::Draw(){

	}

	void GameOverState::DrawScore(){
			int s = this->_score;
			std::vector<int> v;
			while(s != 0){
				v.push_back(s%10);
				s/=10;
			}

			int xPosition = 160;
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
