#pragma once

#define SPLASH_STATE_SHOW_TIME 3.0


#define RENDER_BASE (volatile unsigned int *)(0xFF202080)

// #define v=0x4F;
// #define SPLASH_STATE_BACKGROUND_COLOR = 0x6A;


#define PIPE_SPAWN_FREQUENCY 2
#define PIPE_MOVEMENT_SPEED 200.0f
#define PIPE_DISTANCE_WITH_CENTER 20



enum GameStates {
	Ready,
	Playing,
	GameOver
};