#pragma once

#include "Game.h"
#include "State.h"
#include <memory>
#include "StateMachine.h"
#include "GameState.h"
#include "DEFINITION.h"

namespace GameLogic {
	class MainMenuState: public State
	{
	public:
		MainMenuState(GameDataRef data) {}
		
		void Init();

		void HandleInput();

		void Update(float ft);

		void Draw(float ft);

	private:
		GameDataRef _data;


		//TODO: WE ****DON"T **** need a clock for this state, but just for the purpose of testing
		clock_t _clock;
	};
}

