#include "GameState.h"



namespace Patrick {
	GameState::GameState(GameDataRef data) : _data(data) {
	}

	void GameState::Init() {
		_data->assets.LoadTexture("Game_Background",
			GAME_BACKGROUND_FILEPATH);
		_data->assets.LoadTexture("Pipe_Up",
			PIPE_UP_FILEPATH);
		_data->assets.LoadTexture("Pipe_Down",
			PIPE_DOWN_FILEPATH);
		_data->assets.LoadTexture("Land",
			LAND_FILEPATH);
		_data->assets.LoadTexture("Bird_Frame_1",
			BIRD_FRAME_1_FILEPATH);
		_data->assets.LoadTexture("Bird_Frame_2",
			BIRD_FRAME_2_FILEPATH);
		_data->assets.LoadTexture("Bird_Frame_3",
			BIRD_FRAME_3_FILEPATH);
		_data->assets.LoadTexture("Bird_Frame_4",
			BIRD_FRAME_4_FILEPATH);

		pipe = new Pipe(_data);
		land = new Land(_data);
		bird = new Bird(_data);

		_background.setTexture(this->_data->assets
			.GetTexture("Game_Background"));

		_gameState = GameStates::eReady;
	}
	void GameState::HandleInput() {
		sf::Event event;

		while (_data->window.pollEvent(event)) {
			if (sf::Event::Closed == event.type) {
				_data->window.close();
			}

			if (GetKeyState('E') & 0x8000)
			{
				if (GameStates::eGameOver != _gameState) {
					_gameState = GameStates::ePlaying;
					bird->Tap();
				}
				
			}

		}
	}

	void GameState::Update(float dt) {
		if (GameStates::eGameOver != _gameState) {
			bird->Animate(dt);
			land->MoveLand(dt);
		}
		if (_gameState == GameStates::ePlaying) {
			pipe->MovePipes(dt);
			if (clock.getElapsedTime().asSeconds() > PIPE_SPAWN_FREQUENCY) {
				pipe->RandomizedPipeOffset();

				pipe->SpawnInvisiblePipe();
				pipe->SpawnBottomPipe();
				pipe->SpawnTopPipe();


				clock.restart();
			}

			bird->Update(dt);
			std::vector<sf::Sprite> landSprites = land->GetSprites();
			for (sf::Sprite sp : landSprites) {
				if (Collision.CheckSpriteCollistion(bird->GetSprite(), 0.7f,
					sp, 1.0f)) {
					_gameState = GameStates::eGameOver;
				}
			}

			std::vector<sf::Sprite> pipeSprites = pipe->GetSprtes();
			for (sf::Sprite pp : pipeSprites) {
				if (Collision.CheckSpriteCollistion(bird->GetSprite(), 0.625f,
					pp, 1.0f)) {
					_gameState = GameStates::eGameOver;
				}
			}
		}
		
	}

	void GameState::Draw(float dt) {
		_data->window.clear();
		_data->window.draw(_background);
		pipe->DrawPipes();
		land->DrawLand();
		bird->Draw();

		_data->window.display();
	}
}