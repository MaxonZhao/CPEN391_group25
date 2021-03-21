#include "Game.h"



namespace GameLogic {
	Game::Game() {

		this->_data->machine.AddState(StateRef(new SplashState(this->_data)));
		this->Run();
	}

	void Game::Run() {
		// the most important thing in the game logic:
		
		float newTime;
		float currentTime = clock();

		float frameTime;
		float accumulator;

		while (true) {
			this->_data->machine.ProcessStateChanges();

			newTime = clock();
			frameTime = newTime - currentTime;

			if (frameTime > 0.25f) frameTime = 0.25f;

			currentTime = newTime;
			accumulator += frameTime;

			while (accumulator >= dt) {
				this->_data->machine.GetActiveState()->HandleInput();
				this->_data->machine.GetActiveState()->Update(dt);

				accumulator -= dt;
			}

			this->_data->machine.GetActiveState()->Draw(accumulator / dt);

		}
	}
}