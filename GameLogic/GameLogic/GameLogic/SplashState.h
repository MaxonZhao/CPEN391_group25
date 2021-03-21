#pragma once

#include "Game.h"
#include "State.h"
#include <memory>
#include "StateMachine.h"
#include "MainMenuState.h"
#include "DEFINITION.h"

namespace GameLogic {
	class SplashState: public State
	{
	public:
		SplashState(GameDataRef data);

		void Init();
		void HandleInput();
		void Update(float dt);
		void Draw(float dt);

	private:
		GameDataRef _data; 
		
		clock_t _clock;

	};
}

