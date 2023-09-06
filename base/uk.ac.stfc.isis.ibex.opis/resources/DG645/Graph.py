from org.csstudio.opibuilder.scriptUtil import PVUtil
from jarray import array

# This script controls behaviour of graphs in the OPI

x_minimum = -1
x_maximum = 1
y_minimum = -1
y_maximum = 1

graph_multiplier_microseconds = 1000000

# Get global maximum x to sync all graphs' x axis
try:    
    x_maximum = PVUtil.getDouble(pvs[6])
except:
    pvs[6].setValue(x_maximum)

def get_value_scaled(val):
    return round(float(val) * graph_multiplier_microseconds, 2)


def check_in_range(location):
    return x_minimum < location < x_maximum


def get_value_range_clamped(val):
    return min(max(x_minimum, val), x_maximum)


def get_starting_y(x_value, current_polarity):
    return 0 if check_in_range(x_value) else current_polarity


x_start = get_value_scaled(PVUtil.getDouble(pvs[0]))
x_end = get_value_scaled(PVUtil.getDouble(pvs[1]))

if x_maximum < x_end:
    x_maximum = x_end + 1
    pvs[6].setValue(x_maximum)


# Boolean PV to be converted into y or -y
polarity = y_minimum if PVUtil.getDouble(pvs[5]) == 0 else y_maximum

points = []
current_y = get_starting_y(x_start, polarity)

# Graph start
points.append([x_minimum, current_y])
# Bump start
if check_in_range(x_start) or check_in_range(x_end):
    points.append([get_value_range_clamped(x_start), 0])
    points.append([get_value_range_clamped(x_start), polarity])
    current_y = polarity
# Bump end
if check_in_range(x_end):
    points.append([get_value_range_clamped(x_end), polarity])
    points.append([get_value_range_clamped(x_end), 0])
    current_y = 0
# Graph end
points.append([x_maximum, current_y])

xArray = [x for x, y in points]
yArray = [y for x, y in points]

pvs[2].setValue(array(xArray, 'd'))
pvs[3].setValue(array(yArray, 'd'))
