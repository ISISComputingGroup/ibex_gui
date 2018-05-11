from org.csstudio.opibuilder.scriptUtil import PVUtil
from org.csstudio.opibuilder.scriptUtil import WidgetUtil
# from org.csstudio.opibuilder.scriptUtil import ConsoleUtil

# PVs
# pv[0] = $(P)CAEN:crates, triggered
# pv[1] = $(P)CAEN:CHANLIST, triggered
# pv[2-17] = $(P)CAEN:crates.[XX]ST

# Get and clear the group container prior to display
group = display.getWidget('group')
group.removeAllChildren()

# Get the list of entries in the channel list
try:
    fullchanlist_string = PVUtil.getString(pvs[1])
except Exception:
    fullchanlist_string = ""

# Split the chanlist into an array
fullchanlist = fullchanlist_string.split(' ')
chanlist = list()
testval = ''

# format the chanlist array for searching
for chan in fullchanlist:
    chan = chan.replace(" ", "")
    if chan.endswith(','):
        chan = chan.replace(",","")
    chanlist.append(chan)

# Values to control the search length for all channels (must match the values in UpdateChannels)
maxcrate = 2
maxslot = 5
maxchan = 10

# Loop through and generate an OPI linked to each channel displaying the name for inclusion in the chan list
for z in range(maxcrate):
    cratename = pvs[2+z]
    if cratename != "":
        crate = PVUtil.getString(cratename)
        for x in range(maxslot):
            for y in range(maxchan):
                avail = crate + ':' + x + ':' + y
                target = WidgetUtil.createWidgetModel("org.csstudio.opibuilder.widgets.linkingContainer")
                target.setPropertyValue('opi_file', "HVChannelSummaryMaintenance.opi")
                target.setPropertyValue('auto_size','true')
                target.setPropertyValue('zoom_to_fit','false')
                target.setPropertyValue('border_style',0)
                target.setPropertyValue('name',avail)
                target.addMacro('SEL',avail)
                group.addChildToBottom(target)
                container = display.getWidget(avail)
                # Check if the channel is in in the list, and check the bos if it is
                for chan in chanlist:
                    if avail == chan:
                        inlistwidget = container.getChild('Include')
                        inlistwidget.setValue(1)
