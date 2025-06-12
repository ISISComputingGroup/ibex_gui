from org.csstudio.opibuilder.scriptUtil import ConsoleUtil
from java.lang.System import getProperty as getJavaSystemProperty

import sys
import os
resources_dir = getJavaSystemProperty("ibex.opis.resources_directory")
sys.path.append(os.path.join(resources_dir, "HV", "Scripts"))
from ChannelUtilities import get_summary_channels
from DisplayUtilities import get_cleared_group_widget, create_channel_widget_model


def create_channels_summary(this_display, this_pvs, group_container_name, opi_name):
    """
    Clears the summary widget and repopulates it with the widgets for the current channel summary.

    Args:
        this_display: The display that has called this script. Used to control modification of global CSS variables
        this_pvs: PVs passed to the script by CSS. Used to control modification of global CSS variables
        group_container_name: the name of the grouping container to populate
        opi_name: the opi to populate with

    Returns:
        None
    """ 
    group = get_cleared_group_widget(this_display, group_container_name)
    for chan in get_summary_channels(this_pvs[0]):
        group.addChildToBottom(create_channel_widget_model(chan, opi_name))

#The function that is called by CSS.
create_channels_summary(display, pvs, "group", "HVChannelMonitorSummary.opi")