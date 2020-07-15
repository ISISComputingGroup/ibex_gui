from org.csstudio.opibuilder.scriptUtil import PVUtil
from org.csstudio.opibuilder.scriptUtil import ConsoleUtil

if pvArray[0].isConnected():
    widget.setPropertyValue('visible', True)
else:
    widget.setPropertyValue('visible', False)
