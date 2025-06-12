from org.csstudio.opibuilder.scriptUtil import PVUtil
from org.csstudio.opibuilder.scriptUtil import ConsoleUtil

value = PVUtil.getString(pvs[0])
units = PVUtil.getString(pvs[1])
widget.setValue(value + " " + units)
