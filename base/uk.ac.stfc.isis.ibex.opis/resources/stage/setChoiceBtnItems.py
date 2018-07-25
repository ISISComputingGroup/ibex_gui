from org.csstudio.opibuilder.scriptUtil import PVUtil
from org.csstudio.opibuilder.scriptUtil import ConsoleUtil

inOutBtn = display.getWidget('InOutBtn')
value = pvs[0].getValue()



if value is None:
    values = ["No positions", "END"]
else:
	values = value.getData()
	
items = []
	
for i in range(len(values)):
	name = values[i]
	if name=="END":
		break
	if len(name)>0:
		items.append(name.strip())
		
inOutBtn.setPropertyValue('items', items)