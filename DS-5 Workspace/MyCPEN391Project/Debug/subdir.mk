################################################################################
# Automatically-generated file. Do not edit!
################################################################################

# Add inputs and outputs from these tool invocations to the build variables 
C_SRCS += \
../RS232.c \
../ledAndSwitch.c \
../main.c \
../wifi.c 

OBJS += \
./RS232.o \
./ledAndSwitch.o \
./main.o \
./wifi.o 

C_DEPS += \
./RS232.d \
./ledAndSwitch.d \
./main.d \
./wifi.d 


# Each subdirectory must supply rules for building sources it contributes
%.o: ../%.c
	@echo 'Building file: $<'
	@echo 'Invoking: ARM C Compiler 5'
	armcc --c99 -O0 -g --md --depend_format=unix_escaped -c -o "$@" "$<"
	@echo 'Finished building: $<'
	@echo ' '


