#include "GameState.h"


namespace GameLogic {
	GameState::GameState(GameDataRef data) {
		this->_data = data;
	}

	void GameState::Init() {
		// TODO: need to initiate a pipe class later:
		this->pipe = new Pipe(_data);

		this->_gameState = GameStates::Ready;

		this->_clock = clock();
	}

	void GameState::HandleInput() {
		// do this later:
	}

	void GameState::Update(float dt) {
		// update the location of textures:
		if (this->_gameState == GameStates::Playing) {
			this->pipe->MovePipes(dt);

			if (this->_clock + dt < clock()) {
				// generate pipe:
				this->pipe->RandomizedPipeOffset();
				this->pipe->SpawnPipe();

				this->_clock = clock();
			}
		}



	}

	void GameState::Draw(float dt) {
		// draw the pipe here:
		this->pipe->DrawPipes();
	}
}