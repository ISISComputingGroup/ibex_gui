from org.csstudio.opibuilder.scriptUtil import PVUtil
from org.csstudio.opibuilder.scriptUtil import ConsoleUtil


container = display.getWidget("Tabs")

# enable tabs
tab_count = int(container.getPropertyValue("tab_count"))
for i in range(tab_count):

	if pvArray[i + 1].isConnected():
		container.setPropertyValue("tab_%s_enabled" % i, "true")
	else:
		container.setPropertyValue("tab_%s_enabled" % i, "false")

# activate asked for tab
try:
    active_tab = int(pvs[0].getValue().value)
except (IndexError, AttributeError, TypeError, ValueError):
    active_tab = 1
    ConsoleUtil.writeInfo('Could not get value from macro for active tab')

if active_tab < 1 or active_tab > tab_count:
    ConsoleUtil.writeInfo('Active tab is out of range was ' + str(active_tab))
    active_tab = 1

container.setActiveTabIndex(active_tab - 1)
