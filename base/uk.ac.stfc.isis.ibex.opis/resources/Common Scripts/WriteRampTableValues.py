from org.csstudio.opibuilder.scriptUtil import PVUtil
from org.csstudio.opibuilder.scriptUtil import ColorFontUtil


table = display.getWidget("ramp_table").getTable()

rates = display.getWidget("ramp_table_rates").getPV()
targets = display.getWidget("ramp_table_targets").getPV()

row = 1
col = 1
index = 0

rates = PVUtil.getDoubleArray(rates)
targets = PVUtil.getDoubleArray(targets)

color = ColorFontUtil.getColorFromRGB(240,245,245)


while targets[index] > 0:
	table.setCellText(index, 0, "up to (Gauss)")	
	table.setCellText(index, 1, str(targets[index]))
	table.setCellText(index, 2, "Rate (A/s)")
	table.setCellText(index, 3, str(rates[index]))
	
	if index%2 != 0:
		for i in range(0,4):
			table.setCellBackground(index, i, color)
		
	index += 1



def remove_zero_rows():
	rows = table.getRowCount()
	for x in range(0, rows):
		
		if float(table.getCellText(x,1)) == 0:
			table.deleteRow(x)
			if x < rows:
				remove_zero_rows()
			
remove_zero_rows()
			

	
	


   
	



	
	
	
 

