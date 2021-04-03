/*
 * SplashState.h
 *
 *  Created on: Mar 24, 2021
 *      Author: patri
 */

#ifndef SPLASHSTATE_H_
#define SPLASHSTATE_H_

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
		SplashState(StateMachine* data);

		void Init();
		void HandleInput();
		void Update(float dt);
		void Draw(float dt);

	private:
		StateMachine* _data;

		clock_t _clock;

	};
}



#endif /* SPLASHSTATE_H_ */
