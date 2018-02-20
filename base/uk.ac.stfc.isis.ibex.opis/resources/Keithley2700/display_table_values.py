from org.csstudio.opibuilder.scriptUtil import PVUtil, ColorFontUtil

table = widget.getTable()
row = 1
color = ColorFontUtil.getColorFromRGB(240,245,245)

for pv in pvs:
    value = PVUtil.getString(pv)
    table.setCellText(row, 3, value)
    
    if row%2 != 0:
		for col in range(0,4):
			table.setCellBackground(row, col, color)
    
    if not pv.isConnected():
        table.setCellText(row, 1, "Disconnected")
	
    i+=1






	
	