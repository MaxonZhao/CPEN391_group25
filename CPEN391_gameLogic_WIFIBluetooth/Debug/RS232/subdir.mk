################################################################################
# Automatically-generated file. Do not edit!
################################################################################

# Add inputs and outputs from these tool invocations to the build variables 
CPP_SRCS += \
../RS232/RS232.cpp \
../RS232/wifi.cpp 

OBJS += \
./RS232/RS232.o \
./RS232/wifi.o 

CPP_DEPS += \
./RS232/RS232.d \
./RS232/wifi.d 


# Each subdirectory must supply rules for building sources it contributes
RS232/%.o: ../RS232/%.cpp
	@echo 'Building file: $<'
	@echo 'Invoking: ARM C++ Compiler 5'
	armcc --cpp11 -O0 -g --md --depend_format=unix_escaped --no_depend_system_headers --depend_dir="RS232" -c -o "$@" "$<"
	@echo 'Finished building: $<'
	@echo ' '


