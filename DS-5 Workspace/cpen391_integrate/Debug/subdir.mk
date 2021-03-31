################################################################################
# Automatically-generated file. Do not edit!
################################################################################

# Add inputs and outputs from these tool invocations to the build variables 
CPP_SRCS += \
../Bird.cpp \
../CPEN391.cpp \
../Game.cpp \
../GameState.cpp \
../MainMenuState.cpp \
../Pipe.cpp \
../SplashState.cpp \
../StateMachine.cpp 

OBJS += \
./Bird.o \
./CPEN391.o \
./Game.o \
./GameState.o \
./MainMenuState.o \
./Pipe.o \
./SplashState.o \
./StateMachine.o 

CPP_DEPS += \
./Bird.d \
./CPEN391.d \
./Game.d \
./GameState.d \
./MainMenuState.d \
./Pipe.d \
./SplashState.d \
./StateMachine.d 


# Each subdirectory must supply rules for building sources it contributes
%.o: ../%.cpp
	@echo 'Building file: $<'
	@echo 'Invoking: ARM C++ Compiler 5'
	armcc --cpp -O0 -g --md --depend_format=unix_escaped -c -o "$@" "$<"
	@echo 'Finished building: $<'
	@echo ' '


