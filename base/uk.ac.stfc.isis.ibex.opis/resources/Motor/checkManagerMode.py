from org.csstudio.opibuilder.scriptUtil import PVUtil
from org.csstudio.opibuilder.scriptUtil import ConsoleUtil

moreDetailsBtn = display.getWidget('MoreDetailsButton')
pv0 = pvs[0].getValue()
pv0Str = str(pv0.value)
if pv0Str == 'No':
    moreDetailsBtn.setPropertyValue('enabled', False)
elif pv0Str == 'Yes':
    moreDetailsBtn.setPropertyValue('enabled', True)

