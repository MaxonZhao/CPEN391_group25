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
#include "SplashState.h"
#include "GameState.h"


namespace GameLogic {

//	struct GameData {
//		StateMachine machine;
//	};

	// typedef GameData* GameDataRef;
	enum GameStates {
		Ready,
		Playing,
		GameOver
	};

	class Game
	{
	public:
		//Create a new Game:
		Game();

	private:
		const float dt = 1.0f / 60.0f;
		clock_t _clock;

		// _data is a shared pointer to type GameData
		StateMachine* _data;

		void Run();


	};
}



#endif /* GAME_H_ */
