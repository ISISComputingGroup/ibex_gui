from org.csstudio.opibuilder.scriptUtil import WidgetUtil
from Utilities import get_cleared_group_widget, get_summary_channels, get_available_channels, get_max_crates

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


def add_channel_widgets():
    """
    Clears the current group box displaying the available channels and repopulates it with the latest
    available channels.

    Returns:
        None
    """
    channel_selector_widget = get_cleared_group_widget(display)
    current_included_channels = get_summary_channels(pvs[1])

    for crate, slot, channel in get_available_channels(pvs[2:2+get_max_crates()]):
        channel_model = create_channel_model(crate, slot, channel)
        channel_selector_widget.addChildToBottom(channel_model)

        # Check if the channel is in in the list, and check the box if it is
        if get_channel_pv_name(crate, slot, channel) in current_included_channels:
            channel_selector_widget.getChildren()[-1].getChild("Include").setValue(1)


if __name__ == "__main__":
    add_channel_widgets()
