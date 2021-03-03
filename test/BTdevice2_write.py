from random import seed
from random import random
import serial
import time
ser = serial.Serial("/dev/tty.hc01comHC-05-DevB-1",115200)
print(ser.name)
print(ser.isOpen())
seed(1)
count = 0
while 1:
    
    print(random())
    time.sleep(random())
    count = count + 1
    print(count," write:2")
    ser.write('2'.encode())


