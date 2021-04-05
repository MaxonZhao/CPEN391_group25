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
	// typedef std::unique_ptr<State> StateRef;

	class StateMachine
	{
	public:
//		StateMachine(){};
//		~StateMachine(){};

		void AddState(State* newState);
		void RemoveState();

		void ProcessStateChanges();

		State* GetActiveState();

		char userName[16];
		bool PlayInGuestMode;
		int score;

		// debugging, will delete later:
		int count_update = 0;
		int count_draw = 0;

	private:
		std::stack<State*> _states;
		State* _newState;

		bool _isRemoving;
		bool _isAdding;
		bool _isReplacing;
	};
}


#endif /* STATEMACHINE_H_ */
