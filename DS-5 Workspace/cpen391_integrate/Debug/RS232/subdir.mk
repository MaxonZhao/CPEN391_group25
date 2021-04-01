################################################################################
# Automatically-generated file. Do not edit!
################################################################################

# Add inputs and outputs from these tool invocations to the build variables 
CPP_SRCS += \
../RS232/RS232.cpp 

OBJS += \
./RS232/RS232.o 

CPP_DEPS += \
./RS232/RS232.d 


# Each subdirectory must supply rules for building sources it contributes
RS232/%.o: ../RS232/%.cpp
	@echo 'Building file: $<'
	@echo 'Invoking: ARM C++ Compiler 5'
	armcc --cpp -O0 -g --md --depend_format=unix_escaped -c -o "$@" "$<"
	@echo 'Finished building: $<'
	@echo ' '


