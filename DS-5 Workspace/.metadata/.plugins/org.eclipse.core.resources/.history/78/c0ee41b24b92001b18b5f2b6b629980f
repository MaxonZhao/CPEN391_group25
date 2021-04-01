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
		*(RENDER_BASE + 4) = _animationIterator % 4 + 1;
		*(RENDER_BASE + 1) = 15;
		*(RENDER_BASE + 2) = this->birdYPosition;
		*(RENDER_BASE + 6) = 0x05;
		_animationIterator++;
	}

//	void Bird::Animate(float dt) {
//		if (_clock.getElapsedTime().asSeconds() > BIRD_ANIMATION_DURATION/_animationFrames.size()) {
//			if (_animationIterator < _animationFrames.size() - 1) {
//				_animationIterator++;
//			}
//			else {
//				_animationIterator = 0;
//			}
//			_birdSprite.setTexture(_animationFrames.at(_animationIterator));
//
//			_clock.restart();
//		}
//	}

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
		std::cout<<"Button Pressed"<<std::endl;
		_movementClock = clock();
		_birdState = BIRD_STATE_FLYING;
	}
}
