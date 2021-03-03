from random import seed
from random import random
import serial
import time
ser = serial.Serial("/dev/tty.hc01comHC-05-DevB",115200)

print(ser.name)
print(ser.isOpen())
seed(1)
count = 0
while 1:
    
    print(random())
    time.sleep(random())
    count = count + 1
    print(count," write:1")
    ser.write('1'.encode())


