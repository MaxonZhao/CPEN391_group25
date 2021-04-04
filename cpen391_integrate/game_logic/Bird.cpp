#include "Bird.h"
#include <iostream>

namespace GameLogic {
	Bird::Bird(){
		this->_animationIterator = 0;
		this->birdYPosition = 119;

		this->_clock = clock();

		this->_movementClock = clock();

		this->_birdState = BIRD_STATE_FLYING;
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

	void Bird::Animate(float dt) {
		if (clock()-this->_clock > BIRD_ANIMATION_DURATION/4) {
			if (_animationIterator < 3) {
				_animationIterator++;
			}
			else {
				_animationIterator = 0;
			}
			this->Draw();

			_clock = clock();
		}
	}

	void Bird::Update(float dt) {
		if (BIRD_STATE_FALLING == _birdState) {
			this->birdYPosition += GRAVITY * dt;
		}
		else if (BIRD_STATE_FLYING == _birdState) {
			this->birdYPosition -= FLYING_SPEED * dt;
		}

		if (clock() - _movementClock > FLYING_DURATION) {
			_movementClock = clock();
			_birdState = BIRD_STATE_FALLING;
		}
	}

	void Bird::Tap() {
		// std::cout<<"Bird jump"<<std::endl;
		_movementClock = clock();
		_birdState = BIRD_STATE_FLYING;
	}
}
