from org.csstudio.opibuilder.scriptUtil import PVUtil
from org.csstudio.opibuilder.scriptUtil import ConsoleUtil

#ConsoleUtil.writeInfo('list positions')

targetPV = display.getWidget('SetPointText').getPV()
sourceBtn = display.getWidget('sourceBtn') 

menu = display.getWidget('SetPosnBtn')
value = pvs[0].getValue()    
    
if value is None:
    values = ["No positions", "END"]
else:
    values = value.getData()
#ConsoleUtil.writeInfo('count: ' + str(len(values)))

actionList = menu.getPropertyValue('actions').getActionsList()
actionClass = sourceBtn.getPropertyValue('actions').getActionsList()[0].__class__
actionList.clear()

for i in range(len(values)):
	name = values[i]
	if name=="END":
		break
	if len(name)>0:
		action = actionClass()
		action.setPropertyValue('pv_name', targetPV.getName())
		action.setPropertyValue('description', name)
		action.setPropertyValue('value', name)
		actionList.add(action)
