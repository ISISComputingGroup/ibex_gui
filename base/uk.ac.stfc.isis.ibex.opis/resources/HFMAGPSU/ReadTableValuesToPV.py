from org.csstudio.opibuilder.scriptUtil import PVUtil

rates = display.getWidget("ramp_table_rates").getPV()
targets = display.getWidget("ramp_table_targets").getPV()

rates_values = PVUtil.getDoubleArray(rates)
targets_values = PVUtil.getDoubleArray(targets)

for i in range(0, (len(rates_values)-1)):
	rates_values[i] = 0
	targets_values[i] = 0

table = display.getWidget("ramp_table").getTable()
rows = table.getRowCount()

for index in range(0, rows):	
	# If table rows contain values that aren't zero
	if (float(table.getCellText(index,1)) > 0) and (float(table.getCellText(index,3)) > 0):
		# Set new array values
		targets_values[index] = float(table.getCellText(index, 1))
		rates_values[index] = float(table.getCellText(index, 3))	


# Set new Values to PV


targets.setValue(targets_values)
rates.setValue(rates_values)

