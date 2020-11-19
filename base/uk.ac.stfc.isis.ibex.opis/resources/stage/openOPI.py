from org.csstudio.opibuilder.scriptUtil import PVUtil, ScriptUtil, DataUtil

macroInput = DataUtil.createMacrosInput(True);
axis_name_pv = widget.getPV()
macroInput.put("MM", PVUtil.getString(axis_name_pv));		
macroInput.put("P", "");
	
ScriptUtil.openOPI(widget, "../Motor/mymotor.opi", 0, macroInput);
