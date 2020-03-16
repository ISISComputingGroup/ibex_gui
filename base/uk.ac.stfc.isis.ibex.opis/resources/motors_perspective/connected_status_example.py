from org.csstudio.opibuilder.scriptUtil import PVUtil, ConsoleUtil

# If connected write to internal PV

if ( PVUtil.getSeverityString(pvs[0]) == -1 ):  # Disconnected
	PVUtil.writePV("loc://motor_0101_connected", 0)
	ConsoleUtil.writeInfo("Main: Set local pv to 0")
else:
	PVUtil.writePV("loc://motor_0101_connected", 1)
	ConsoleUtil.writeInfo("Main: Set local pv to 1")
