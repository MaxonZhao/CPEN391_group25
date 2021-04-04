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
		std::cout<<"Hi New!"<<std::endl;
	}

	void MainMenuState::Init() {
		// There is nothing to be initiated
		this->_clock  = clock();
		this->_colorChanged = clock();
		this->_animationIterator = 0;
		this->_chosenBirdColorIndex = 0;

		this->_birdColors[0] = 0; 		//black
		this->_birdColors[1] = 0x30;	//red
		this->_birdColors[2] = 0x38;	//orange
		this->_birdColors[3] = 0xC;		//green
		this->_birdColors[4] = 0x3C;	//yellow
		this->_birdColors[5] = 0x3;		//blue

		this->readyToStart = 0;

		//send a hello message to the App to indicate de1 is ready for receiving game setting message
		sendMessage("hello",BT_LineStatusReg, BT_TransmitterFifo);
	}

	void MainMenuState::HandleInput() {
		if(*PUSHBUTTONS == 14){
			// start the game.
			_data->AddState(new GameState(_data));
		}else if (*PUSHBUTTONS == 13){
			// change the color of the bird
			if(this->_colorChanged + COLORCHANGEDDURATION < clock()){
				std::cout<<"changing!"<<std::endl;
				this->_chosenBirdColorIndex++;
				this->_colorChanged = clock();
			}

		}


		int bytes_received = 0;
		char buffer[16];
		bytes_received = receiveBuffer(buffer, BT_LineStatusReg, BT_ReceiverFifo);

		// std::cout << "received:" << buffer <<std::endl;
		// std::cout << "bytes_received:" << bytes_received <<std::endl;

		if(bytes_received != 0 && !this->readyToStart){
		
			char LoginStatus[2];
			std::memcpy( LoginStatus, &buffer[0], 1 );
			LoginStatus[1] = '\0';
			std::cout<< "LoginStatus: "<<LoginStatus <<std::endl;
			if(std::strcmp(LoginStatus,"g")==0){
				this->PlayInGuestMode = 1; //guest mode, no need to upload Score to Database later
			}else if(std::strcmp(LoginStatus,"p")==0){
				this->PlayInGuestMode = 0; //played as a login player, need to upload Score to Database later
			}else{
					//receive invalid data
					return;	
			}

			//TODO: setting gaming diffuculty level
			//diffucultyLevel is a fake variable
			char inputDifficultyLevel[2];
			std::memcpy( inputDifficultyLevel, &buffer[1], 1 );
			inputDifficultyLevel[1] = '\0';
	
			std::cout<< "inputDifficultyLevel: "<<inputDifficultyLevel <<std::endl;
			// if(std::strcmp(inputDifficultyLevel,"e")==0){
			// 	diffucultyLevel = 0; //easy
			// }else if(std::strcmp(inputDifficultyLevel,"m")==0){
			// 	diffucultyLevel = 1; //medium
			// }else if(std::strcmp(inputDifficultyLevel,"h")==0){
			// 	diffucultyLevel = 2; //hard
			// }else{
			// 	//receive invalid difficulty setting code
			// 	readyToStart = 0;
			// 	return;
			// }
		
			// change the color of the bird
			if(this->_colorChanged + COLORCHANGEDDURATION < clock()){
		
				char inputColor[3];
				std::memcpy( inputColor, &buffer[2], 2 );
				inputColor[2] = '\0';
		
				std::cout<< "inputColor: "<< inputColor <<std::endl;
				if(std::strcmp(inputColor,"bk")==0){
					this->_chosenBirdColorIndex = 0;
				}else if(std::strcmp(inputColor,"re")==0){
					this->_chosenBirdColorIndex = 1;
				}else if(std::strcmp(inputColor,"or")==0){
					this->_chosenBirdColorIndex = 2;
				}else if(std::strcmp(inputColor,"gr")==0){
					this->_chosenBirdColorIndex = 3;
				}else if(std::strcmp(inputColor,"ye")==0){
					this->_chosenBirdColorIndex = 4;
				}else if(std::strcmp(inputColor,"bu")==0){
					this->_chosenBirdColorIndex = 5;
				}else{
					//receive invalid color code
					return;	
				}
				
				this->_colorChanged = clock();
				this->readyToStart = 1;

				//send an acknowledgement to the Android App
				sendMessage("OK",BT_LineStatusReg, BT_TransmitterFifo);
			}
		}
		else if(bytes_received != 0 && this->readyToStart){
			//TODO: buffer contains the username, store the username at somewhere
			// ??? = buffer;

			// start the game.
			std::cout << "receive signal to start the game(username):" << buffer <<std::endl;
			_data->AddState(new GameState(_data));

	
		}
		
		// if(*PUSHBUTTONS == 14){
		// 	// start the game.
		// 	_data->AddState(new GameState(_data));
		// }else if (*PUSHBUTTONS == 13){
		// 	// change the color of the bird
		// 	if(this->_colorChanged + COLORCHANGEDDURATION < clock()){
		// 		std::cout<<"changing!"<<std::endl;
		// 		this->_chosenBirdColorIndex++;
		// 		this->_colorChanged = clock();
		// 	}

		// }

	}

	void MainMenuState::Update(float dt) {
		// there is nothing to be update in the mainmenu state.
//		if (this->_clock + SPLASH_STATE_SHOW_TIME < clock()) {
//			_data->AddState(new GameState(_data));
//		}
		// do nothing?


	}

	void MainMenuState::Draw(float dt) {
		// TODO:
		// 1. draw the background
		*(RENDER_BASE + 4) = 0x6A;
		*(RENDER_BASE + 6) = 0x4F;


		// 2. draw the "flappy bird" text
		*(RENDER_BASE + 3) = 0;
		*(RENDER_BASE + 4) = 19;
		*(RENDER_BASE + 1) = 160;
		*(RENDER_BASE + 2) = 60;
		*(RENDER_BASE + 6) = 0x05;


		// 3. draw the bird texture:
		*(RENDER_BASE + 4) = (this->_animationIterator++) % 4 + 1;
		*(RENDER_BASE + 1) = 160;
		*(RENDER_BASE + 2) = 120;
		*(RENDER_BASE + 7) = this->_birdColors[this->_chosenBirdColorIndex % 6];
		*(RENDER_BASE + 6) = 0x05;



	}
}

