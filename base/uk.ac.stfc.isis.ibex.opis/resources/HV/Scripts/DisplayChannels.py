from org.csstudio.opibuilder.scriptUtil import PVUtil
from org.csstudio.opibuilder.scriptUtil import WidgetUtil
from org.csstudio.opibuilder.scriptUtil import ConsoleUtil
from Utilities import get_cleared_group_widget, get_summary_channels
from time import sleep

# PVs
# pv[0] = $(P)CAEN:crates, triggered
# pv[1] = $(P)CAEN:CHANLIST, triggered
# pv[2-17] = $(P)CAEN:crates.[XX]ST


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


def create_channel_model(crate, slot, channel):
    """
    Creates a widget to allow selection of which channels are included in the summary list.

    Args:
        crate: The name of the crate
        slot: The slot number
        channel: The channel number

    Returns:
        The target widget
    """
    model = WidgetUtil.createWidgetModel("org.csstudio.opibuilder.widgets.linkingContainer")
    model.setPropertyValue("opi_file", "HVChannelSummaryMaintenance.opi")
    model.setPropertyValue("auto_size", "true")
    model.setPropertyValue("zoom_to_fit", "false")
    model.setPropertyValue("border_style", 0)
    avail = get_channel_pv_name(crate, slot, channel)
    model.setPropertyValue("name", avail)
    model.addMacro("SEL", avail)
    return model


def get_available_channels():
    """
    Generator for the available Caen channels

    Yields:
        A tuple of crate (string), slot (int), channel (int)
    """
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
    """
    Clears the current group box displaying the available channels and repopulates it with the latest
    available channels.

    Returns:
        None
    """
    channel_selector_widget = get_cleared_group_widget(display)
    current_included_channels = get_summary_channels(pvs[1], PVUtil.getString, ConsoleUtil.writeError)

    for crate, slot, channel in get_available_channels():
        channel_model = create_channel_model(crate, slot, channel)
        channel_selector_widget.addChildToBottom(channel_model)

        # Check if the channel is in in the list, and check the box if it is
        if get_channel_pv_name(crate, slot, channel) in current_included_channels:
            channel_selector_widget.getChildren()[-1].getChild("Include").setValue(1)


if __name__ == "__main__":
    add_channel_widgets()
