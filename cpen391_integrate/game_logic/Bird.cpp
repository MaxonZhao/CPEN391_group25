#include "Bird.h"
#include <iostream>

namespace GameLogic {
	Bird::Bird(){
		this->_animationIterator = 0;
		this->birdYPosition = 119;

		this->_clock = clock();

		this->_movementClock = clock();

		this->isBirdFlying = false;
	}

	void Bird::Draw() {
		// Plot bird
		*(RENDER_BASE + 3) = 0;
		*(RENDER_BASE + 4) = _animationIterator % 4 + 1;
		*(RENDER_BASE + 1) = 15;
		*(RENDER_BASE + 2) = int(this->birdYPosition);
		*(RENDER_BASE + 6) = 0x05;
		_animationIterator++;
	}


	void Bird::Update(float dt) {
		if (!this->isBirdFlying) {
			this->birdYPosition += GRAVITY * dt;
		}else{
			this->birdYPosition -= FLYING_SPEED * dt;
		}

		if (clock() - _movementClock > FLYING_DURATION) isBirdFlying = false;

	}

	void Bird::Tap() {
		// std::cout<<"Bird jump"<<std::endl;
		_movementClock = clock();
		isBirdFlying = true;
	}
}
