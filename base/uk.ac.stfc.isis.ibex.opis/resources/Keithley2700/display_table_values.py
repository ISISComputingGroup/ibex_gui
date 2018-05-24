from org.csstudio.opibuilder.scriptUtil import PVUtil, ColorFontUtil

'''
pvs[0-19] = CHNL:XXX:READ
pvs[20-39] = CHNL:XXX:DRIFT
pvs[40-59] = CHNL:XXX:TEMP
'''

table = widget.getTable()
color = ColorFontUtil.getColorFromRGB(240, 245, 245)
number_of_channels = 20
temp_pv_index_offset = number_of_channels
drift_pv_index_offset = number_of_channels*2
temp_value_column = 1
drift_value_column = 2
reading_value_column = 3

for i in range(number_of_channels):
    reading = PVUtil.getString(pvs[i])
    temperature = PVUtil.getString(pvs[i + temp_pv_index_offset])
    drift = PVUtil.getString(pvs[i + drift_pv_index_offset])
    row = i+1
    table.setCellText(row, temp_value_column, temperature)
    table.setCellText(row, drift_value_column, drift)
    table.setCellText(row, reading_value_column, reading)

    # Create colour scheme for table
    if row % 2 != 0:
        for col in range(0, 4):
            table.setCellBackground(row, col, color)

    if not pvs[i].isConnected():
        table.setCellText(row, 1, "Disconnected")

	