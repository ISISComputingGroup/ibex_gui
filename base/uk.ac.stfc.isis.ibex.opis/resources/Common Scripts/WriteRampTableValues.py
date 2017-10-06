from org.csstudio.opibuilder.scriptUtil import PVUtil


table = widget.getTable()

row = 1
col = 1
index = 0

table.setCellText(1, 1, "boop")

targets = PVUtil.getDoubleArray(pvs[0])
rates = PVUtil.getDoubleArray(pvs[1])

while targets[index] > 0:
	table.setCellText(index, 0, "up to (Gauss)")
	table.setCellText(index, 1, str(targets[index]))
	table.setCellText(index, 2, "Rate (A/s)")
	table.setCellText(index, 3, str(rates[index]))
	index += 1


	
	
	
 

