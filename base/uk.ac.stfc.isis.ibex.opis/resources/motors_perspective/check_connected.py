from org.csstudio.opibuilder.scriptUtil import PVUtil, ConsoleUtil

if (PVUtil.getLong(pvs[0]) == 1):
	widget.setPropertyValue("opi_file", "single_motor_standard_view.opi", True)
	ConsoleUtil.writeInfo("Sub: Set opi to standard view")
else:
	widget.setPropertyValue("opi_file", "single_motor_view_disconnected.opi", True)
	ConsoleUtil.writeInfo("Sub: Set opi to disconnected view")