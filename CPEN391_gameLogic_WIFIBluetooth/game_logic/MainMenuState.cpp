/*
 * MainMenuState.cpp
 *
 *  Created on: Mar 24, 2021
 *      Author: patri
 */


#include "MainMenuState.h"
#include "../RS232/serial.h"
#include <iostream>
#include <cstring>



namespace GameLogic {
	MainMenuState::MainMenuState(StateMachine* data) {
		this->_data = data;
	}

	void MainMenuState::Init() {
		// There is nothing to be initiated
//		this->_colorChanged = clock();
		this->_animationIterator = 0;
		// this->_chosenBirdColorIndex = 0;

//		this->_birdColors[0] = 0; 		//black
//		this->_birdColors[1] = 0x30;	//red
//		this->_birdColors[2] = 0x38;	//orange
//		this->_birdColors[3] = 0xC;		//green
//		this->_birdColors[4] = 0x3C;	//yellow
//		this->_birdColors[5] = 0x3;		//blue

		this->readyToStart = 0;

		//send a hello message to the App to indicate de1 is ready for receiving game setting message
		sendMessage("hello",BT_LineStatusReg, BT_TransmitterFifo);

		// 1. draw the background
		*(RENDER_BASE + 4) = 0x5F;
		*(RENDER_BASE + 6) = 0x4F;

		// 2. draw the "flappy bird" text
		*(RENDER_BASE + 4) = 19;
		*(RENDER_BASE + 1) = 160;
		*(RENDER_BASE + 2) = 60;
		*(RENDER_BASE + 6) = 0x05;
	}

	void MainMenuState::HandleInput() {

			bytes_received = receiveBuffer(buffer, BT_LineStatusReg, BT_ReceiverFifo);

			if(bytes_received != 0 && !this->readyToStart){

				switch(buffer[0]){
					case 'g':
						this->_data->PlayInGuestMode = true; //guest mode, no need to upload Score to Database later
						break;
					case 'p':
						this->_data->PlayInGuestMode = false; //played as a login player, need to upload Score to Database later
						break;
					default:
						return;
				}

				switch(buffer[1]){
					case 'e':
						this->_data->pipe_spawn_frequency = 400;
						break;
					case 'm':
						this->_data->pipe_spawn_frequency = 300;
						break;
					case 'h':
						this->_data->pipe_spawn_frequency = 200;
						break;
					default:
						return;

				}

				char inputColor[3];
				std::memcpy( inputColor, &buffer[2], 2 );
				inputColor[2] = '\0';


				if(std::strcmp(inputColor,"bk")==0){
					this->_birdColor = 0;
				}else if(std::strcmp(inputColor,"re")==0){
					this->_birdColor = 0x30;
				}else if(std::strcmp(inputColor,"or")==0){
					this->_birdColor = 0x38;
				}else if(std::strcmp(inputColor,"gr")==0){
					this->_birdColor = 0xC;
				}else if(std::strcmp(inputColor,"ye")==0){
					this->_birdColor = 0x3C;
				}else if(std::strcmp(inputColor,"bu")==0){
					this->_birdColor = 0x3;
				}else{
					return;
				}

				this->readyToStart = 1;

				//send an acknowledgement to the Android App
				sendMessage("OK",BT_LineStatusReg, BT_TransmitterFifo);

			}else if(bytes_received != 0 && this->readyToStart){

				std::memcpy(this->_data->userName, &buffer, 16);
				_data->AddState(new GameState(_data));
			}


///////////////////////////////////////////////////////////////////////////////////////////////////////
//			if(*PUSHBUTTONS == 14){
//				// start the game.
//				_data->AddState(new GameState(_data));
//			}else if (*PUSHBUTTONS == 13){
//				// change the color of the bird
//				if(this->_colorChanged + COLORCHANGEDDURATION < clock()){
//					this->_chosenBirdColorIndex++;
//					this->_colorChanged = clock();
//				}
//
//			}

	}

	void MainMenuState::Update(float dt) {

	}

	void MainMenuState::Draw() {
		// TODO:
		// 1. draw the background
		*(RENDER_BASE + 8) = 0x1F;
		*(RENDER_BASE + 4) = 21;
		*(RENDER_BASE + 1) = 160;
		*(RENDER_BASE + 2) = 120;
		*(RENDER_BASE + 6) = 0x4F;



		// 2. draw the bird texture:
		int a = (this->_animationIterator++) % 4 + 1;
		*(RENDER_BASE + 4) = a;
		*(RENDER_BASE + 1) = 160;
		*(RENDER_BASE + 2) = 120;
		*(RENDER_BASE + 7) = this->_birdColor;
		*(RENDER_BASE + 6) = 0x05;
	}
}

