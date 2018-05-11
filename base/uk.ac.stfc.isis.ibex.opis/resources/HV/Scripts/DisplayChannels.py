from org.csstudio.opibuilder.scriptUtil import PVUtil
from org.csstudio.opibuilder.scriptUtil import WidgetUtil
from org.csstudio.opibuilder.scriptUtil import ConsoleUtil
from Utilities import get_cleared_group_widget, get_channels
from time import sleep

# PVs
# pv[0] = $(P)CAEN:crates, triggered
# pv[1] = $(P)CAEN:CHANLIST, triggered
# pv[2-17] = $(P)CAEN:crates.[XX]ST

# Values to control the search length for all channels (must match the values in UpdateChannels)

def get_channel_pv_name(crate, slot, channel):
    """
    Gets the PV name for a given channel

    Args:
        crate: The name of the crate
        slot: The slot number
        channel: The channel number

    Returns:
        The PV name for accessing the given channel
    """
    return crate + ":" + str(slot) + ":" + str(channel)

def create_channel_target(crate, slot, channel):
    """
    Creates a widget to allow selection of which channels are included in the summary list.

    Args:
        crate: The name of the crate
        slot: The slot number
        channel: The channel number

    Returns:
        The target widget
    """
    target = WidgetUtil.createWidgetModel("org.csstudio.opibuilder.widgets.linkingContainer")
    target.setPropertyValue("opi_file", "HVChannelSummaryMaintenance.opi")
    target.setPropertyValue("auto_size", "true")
    target.setPropertyValue("zoom_to_fit", "false")
    target.setPropertyValue("border_style", 0)
    avail = get_channel_pv_name(crate, slot, channel)
    target.setPropertyValue("name", avail)
    target.addMacro("SEL", avail)
    return target


def get_available_crates():
    max_crate = 2
    max_slot = 5
    max_chan = 10

    for crate_name_pv in pvs[2:2+max_crate]:
        try:
            crate = PVUtil.getString(crate_name_pv)
        except Exception, e:
            ConsoleUtil.writeError("Unable to get crate name for PV: " + crate_name_pv + " , error: " + str(e))
            continue

        if len(crate) == 0:
            continue

        for slot in range(max_slot):
            for channel in range(max_chan):
                yield crate, slot, channel

def add_channel_widgets():
    channel_selector_widget = get_cleared_group_widget(display)
    current_included_channels = get_channels(pvs[1], PVUtil.getString, ConsoleUtil.writeError)

    for crate, slot, channel in get_available_crates():
        channel_widget = create_channel_target(crate, slot, channel)
        channel_selector_widget.addChildToBottom(channel_widget)
        container = display.getWidget(channel_widget.getPropertyValue("name"))

        # Check if the channel is in in the list, and check the box if it is. This assumes the naming convention of
        # channel widgets remains name=widget_name
        if get_channel_pv_name(crate, slot, channel) in current_included_channels:
            container.getChild("Include").setValue(1)


if __name__ == "__main__":
    add_channel_widgets()
