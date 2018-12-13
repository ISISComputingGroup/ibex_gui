from org.csstudio.opibuilder.scriptUtil import PVUtil
from org.csstudio.opibuilder.scriptUtil import ConsoleUtil

pv0 = PVUtil.getString(pvs[0]);
widget.setPropertyValue("tooltip", pv0);
