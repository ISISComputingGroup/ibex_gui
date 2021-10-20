from org.csstudio.opibuilder.scriptUtil import PVUtil
from jarray import array

# This script controls behaviour of graphs in the OPI

xStart = -1
xEnd = 60

# Multiply times 1,000,000 because scale of graph is in microseconds
multiplier = 1000000

def getVal(val):
    return round(float(val) * multiplier, 2)


def inRange(loc):
    return False if loc < xStart or loc > xEnd else True


def getRange(val):
    return min(max(xStart, val), xEnd)

x1 = getVal(PVUtil.getDouble(pvs[0]))
x2 = getVal(PVUtil.getDouble(pvs[1]))
polarity = PVUtil.getDouble(pvs[5])
polarity = -1 if polarity == 0 else 1

points = []

height = 0 if x1 >= xStart else polarity
points.append([xStart, height])
if inRange(x1) or inRange(x2):
    points.append([getRange(x1), 0])
    points.append([getRange(x1), polarity])
    height = polarity
if inRange(x2):
    points.append([getRange(x2), polarity])
    points.append([getRange(x2), 0])
    height = 0
points.append([xEnd, height])

xArray = []
yArray = []
for point in points:
    xArray.append(point[0])
    yArray.append(point[1])

pvs[2].setValue(array(xArray, 'd'))
pvs[3].setValue(array(yArray, 'd'))
