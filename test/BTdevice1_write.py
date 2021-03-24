from random import seed
from random import random
import serial
import time
ser = serial.Serial("/dev/tty.FLAPPY-BIRD-1-DevB",115200)

print(ser.name)
print(ser.isOpen())
seed(1)
count = 0

while 1:
    
    print(random()*2)
    time.sleep(random()*2)
    count = count + 1
    print(count," write:1")
    ser.write('1'.encode())


