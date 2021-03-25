/*
 * Pipe.cpp
 *
 *  Created on: Mar 24, 2021
 *      Author: patri
 */

#include "Pipe.h"

#include <iostream>

namespace GameLogic {
	Pipe::Pipe() {
		//this->_data = data;


		// TODO: also needs to initiate landHeight and pipeSpawnYOffset later
	}

	void Pipe::SpawnPipe() {
		// spawn bottom pipe:
		std::cout<<"Spawning new pipes!!!!!"<<std::endl;
		this->PipeLocations.push_back(std::pair<int, int>(319, this->_pipeSpawnYOffset));
	}


	void Pipe::MovePipes(float dt) {
		// basically just minor 1 for every value in the vector

		std::vector< std::pair<int,int> >::iterator it = this->PipeLocations.begin();
		while (it != this->PipeLocations.end()) {
			if (it->first < 0) {
				it = this->PipeLocations.erase(it);
			}
			else {
				it->first -= PIPE_MOVEMENT_SPEED * dt;

				it++;
			}
		}
	}

	void Pipe::DrawPipes() {
		// FPGA interfase:
		for(int i = 0; i<this->PipeLocations.size(); i++) {
			*(RENDER_BASE + 4) = 0x06; // Set texture code to pipe up
			*(RENDER_BASE + 1) = this->PipeLocations[i].first;  // Set x-coor to center of screen
			*(RENDER_BASE + 2) = this->PipeLocations[i].second - PIPE_DISTANCE_WITH_CENTER;  // Set y-coor to center of screen
			*(RENDER_BASE + 6) = 0x05;    // Plot to buffer

			*(RENDER_BASE + 4) = 0x05; // Set texture code to pipe up
			*(RENDER_BASE + 1) = this->PipeLocations[i].first;  // Set x-coor to center of screen
			*(RENDER_BASE + 2) = this->PipeLocations[i].second + PIPE_DISTANCE_WITH_CENTER;  // Set y-coor to center of screen
			*(RENDER_BASE + 6) = 0x05;    // Plot to buffer
		}

	}
	// x-coor range:
	// [0, 319]
	// y-coor range:
	// [0, 239]

	void Pipe::RandomizedPipeOffset() {
		// do nothing for now

		this->_pipeSpawnYOffset = 119;
	}
}


