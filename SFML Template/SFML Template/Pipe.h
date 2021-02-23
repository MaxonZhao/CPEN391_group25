#pragma once

#include <SFML/Graphics.hpp>
#include "Game.h"
#include <vector>
#include "DEFINITIONS.h"

namespace Patrick {
	class Pipe
	{
	public:
		Pipe(GameDataRef data);

		void SpawnBottomPipe();
		void SpawnTopPipe();
		void SpawnInvisiblePipe();
		void MovePipes(float ft);
		
		void DrawPipes();

		void RandomizedPipeOffset();

	private:
		GameDataRef _data;
		std::vector<sf::Sprite> PipeSprites;

		int _landHeight;
		int _pipeSpawnYOffset;

	};
}


