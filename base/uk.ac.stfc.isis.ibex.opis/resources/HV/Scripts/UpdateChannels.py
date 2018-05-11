from org.csstudio.opibuilder.scriptUtil import PVUtil

# PVs
# pv[0] = $(P)CAEN:crates, triggered
# pv[1] = $(P)CAEN:CHANLIST, triggered
# pv[2-17] = $(P)CAEN:crates.[XX]ST

# Get the display objects and PVs for interaction
group = display.getWidget('group')
test = display.getWidget('test')
chanlistwidget = display.getWidget('update')
chanlistpv = chanlistwidget.getPV()

# Clear the list
newlist = ''

# Values to control the search length for all channels (must match the values in DisplayChannels)
maxcrate = 2
maxslot = 5
maxchan = 10

actioned = PVUtil.getDouble(pvs[0])

# Loop through the channels, and if selected add them to the list
if actioned == 1:
    for z in range(maxcrate):
        cratename = pvs[1+z]
        if cratename != "":
            crate = PVUtil.getString(cratename)
            for x in range(maxslot):
                for y in range(maxchan):
                    avail = crate + ':' + x + ':' + y
                    container = display.getWidget(avail)
                    inlistwidget = container.getChild('Include')
                    include = inlistwidget.getValue()
                    if include == 1:
                        newlist = newlist + ' ' + avail
else:
    newlist = 'Not looping'

# Update the PV value
chanlistpv.setValue(newlist)