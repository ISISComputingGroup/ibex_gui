from org.csstudio.opibuilder.scriptUtil import PVUtil

# PVs
# pv[] = $(P)CAEN:CHANLIST

# Get and clear the group container prior to display
group = display.getWidget('group')
group.removeAllChildren()

# Get the list of entries in the channel list
try:
    chanlist_string = PVUtil.getString(pvs[0])
except Exception:
    chanlist_string = ""
chanlist = chanlist_string.split(' ')

# Loop through the chan list and display channel information as appropriate
for chan in chanlist:
    if chan.endswith(","):
        chan = chan.replace(",","")
    if chan != '' and chan != ',':
        target = WidgetUtil.createWidgetModel("org.csstudio.opibuilder.widgets.linkingContainer")
        target.setPropertyValue('opi_file', "HVChannelMonitorSummary.opi")
        target.setPropertyValue('auto_size','true')
        target.setPropertyValue('zoom_to_fit','false')
        target.setPropertyValue('border_style',0)
        target.addMacro('SEL',chan)
        group.addChildToBottom(target)
