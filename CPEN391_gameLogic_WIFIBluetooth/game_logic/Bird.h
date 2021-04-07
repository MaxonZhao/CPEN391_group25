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

		void Update(float dt);

		void Tap();

		double birdYPosition;


	private:

		unsigned int _animationIterator;

		clock_t _clock;

		clock_t _movementClock;

		bool isBirdFlying;
	};
}



#endif /* BIRD_H_ */
