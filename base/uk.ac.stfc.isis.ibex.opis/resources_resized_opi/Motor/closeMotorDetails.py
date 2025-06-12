from org.csstudio.opibuilder.scriptUtil import PVUtil
from org.csstudio.opibuilder.scriptUtil import ConsoleUtil

overviewBtn = display.getWidget('OverviewButton')

pv0 = pvs[0].getValue()
pv0Str = str(pv0.value)
if pv0Str == 'No':
    overviewBtn.getPropertyValue('actions').getActionsList()[0].run()

