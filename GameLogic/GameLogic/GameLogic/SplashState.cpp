#include "SplashState.h"

namespace GameLogic {
	SplashState::SplashState(GameDataRef data) {
		this->_data = data;
	}

	void SplashState::Init() {
		// there is nothing to be initiated
	}

	void SplashState::HandleInput() {
		// wait for bluetooth to start the game, then it will move to next state
	}
	void SplashState::Update(float dt) {
		if (this->_clock + SPLASH_STATE_SHOW_TIME < clock()) {
			_data->machine.AddState(StateRef(new MainMenuState(_data)));
		}
	}
	void SplashState::Draw(float dt) {
		//TODO: load texture here:
		*(RENDER_BASE + 4) = 0x6A;
		*(RENDER_BASE + 6) = 0x4F;
	}

}