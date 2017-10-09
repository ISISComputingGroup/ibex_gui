from org.csstudio.opibuilder.scriptUtil import PVUtil

directory = widget.getPV()

waveform = [""]

# Convert to readable string
for c in PVUtil.getLongArray(directory):
	if c == 0:
		break	
	waveform.append(chr(c))

directory = "".join(waveform)

rates = display.getWidget("ramp_table_rates").getPV()
rates = PVUtil.getDoubleArray(rates)

targets = display.getWidget("ramp_table_targets").getPV()
targets = PVUtil.getDoubleArray(targets)

table = display.getWidget("ramp_table").getTable()
rows = table.getRowCount()


index = 0
# Text File headers
text = "target rate"

f = open(directory, 'w')

for index in range(0, rows):
	
	if (float(table.getCellText(index,1)) > 0) and (float(table.getCellText(index,3)) > 0):
		# Set PVs
		targets[index] = float(table.getCellText(index, 1))
		rates[index] = float(table.getCellText(index, 3))
		
		# Build text string for ramp table file
		text = text + "\n%s %s" % (str(targets[index]), str(rates[index]))
		
		# Update Table with new PV values	
		table.setCellText(index, 1, str(targets[index]))
		table.setCellText(index, 3, str(rates[index]))	

	
f.write(text)

f.close()



