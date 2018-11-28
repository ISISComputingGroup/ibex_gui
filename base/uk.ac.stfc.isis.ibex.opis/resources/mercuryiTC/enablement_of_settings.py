from org.csstudio.opibuilder.scriptUtil import PVUtil

auto_pid_pv = pvs[0].getValue().value
auto_gas_pv = pvs[1].getValue().value
auto_heater_pv = pvs[2].getValue().value


pid_controls = [display.getWidget("P_setpoint"), display.getWidget("I_setpoint"), display.getWidget("D_setpoint")]

if auto_pid_pv == "ON":
    for control in pid_controls:
        control.setPropertyValue("enabled", "false")
        control.setPropertyValue("transparent", "true")

elif auto_pid_pv == "OFF":
    for control in pid_controls:
        control.setPropertyValue("enabled", "true")
        control.setPropertyValue("transparent", "false")


gas_controls = [display.getWidget("Flow_setpoint")]

if auto_gas_pv == "Automatic":
    for control in gas_controls:
        control.setPropertyValue("enabled", "false")
        control.setPropertyValue("transparent", "true")

elif auto_gas_pv == "Manual":
    for control in gas_controls:
        control.setPropertyValue("enabled", "true")
        control.setPropertyValue("transparent", "false")


heater_controls = [display.getWidget("Output_setpoint")]

if auto_heater_pv == "Automatic":
    for control in heater_controls:
        control.setPropertyValue("enabled", "false")
        control.setPropertyValue("transparent", "true")

elif auto_heater_pv == "Manual":
    for control in heater_controls:
        control.setPropertyValue("enabled", "true")
        control.setPropertyValue("transparent", "false")
