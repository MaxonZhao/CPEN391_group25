#include "Pipe.h"


namespace Patrick {
	Pipe::Pipe(GameDataRef data) : _data(data) {
		_landHeight = _data->assets.GetTexture("Land").getSize().y;
		_pipeSpawnYOffset = 0;
	}

	void Pipe::SpawnBottomPipe() {
		sf::Sprite sprite(_data->assets.GetTexture("Pipe_Up"));

		sprite.setPosition(_data->window.getSize().x,
			_data->window.getSize().y 
			- sprite.getGlobalBounds().height
			- _pipeSpawnYOffset);

		PipeSprites.push_back(sprite);
	}
	void Pipe::SpawnTopPipe() {
		sf::Sprite sprite(_data->assets.GetTexture("Pipe_Down"));

		sprite.setPosition(_data->window.getSize().x, -_pipeSpawnYOffset);

		PipeSprites.push_back(sprite);
	}
	void Pipe::SpawnInvisiblePipe() {
		sf::Sprite sprite(_data->assets.GetTexture("Pipe_Up"));

		sprite.setPosition(_data->window.getSize().x,
			_data->window.getSize().y - sprite.getGlobalBounds().height);

		sprite.setColor(sf::Color(0, 0, 0, 0));

		PipeSprites.push_back(sprite);
	}
	void Pipe::MovePipes(float dt) {
		for (unsigned short int i = 0; i < PipeSprites.size(); i++) {
			
			if (PipeSprites.at(i).getPosition().x < 0 - 
				PipeSprites.at(i).getGlobalBounds().width) {
				PipeSprites.erase(PipeSprites.begin() + i);
			}
			else {
				float movement = PIPE_MOVEMENT_SPEED * dt;
				PipeSprites.at(i).move(-movement, 0);
			}
			
		}

		
	}

	void Pipe::DrawPipes() {
		for (unsigned short int i = 0; i < PipeSprites.size(); i++) {
			_data->window.draw(PipeSprites.at(i));
		}
	}


	void Pipe::RandomizedPipeOffset() {
		_pipeSpawnYOffset = rand() % (_landHeight + 1);
	}
	
}
