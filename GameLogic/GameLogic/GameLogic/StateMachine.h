#pragma once

#include <stack>
#include <memory>
#include "State.h"

namespace GameLogic {
	typedef std::unique_ptr<State> StateRef;

	class StateMachine
	{
	public: 
		StateMachine() {}
		~StateMachine() {}

		void AddState(StateRef newState, bool isReplacing = true);
		void RemoveState();

		void ProcessStateChanges();

		StateRef& GetActiveState();

	private:
		std::stack<StateRef> _states;
		StateRef _newState;

		bool _isRemoving;
		bool _isAdding;
		bool _isReplacing;

	};
}

