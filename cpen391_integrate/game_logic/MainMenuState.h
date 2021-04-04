/*
 * MainMenuState.h
 *
 *  Created on: Mar 24, 2021
 *      Author: patri
 */

#ifndef MAINMENUSTATE_H_
#define MAINMENUSTATE_H_

#include "Game.h"
#include "State.h"
#include <memory>
#include "StateMachine.h"
#include "GameState.h"
#include "DEFINITION.h"
#include <time.h>
namespace GameLogic {
	class MainMenuState: public State
	{
	public:
		MainMenuState(StateMachine* data);

		void Init();

		void HandleInput();

		void Update(float ft);

		void Draw(float ft);

	private:
		StateMachine* _data;


		//TODO: WE ****DON"T **** need a clock for this state, but just for the purpose of testing
		clock_t _clock;

		unsigned int _animationIterator;

		int _birdColors[6];

		unsigned int _chosenBirdColorIndex;

		int readyToStart;
		
		int PlayInGuestMode;

		// don't need this clock later:
		clock_t _colorChanged;
	};

}



#endif /* MAINMENUSTATE_H_ */
