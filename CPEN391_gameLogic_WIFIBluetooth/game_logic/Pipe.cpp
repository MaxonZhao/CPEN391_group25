/*
 * Pipe.cpp
 *
 *  Created on: Mar 24, 2021
 *      Author: patri
 */

#include "Pipe.h"

#include <iostream>
#include <cstdlib>

namespace GameLogic {
	Pipe::Pipe() {
		this->_pipeSpawnYOffset = 0;
		this->_landHeight = 0;
	}

	void Pipe::SpawnPipe() {
		this->PipeLocations.push_back(std::pair<int, int>(319, 119 + this->_pipeSpawnYOffset));
	}


	int Pipe::MovePipes(float dt) {
		int score = 0;
		std::vector< std::pair<int,int> >::iterator it = this->PipeLocations.begin();
		while (it != this->PipeLocations.end()) {
			if (it->first < 5) {
				score += SCORE;
				it = this->PipeLocations.erase(it);
			}
			else {
				it->first -= 10* PIPE_MOVEMENT_SPEED * dt;

				it++;
			}
		}
		return score;
	}

	void Pipe::DrawPipes() {
		for(int i = 0; i<this->PipeLocations.size(); i++) {
			*(RENDER_BASE + 3) = 0;
			*(RENDER_BASE + 4) = 0x06; // Set texture code to pipe up
			*(RENDER_BASE + 1) = this->PipeLocations[i].first;  // Set x-coor to center of screen
			*(RENDER_BASE + 2) = this->PipeLocations[i].second + PIPE_DISTANCE_WITH_CENTER;  // Set y-coor to center of screen
			*(RENDER_BASE + 6) = 0x05;    // Plot to buffer
			if(this->PipeLocations[i].second - PIPE_DISTANCE_WITH_CENTER < 0){
				*(RENDER_BASE + 4) = 0x05; // Set texture code to pipe down
				*(RENDER_BASE + 1) = this->PipeLocations[i].first;  // Set x-coor to center of screen
				*(RENDER_BASE + 3) = 1;
				*(RENDER_BASE + 2) = - this->PipeLocations[i].second + PIPE_DISTANCE_WITH_CENTER;  // Set y-coor to center of screen
				*(RENDER_BASE + 6) = 0x05;    // Plot to buffer
			}else{
				*(RENDER_BASE + 3) = 0;
				*(RENDER_BASE + 4) = 0x05; // Set texture code to pipe down
				*(RENDER_BASE + 1) = this->PipeLocations[i].first;  // Set x-coor to center of screen
				*(RENDER_BASE + 2) = this->PipeLocations[i].second - PIPE_DISTANCE_WITH_CENTER;  // Set y-coor to center of screen
				*(RENDER_BASE + 6) = 0x05;    // Plot to buffer
			}

		}

	}
	// x-coor range:
	// [0, 319]
	// y-coor range:
	// [0, 239]

	void Pipe::RandomizedPipeOffset() {
		this->_pipeSpawnYOffset = std::rand() % 60 - 30;
	}


	bool Pipe::CheckCollision(double birdYPosition){
		for(int i = 0; i<PipeLocations.size(); i++){
			std::pair<int,int> p = PipeLocations[i];
			if(p.first > 9 && p.first < 21){

				if(birdYPosition+6 > p.second+PIPE_DISTANCE_WITH_CENTER-90 || birdYPosition-6 < p.second-PIPE_DISTANCE_WITH_CENTER+90){
					return true;
				}
			}else if(p.first >= 21) return false;
		}
		return false;
	}
}


