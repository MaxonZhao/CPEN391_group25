import serial
ser = serial.Serial("/dev/tty.FLAPPY-BIRD-1-DevB",115200)
print(ser.name)
print("serial port is open:",ser.isOpen())

while 1:
    s = ser.readline() 
    print(s)

    