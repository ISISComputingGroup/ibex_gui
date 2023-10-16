# This script sets the enabled and transparent property of the widget based on script PV value at index 0

from org.csstudio.opibuilder.scriptUtil import PVUtil

enabled = PVUtil.getDouble(pvs[0]) == 0

widget.setEnabled(enabled)
widget.setPropertyValue("transparent", not enabled)
