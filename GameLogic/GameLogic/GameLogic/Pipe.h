#pragma once

#include "Game.h"
#include <vector>

namespace GameLogic {
	class Pipe
	{
	public: 
		Pipe(GameDataRef data);

		void SpawnPipe();

		void MovePipes(float ft);

		void DrawPipes();

		void RandomizedPipeOffset();

	private:
		GameDataRef _data;
		std::vector<std::pair<int,int>> PipeLocations;

		int _landHeight;
		int _pipeSpawnYOffset;
	};
}

