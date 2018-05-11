from ChannelUtilities import get_summary_channels, get_available_channels, get_max_crates
from OPIUtilities import get_cleared_group_widget, create_channel_widget_model

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


def add_channel_widgets():
    """
    Clears the current group box displaying the available channels and repopulates it with the latest
    available channels.

    Returns:
        None
    """
    channel_selector_widget = get_cleared_group_widget(display)
    current_included_channels = get_summary_channels(pvs[1])

    for crate, slot, channel in get_available_channels(pvs[2:2+get_max_crates(display)], display):
        channel_model = create_channel_widget_model(get_channel_pv_name(crate, slot, channel), False)
        channel_selector_widget.addChildToBottom(channel_model)

        # Check if the channel is in in the list, and check the box if it is
        if get_channel_pv_name(crate, slot, channel) in current_included_channels:
            channel_selector_widget.getChildren()[-1].getChild("Include").setValue(1)


if __name__ == "__main__":
    add_channel_widgets()
