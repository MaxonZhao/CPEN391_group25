/*
 * Game.h
 *
 *  Created on: Mar 24, 2021
 *      Author: patri
 */

#ifndef GAME_H_
#define GAME_H_

#include <time.h>
#include <memory>
#include "StateMachine.h"
#include "MainMenuState.h"


namespace GameLogic {

	class Game
	{
	public:
		//Create a new Game:
		Game();

	private:
		// const float dt = 1.0f / 10.0f;
		clock_t _clock;

		StateMachine* _data;

		void Run();
	};
}



#endif /* GAME_H_ */
