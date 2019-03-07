from org.csstudio.opibuilder.scriptUtil import PVUtil
from org.csstudio.opibuilder.scriptUtil import ConsoleUtil

onoff_group = display.getWidget("Power")
if pvArray[0].isConnected():
    onoff_group.setPropertyValue('visible', True)
else:
    onoff_group.setPropertyValue('visible', False)
