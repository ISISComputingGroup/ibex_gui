from org.csstudio.opibuilder.scriptUtil import PVUtil, ColorFontUtil

##############################################
#
# pvs[0-19] = CHNL:XXX:READ
# pvs[20-39] = CHNL:XXX:DRIFT
# pvs[40-59] = CHNL:XXX:TEMP
#
##############################################


table = widget.getTable()
row = 1
color = ColorFontUtil.getColorFromRGB(240,245,245)
i = 0
for i in range(20):
    reading = PVUtil.getString(pvs[i]) 
    table.setCellText(row, 3, reading)
    
    if row%2 != 0:
        for col in range(0,4):
            table.setCellBackground(row, col, color)

    if not pvs[i].isConnected():
    table.setCellText(row, 1, "Disconnected")

    row+=1

	
	