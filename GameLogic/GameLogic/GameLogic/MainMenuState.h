#pragma once
#include "Game.h"

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

	};
}

