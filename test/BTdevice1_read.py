import serial
ser = serial.Serial("/dev/tty.hc01comHC-05-DevB",115200)
print(ser.name)
print("serial port is open:",ser.isOpen())

while 1:
    s = ser.readline() 
    print(s)

    