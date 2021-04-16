/*
 * State.h
 *
 *  Created on: Mar 24, 2021
 *  Author: patrick
 *  Purpose: this is the template state for all states.
 */

#ifndef STATE_H_
#define STATE_H_

namespace GameLogic {
	class State {
	public:
		virtual void Init() = 0;
		virtual void HandleInput() = 0;
		virtual void Update(float dt) = 0;
		virtual void Draw() = 0;
	};
}



#endif /* STATE_H_ */
