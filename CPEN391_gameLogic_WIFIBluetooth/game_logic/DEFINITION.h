/*
 * DEFINITION.h
 *
 *  Created on: Mar 24, 2021
 *      Author: patri
 */

#ifndef DEFINITION_H_
#define DEFINITION_H_


#define SPLASH_STATE_SHOW_TIME 3.0


#define RENDER_BASE (volatile unsigned int *)(0xFF202080)
#define PUSHBUTTONS (volatile unsigned int *)(0xFF200010)

// #define v=0x4F;
// #define SPLASH_STATE_BACKGROUND_COLOR = 0x6A;

#define COLORCHANGEDDURATION 30
#define WAITUNTILGAMEOVER 200


//#define PIPE_SPAWN_FREQUENCY_1 300
#define PIPE_MOVEMENT_SPEED 4.0f
#define PIPE_DISTANCE_WITH_CENTER 120


#define BIRD_STATE_STILL 1
#define BIRD_STATE_FALLING 2
#define BIRD_STATE_FLYING 3

#define GRAVITY 10.0f
#define FLYING_SPEED 15.0f
#define FLYING_DURATION 100.0f

#define BIRD_ANIMATION_DURATION 0.3f

#define SCORE 1

#define WIDTH_DIGIT 20



#endif /* DEFINITION_H_ */
