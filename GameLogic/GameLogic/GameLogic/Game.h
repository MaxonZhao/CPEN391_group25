#pragma once

#include <time.h>
#include <memory>
#include "StateMachine.h"
#include "SplashState.h"


namespace GameLogic {

	struct GameData {
		StateMachine machine;

	};

	typedef std::shared_ptr<GameData> GameDataRef;

	class Game
	{
	public:
		//Create a new Game:
		Game();

	private:
		const float dt = 1.0f / 60.0f;
		clock_t _clock;

		// _data is a shared pointer to type GameData
		GameDataRef _data = std::make_shared<GameData>();

		void Run();


	};
}

