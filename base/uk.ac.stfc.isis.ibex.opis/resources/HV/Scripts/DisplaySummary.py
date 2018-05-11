from org.csstudio.opibuilder.scriptUtil import PVUtil
from org.csstudio.opibuilder.scriptUtil import WidgetUtil
from org.csstudio.opibuilder.scriptUtil import ConsoleUtil
from Utilities import get_cleared_group_widget, get_summary_channels

# PVs
# pv[0] = $(P)CAEN:CHANLIST


def create_summary_widget(channel):
    """
    Creates a widget model to display the summary information for a given channel.

    Args:
        channel: The channel to display summary information for

    Returns:
        The widget model used to construct the channel summary
    """
    target = WidgetUtil.createWidgetModel("org.csstudio.opibuilder.widgets.linkingContainer")
    target.setPropertyValue('opi_file', "HVChannelMonitorSummary.opi")
    target.setPropertyValue('auto_size','true')
    target.setPropertyValue('zoom_to_fit','false')
    target.setPropertyValue('border_style',0)
    target.addMacro('SEL',channel)
    return target


def create_channels_summary():
    """
    Clears the summary widget and repopulates it with the widgets for the current channel summary.

    Returns:
        None
    """
    group = get_cleared_group_widget(display)
    for chan in get_summary_channels(pvs[0], PVUtil.getString, ConsoleUtil.writeError):
        group.addChildToBottom(create_summary_widget(chan))


if __name__ == "__main__":
    create_channels_summary()
