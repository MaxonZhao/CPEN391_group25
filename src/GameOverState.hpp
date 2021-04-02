/*
 * GameOverState.hpp
 *
 *  Created on: Apr 2, 2021
 *      Author: patri
 */

#ifndef GAMEOVERSTATE_HPP_
#define GAMEOVERSTATE_HPP_

#include "State.h"
#include "Game.h"


namespace GameLogic {
	class GameOverState: public State {
	public:
		GameOverState(StateMachine* data, int score);

		void Init();

		void HandleInput();
		void Update(float dt);
		void Draw(float dt);

		void DrawScore();

		void plot(int digit, int xPosition);

	private:
		StateMachine* _data;
		int _score;
	};
}



#endif /* GAMEOVERSTATE_HPP_ */
