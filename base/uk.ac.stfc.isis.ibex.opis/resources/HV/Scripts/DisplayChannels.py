from ChannelUtilities import get_summary_channels, get_available_channels, get_max_crates
from OPIUtilities import get_cleared_group_widget, create_channel_widget_model


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


def add_channel_widgets(this_display, this_pvs):
    """
    Clears the current group box displaying the available channels and repopulates it with the latest
    available channels.

    Args:
        this_display: The display that has called this script. Used to control modification of global CSS variables
        this_pvs: PVs passed to the script by CSS. Used to control modification of global CSS variables

    Returns:
        None
    """
    channel_selector_widget = get_cleared_group_widget(this_display)
    current_included_channels = get_summary_channels(this_pvs[1])

    for crate, slot, channel in get_available_channels(this_pvs[2:2+get_max_crates(this_display)], this_display):
        channel_selector_widget.addChildToBottom(
            create_channel_widget_model(get_channel_pv_name(crate, slot, channel), False))

        # Check if the channel is in in the list, and check the box if it is
        if get_channel_pv_name(crate, slot, channel) in current_included_channels:
            channel_selector_widget.getChildren()[-1].getChild("Include").setValue(1)


if __name__ == "__main__":
    # pv[0] = $(P)CAEN:crates, triggered
    # pv[1] = $(P)CAEN:CHANLIST, triggered
    # pv[2-17] = $(P)CAEN:crates.[XX]ST
    add_channel_widgets(display, pvs)
