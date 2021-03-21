#pragma once
#include "Game.h"
#include "Pipe.h"


namespace GameLogic {
	class GameState: public State
	{
	public: 
		GameState(GameDataRef data);

		void Init();

		void HandleInput();

		void Update(float dt);

		void Draw(float dt);

	private:
		GameDataRef _data;

		clock_t _clock;

		int _gameState;

		Pipe* pipe;
	};
}

