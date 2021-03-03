#pragma once
#include <SFML/Graphics.hpp>
#include "State.hpp"
#include "Pipe.h"
#include "Game.h"
#include <sstream>
#include "DEFINITIONS.h"
#include "Land.h"
#include "Bird.h"
#include "Collision.h"


#include <iostream>
#include <Windows.h>


namespace Patrick {
	class GameState : public State
	{
	public:
		GameState(GameDataRef data);

		void Init();

		void HandleInput();

		void Update(float dt);

		void Draw(float dt);

	private:
		GameDataRef _data;

		sf::Sprite _background;

		Pipe* pipe;
		Land* land;
		Collision Collision;
		int _gameState;

		sf::Clock clock;
		Bird* bird;
	};
}
