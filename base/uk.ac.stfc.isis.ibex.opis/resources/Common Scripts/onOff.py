from org.csstudio.opibuilder.scriptUtil import PVUtil

pv = widget.getPV(); 
current_value = int(PVUtil.getDouble(pv))

if current_value == 0:
	pv.setValue(1)
else:
	pv.setValue(0)
