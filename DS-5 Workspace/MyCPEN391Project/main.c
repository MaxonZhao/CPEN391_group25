/*
 * main.c
 *
 *  Created on: Mar 1, 2021
 *      Author: zoeyli
 */
#include "serial.h"
void main(void){
	char a,b,c;

	int i ;
	printf("testing:    ");
	a = '7';

	Init_RS232();
	for (i = 0; i < 5; i++) {
		b=putcharRS232(i);
		c = getcharRS232();
		printf("getchar should be %d : %d\n", b,c);
	}
	while(1) {
		c = getcharRS232();
		printf("getchar: %d\n",c);
	}

}
