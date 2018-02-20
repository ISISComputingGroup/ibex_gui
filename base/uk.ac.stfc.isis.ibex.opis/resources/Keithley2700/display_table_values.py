from org.csstudio.opibuilder.scriptUtil import PVUtil

table = widget.getTable()
i = 1

for pv in pvs:
    value = PVUtil.getString(pv)
    table.setCellText(i, 3, value)
	
    if not pv.isConnected():
        table.setCellText(i, 1, "Disconnected")
	
    i+=1


table.setCellBorder(2)
