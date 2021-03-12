from PIL import Image

def texture_gen(filename):
    im = Image.open(filename, mode="r")
    pixelArray = im.load()
    xMax = im.size[0]
    yMax = im.size[1]

    processedString = []

    for x in range(xMax):
        for y in range (yMax):
            currPixel = pixelArray[x, y]
            red = currPixel[0] // 85
            green = currPixel[1] // 85
            blue = currPixel[2] // 85
            alpha = int(currPixel[3] > 10)

            processedString.append("    " + str(format(y + x * yMax, "03d")) + " : " + str(alpha) + str(format(red, "02b")) + str(format(green, "02b")) + str(format(blue, "02b")) + ";")

    for i in range(len(processedString)):
        print(processedString[i])

texture_gen("bird-01.png")
