// SAMPLE CODE FOR USING RENDERER COMPONENT
// This code sets the background to grey, plots a pipe at the center of the screen,
//  then a bird fly acrosses the screen from left to right.
// It stops running after the bird reaches the right, or when all 4 push buttons
//  on the De1 are pressed at the same time
#include <stdio.h>

#define renderer_base (volatile unsigned int *)(0xFF202080)
#define PUSHBUTTONS (volatile unsigned int *)(0xFF200010)
#define LEDS (volatile unsigned int *)(0xFF200020)

int main()
{
    int v = 0x05;       // Arbitrary value
    int grey_bg = 0x6A; // Grey background, 0x6A = 'b0110_1010
    //int yellow_bg = 0x3C; // Yellow background, 0x3C = 'b0111_1100

    printf("Starting test:");

    // Turn on LEDs to make sure code is running
    *LEDS = 0;

    int curr_frame = *(renderer_base + 5);
    int frame_count = 0;
    int state = 0;
    while (1)
    {
        if (*(renderer_base + 5) != curr_frame)
        {
            curr_frame = *(renderer_base + 5);
            frame_count += 1;
        }

        if (frame_count == 30)
        {
            frame_count = 0;

            // Plot grey background
            *(renderer_base + 4) = grey_bg; // Set texture code to grey background
            *(renderer_base + 6) = v;       // Plot to buffer

            // Plot pipe
            *(renderer_base + 4) = 0x06; // Set texture code to pipe up
            *(renderer_base + 1) = 159;  // Set x-coor to center of screen
            *(renderer_base + 2) = 119;  // Set y-coor to center of screen
            *(renderer_base + 6) = v;    // Plot to buffer

            // Plot bird
            *(renderer_base + 4) = state % 4 + 1; // Set texture code for bird
            *(renderer_base + 1) = state;         // Set x-coor to center of screen
            // *(renderer_base + 2) = 119;  // Y-coor already set from prev plot to center of screen
            *(renderer_base + 6) = v; // Plot to buffer

            state += 1;
        }

        // Stop if all keys pressed or if bird traversed across screen
        if (*PUSHBUTTONS == 1 || state == 319)
            break;
    }

    // Turn off LEDs to make sure code is still running
    *LEDS = 0x3E0;

    return (0);
}
