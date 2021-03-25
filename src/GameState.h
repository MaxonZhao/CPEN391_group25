/*
 * GameState.h
 *
 *  Created on: Mar 24, 2021
 *      Author: patri
 */

#ifndef GAMESTATE_H_
#define GAMESTATE_H_

#include "Game.h"
#include "Pipe.h"
#include <time.h>       /* clock_t, clock, CLOCKS_PER_SEC */


namespace GameLogic {
	class GameState: public State
	{
	public:
		GameState(StateMachine* data);

		void Init();

		void HandleInput();

		void Update(float dt);

		void Draw(float dt);

	private:
		StateMachine* _data;
		clock_t _clock;
		int _gameState;
		Pipe* _pipe;
	};
}




#endif /* GAMESTATE_H_ */
