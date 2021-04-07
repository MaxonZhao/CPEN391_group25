################################################################################
# Automatically-generated file. Do not edit!
################################################################################

# Add inputs and outputs from these tool invocations to the build variables 
CPP_SRCS += \
../game_logic/Bird.cpp \
../game_logic/CPEN391.cpp \
../game_logic/Game.cpp \
../game_logic/GameOverState.cpp \
../game_logic/GameState.cpp \
../game_logic/MainMenuState.cpp \
../game_logic/Pipe.cpp \
../game_logic/StateMachine.cpp 

OBJS += \
./game_logic/Bird.o \
./game_logic/CPEN391.o \
./game_logic/Game.o \
./game_logic/GameOverState.o \
./game_logic/GameState.o \
./game_logic/MainMenuState.o \
./game_logic/Pipe.o \
./game_logic/StateMachine.o 

CPP_DEPS += \
./game_logic/Bird.d \
./game_logic/CPEN391.d \
./game_logic/Game.d \
./game_logic/GameOverState.d \
./game_logic/GameState.d \
./game_logic/MainMenuState.d \
./game_logic/Pipe.d \
./game_logic/StateMachine.d 


# Each subdirectory must supply rules for building sources it contributes
game_logic/%.o: ../game_logic/%.cpp
	@echo 'Building file: $<'
	@echo 'Invoking: ARM C++ Compiler 5'
	armcc --cpp11 -O0 -g --md --depend_format=unix_escaped --no_depend_system_headers --depend_dir="game_logic" -c -o "$@" "$<"
	@echo 'Finished building: $<'
	@echo ' '


