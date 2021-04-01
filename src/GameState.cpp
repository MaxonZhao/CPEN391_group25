/*
 * GameState.cpp
 *
 *  Created on: Mar 24, 2021
 *      Author: patri
 */

#include "GameState.h"
#include <vector>
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

		this->_score = 0;
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
			this->_score += this->_pipe->MovePipes(dt);

			if (this->_clock + PIPE_SPAWN_FREQUENCY < clock()) {
				// generate pipe:
				this->_pipe->RandomizedPipeOffset();
				this->_pipe->SpawnPipe();

				this->_clock = clock();
			}

//			this->_bird->Animate(dt);
			this->_bird->Update(dt);

			// collision detection:
			if(this->_bird->birdYPosition <= 12 || this->_bird->birdYPosition >= 227 ||
					 this->_pipe->CheckCollision(this->_bird->birdYPosition)){
				this->_gameState = GameOver;
			}
		}


	}

	void GameState::Draw(float dt) {
		if(this->_gameState == GameOver) return;
		// draw the pipe here:
		 this->ReDrawBackground();
		 _pipe->DrawPipes();
		 _bird->Draw();

		 if(this->_score == 0){
			 this->plot(0, 15);
		 }else{
			 this->DrawScore();
		 }

	}

	void GameState::ReDrawBackground(){
		*(RENDER_BASE + 4) = 0x6A;
		*(RENDER_BASE + 6) = 0x4F;
	}

	void GameState::DrawScore(){
		int s = this->_score;
		std::vector<int> v;
		while(s != 0){
			v.push_back(s%10);
			s/=10;
		}

		int xPosition = 15;
		for(int i = v.size()-1; i>=0; i--){
			plot(v[i], xPosition);
			xPosition += WIDTH_DIGIT;
		}
	}

	void GameState::plot(int digit, int xPosition){
		*(RENDER_BASE + 3) = 0;
		*(RENDER_BASE + 4) = 7+digit;
		*(RENDER_BASE + 1) = xPosition;
		*(RENDER_BASE + 2) = 10;
		*(RENDER_BASE + 6) = 0x05;
	}
}
