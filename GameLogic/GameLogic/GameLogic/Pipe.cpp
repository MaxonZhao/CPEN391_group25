#include "Pipe.h"


namespace GameLogic {
	Pipe::Pipe(GameDataRef data) {
		this->_data = data;
		

		// TODO: also needs to initiate landHeight and pipeSpawnYOffset later
	}

	void Pipe::SpawnPipe() {
		// spawn bottom pipe:
		this->PipeLocations.push_back({315, this->_pipeSpawnYOffset});
	}


	void Pipe::MovePipes(float dt) {
		// basically just minor 1 for every value in the vector
		auto it = this->PipeLocations.begin();
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
		for (std::pair<int, int> p : this->PipeLocations) {
			*(RENDER_BASE + 4) = 0x06; // Set texture code to pipe up
			*(RENDER_BASE + 1) = p.first;  // Set x-coor to center of screen
			*(RENDER_BASE + 2) = p.second - PIPE_DISTANCE_WITH_CENTER;  // Set y-coor to center of screen
			*(RENDER_BASE + 6) = 0x05;    // Plot to buffer

			*(RENDER_BASE + 4) = 0x05; // Set texture code to pipe up
			*(RENDER_BASE + 1) = p.first;  // Set x-coor to center of screen
			*(RENDER_BASE + 2) = p.second + PIPE_DISTANCE_WITH_CENTER;  // Set y-coor to center of screen
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