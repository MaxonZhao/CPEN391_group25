#include "GameOverState.h"
#include <sstream>
#include "DEFINITIONS.h"

#include <iostream>
#include <Windows.h>

namespace Patrick {
	GameOverState::GameOverState(GameDataRef data) : _data(data) {

	}

	void GameOverState::Init() {
		_data->assets.LoadTexture("Game Over Background",
			GAME_OVER_BACKGROUND_FILEPATH);

		_background.setTexture(this->_data->assets
			.GetTexture("Game Over Background"));
	}
	void GameOverState::HandleInput() {
		sf::Event event;

		while (_data->window.pollEvent(event)) {
			if (sf::Event::Closed == event.type) {
				_data->window.close();
			}

		}
	}

	void GameOverState::Update(float dt) {

	}

	void GameOverState::Draw(float dt) {
		_data->window.clear();
		_data->window.draw(_background);

		_data->window.display();
	}
}