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

		this->_newState = newState;
	}

	void StateMachine::ProcessStateChanges() {
		if (this->_isAdding) {
			delete(this->_states);
			this->_states = this->_newState;

			this->_states->Init();
			this->_isAdding = false;
		}

	}

	State* StateMachine::GetActiveState() {
		return this->_states;
	}

}

