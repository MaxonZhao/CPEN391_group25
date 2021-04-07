/*
 * StateMachine.cpp
 *
 *  Created on: Mar 24, 2021
 *      Author: patri
 */


#include "StateMachine.h"
#include <iostream>

namespace GameLogic {

	void StateMachine::AddState(State* newState) {
		this->_isAdding = true;

		//////////////////////////////////////////////////////////////////////
		this->_isReplacing = true;

		// because StateRef is a unique pointer:
		this->_newState = newState;


		this->PlayInGuestMode = true;
		this->score = 0;
	}

	void StateMachine::ProcessStateChanges() {
		if (this->_isRemoving && !this->_states.empty()) {

			this->_states.pop();


			this->_isRemoving = false;
		}

		if (this->_isAdding) {
			if (!this->_states.empty())
				if (this->_isRemoving) this->_states.pop();

			this->_states.push(this->_newState);
			this->_states.top()->Init();
			this->_isAdding = false;
		}

	}

	State* StateMachine::GetActiveState() {
		return this->_states.top();
	}

}

