from ChannelUtilities import get_summary_channels
from OPIUtilities import get_cleared_group_widget, create_channel_widget_model


def create_channels_summary(this_display, this_pvs):
    """
    Clears the summary widget and repopulates it with the widgets for the current channel summary.

    Args:
        this_display: The display that has called this script. Used to control modification of global CSS variables
        this_pvs: PVs passed to the script by CSS. Used to control modification of global CSS variables

    Returns:
        None
    """
    group = get_cleared_group_widget(this_display)
    for chan in get_summary_channels(this_pvs[0]):
        group.addChildToBottom(create_channel_widget_model(chan, True))


if __name__ == "__main__":
    # pv[0] = $(P)CAEN:CHANLIST
    create_channels_summary(display, pvs)
