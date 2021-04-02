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
#include "Bird.h"
#include <time.h>       /* clock_t, clock, CLOCKS_PER_SEC */
#include "GameOverState.hpp"


namespace GameLogic {
	class GameState: public State
	{
	public:
		GameState(StateMachine* data);

		void Init();

		void HandleInput();

		void Update(float dt);

		void Draw(float dt);

		void ReDrawBackground();

		void DrawScore();

		void plot(int digit, int xPosition);

	private:
		int _score;
		StateMachine* _data;
		clock_t _clock;
		int _gameState;
		Pipe* _pipe;
		Bird* _bird;


		// maybe we don't need this later:
		clock_t _waitscreenClock;
	};
}




#endif /* GAMESTATE_H_ */
