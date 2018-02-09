from org.csstudio.opibuilder.scriptUtil import PVUtil

remote_pv = widget.getPV();
setpoint_pv = PVUtil.createPV(widget.getProperty("pv_name")+":SP", widget)
current_value = int(PVUtil.getDouble(remote_pv))

if current_value == 0:
	setpoint_pv.setValue(1)
else:
	setpoint_pv.setValue(1)
