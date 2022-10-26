from org.csstudio.opibuilder.scriptUtil import PVUtil

mode = PVUtil.getString(pvs[1])

if pvs[3].getValue():
    mode += ",PULSE"

pvs[2].setValue(mode)