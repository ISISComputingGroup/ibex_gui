from ChannelUtilities import get_summary_channels
from OPIUtilities import get_cleared_group_widget, create_channel_widget_model

# PVs
# pv[0] = $(P)CAEN:CHANLIST


def create_channels_summary():
    """
    Clears the summary widget and repopulates it with the widgets for the current channel summary.

    Returns:
        None
    """
    group = get_cleared_group_widget(display)
    for chan in get_summary_channels(pvs[0]):
        group.addChildToBottom(create_channel_widget_model(chan, True))


if __name__ == "__main__":
    create_channels_summary()
