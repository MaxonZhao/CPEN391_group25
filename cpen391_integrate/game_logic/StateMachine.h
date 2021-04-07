/*
 * StateMachine.h
 *
 *  Created on: Mar 24, 2021
 *      Author: patri
 */

#ifndef STATEMACHINE_H_
#define STATEMACHINE_H_


#include <stack>
#include <memory>
#include "State.h"

namespace GameLogic {

	class StateMachine
	{
	public:

		void AddState(State* newState);

		void ProcessStateChanges();

		State* GetActiveState();

		char userName[16];
		bool PlayInGuestMode = true;
		int score = 0;


		int pipe_spawn_frequency = 300;


	private:
		State* _states;
		State* _newState;
		bool _isAdding;
	};
}


#endif /* STATEMACHINE_H_ */
