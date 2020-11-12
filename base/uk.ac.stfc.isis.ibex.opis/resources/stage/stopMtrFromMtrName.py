from org.csstudio.opibuilder.scriptUtil import PVUtil

mtrname_pv = widget.getPV()
mtrname = PVUtil.getString(mtrname_pv)
PVUtil.writePV(mtrname + ".STOP", 1)
