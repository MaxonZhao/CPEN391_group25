/*
 * Bird.h
 *
 *  Created on: Mar 30, 2021
 *      Author: patri
 */

#ifndef BIRD_H_
#define BIRD_H_

#include "DEFINITION.h"
#include <time.h>

namespace GameLogic {
	class Bird
	{
	public:
		Bird();

		void Draw();

//		void Animate(float dt);

		void Update(float dt);

		void Tap();


	private:
		int birdYPosition;

		unsigned int _animationIterator;

		clock_t _clock;

		clock_t _movementClock;

		int _birdState;
	};
}



#endif /* BIRD_H_ */
