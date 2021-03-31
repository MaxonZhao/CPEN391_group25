/*
 * Pipe.h
 *
 *  Created on: Mar 24, 2021
 *      Author: patri
 */

#ifndef PIPE_H_
#define PIPE_H_

#include <vector>
#include "DEFINITION.h"

namespace GameLogic {
	class Pipe
	{
	public:
		Pipe();

		void SpawnPipe();

		void MovePipes(float ft);

		void DrawPipes();

		void RandomizedPipeOffset();

	private:
		//StateMachine* _data;
		std::vector< std::pair<int,int> > PipeLocations;

		int _landHeight;
		int _pipeSpawnYOffset;
	};
}



#endif /* PIPE_H_ */
