from org.csstudio.opibuilder.scriptUtil import PVUtil

auto_pid_pv = pvs[0].getValue().value

print(auto_pid_pv)

controls = [display.getWidget("P_setpoint"), display.getWidget("I_setpoint"), display.getWidget("D_setpoint")]

print(controls)

if auto_pid_pv == "OFF":
	print("OFF case")
	for control in controls:
		print(control)
		control.setPropertyValue("enabled", "false")
else:
	print("ON case")
	for control in controls:
		print(control)
		control.setPropertyValue("enabled", "false")
