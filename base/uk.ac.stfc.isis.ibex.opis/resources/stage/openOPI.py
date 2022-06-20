from org.csstudio.opibuilder.scriptUtil import PVUtil, ScriptUtil, DataUtil

macroInput = DataUtil.createMacrosInput(True);
axis_name_pv = widget.getPV()
motor = PVUtil.getString(axis_name_pv)

#Get the pv_prefix by splitting on the 2nd colon
second_colon = motor.find(":", motor.find(":") + 1)
pv_prefix, motor_pv = motor[:second_colon + 1], motor[second_colon + 1:]

macroInput.put("MM", motor_pv);
macroInput.put("P", pv_prefix);
	
ScriptUtil.openOPI(widget, "../Motor/mymotor.opi", 0, macroInput);
