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
		//this->_data = data;
		// this->RandomizedPipeOffset();
		this->_pipeSpawnYOffset = 0;
		this->_landHeight = 0;

		// TODO: also needs to initiate landHeight and pipeSpawnYOffset later
	}

	void Pipe::SpawnPipe() {
		// spawn bottom pipe:
		// std::cout<<"Spawning new pipes!!!!!"<<std::endl;
		this->PipeLocations.push_back(std::pair<int, int>(319, 119 + this->_pipeSpawnYOffset));
	}


	int Pipe::MovePipes(float dt) {
		int score = 0;
		std::vector< std::pair<int,int> >::iterator it = this->PipeLocations.begin();
		while (it != this->PipeLocations.end()) {
			if (it->first < 9) {
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
		// FPGA interfase:
		for(int i = 0; i<this->PipeLocations.size(); i++) {
			*(RENDER_BASE + 3) = 0;
			*(RENDER_BASE + 4) = 0x06; // Set texture code to pipe up
			*(RENDER_BASE + 1) = this->PipeLocations[i].first;  // Set x-coor to center of screen
			*(RENDER_BASE + 2) = this->PipeLocations[i].second + 90 + PIPE_DISTANCE_WITH_CENTER;  // Set y-coor to center of screen
			*(RENDER_BASE + 6) = 0x05;    // Plot to buffer
			if(this->PipeLocations[i].second - 90 - PIPE_DISTANCE_WITH_CENTER < 0){
				*(RENDER_BASE + 4) = 0x05; // Set texture code to pipe down
				*(RENDER_BASE + 1) = this->PipeLocations[i].first;  // Set x-coor to center of screen
				*(RENDER_BASE + 3) = 1;
				*(RENDER_BASE + 2) = this->PipeLocations[i].second - 90 - PIPE_DISTANCE_WITH_CENTER;  // Set y-coor to center of screen
				*(RENDER_BASE + 6) = 0x05;    // Plot to buffer
			}else{
				*(RENDER_BASE + 3) = 0;
				*(RENDER_BASE + 4) = 0x05; // Set texture code to pipe down
				*(RENDER_BASE + 1) = this->PipeLocations[i].first;  // Set x-coor to center of screen
				*(RENDER_BASE + 2) = this->PipeLocations[i].second - 90 - PIPE_DISTANCE_WITH_CENTER;  // Set y-coor to center of screen
				*(RENDER_BASE + 6) = 0x05;    // Plot to buffer
			}

		}

	}
	// x-coor range:
	// [0, 319]
	// y-coor range:
	// [0, 239]

	void Pipe::RandomizedPipeOffset() {
		// do nothing for now

		this->_pipeSpawnYOffset = std::rand() % 40 - 20;
	}


	bool Pipe::CheckCollision(double birdYPosition){
		for(std::pair<int,int> p: PipeLocations){
			if(p.first > 9 && p.first < 21){

//				std::cout<<birdYPosition<<"  "<<p.second-119<<std::endl;
				if(birdYPosition+6 > p.second+PIPE_DISTANCE_WITH_CENTER || birdYPosition-6 < p.second-PIPE_DISTANCE_WITH_CENTER){
					return true;
				}
			}else if(p.first >= 21) break;
		}


//		auto it = PipeLocations.begin();
//		if((*it).first < 9) return 0;
//		else if((*it).first>=9 && (*it).first < 21){
//			if(birdYPosition+6 > (*it).second+PIPE_DISTANCE_WITH_CENTER ||
//					birdYPosition-6 < (*it).second-PIPE_DISTANCE_WITH_CENTER){
//				return 1;
//			}
//		}


		return false;

	}
}


