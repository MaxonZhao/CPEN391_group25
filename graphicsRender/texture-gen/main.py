from PIL import Image

def texture_gen(filename, startAddr):
    im = Image.open(filename, mode="r")
    pixelArray = im.load()
    xMax = im.size[0]
    yMax = im.size[1]

    processedString = []

    for x in range(xMax):
        for y in range (yMax):
            currPixel = pixelArray[x, y]
            red = currPixel[0] / 85
            if red * 10 % 10 < 5:
                red = int(red)
            else:
                red = int(red) + 1

            green = currPixel[1] / 85
            if green * 10 % 10 < 5:
                green = int(green)
            else:
                green = int(green) + 1

            blue = currPixel[2] / 85
            if blue * 10 % 10 < 5:
                blue = int(blue)
            else:
                blue = int(blue) + 1
            alpha = int(currPixel[3] > 127)

            processedString.append("    " + str(format(startAddr + y + x * yMax, "05d")) + " : " + str(alpha) + str(format(red, "02b")) + str(format(green, "02b")) + str(format(blue, "02b")) + ";")

    for i in range(len(processedString)):
        print(processedString[i])

texture_gen("Gold-Medal.png", 7568)
