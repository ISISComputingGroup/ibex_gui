from java.lang.System import getProperty as getJavaSystemProperty

import sys
import os
resources_dir = getJavaSystemProperty("ibex.opis.resources_directory")
sys.path.append(os.path.join(resources_dir, "HV", "Scripts"))

from ChannelUtilities import get_available_channels, get_max_crates, get_channel_pv_name
from org.csstudio.opibuilder.scriptUtil import PVUtil


def update_channels(this_display, this_pvs):
    """
    Updates the channels available on the summary maintenance page

    Args:
        this_display: The display that has called this script. Used to control modification of global CSS variables
        this_pvs: PVs passed to the script by CSS. Used to control modification of global CSS variables

    Returns:
        None
    """
    # Loop through the channels, and if selected add them to the list
    actioned = PVUtil.getDouble(this_pvs[0]) == 1
    if actioned:

        # Generate the list of included channel names
        channel_names = list()
        for crate, slot, channel in get_available_channels(this_pvs[1:1 + get_max_crates(this_display)], this_display):
            channel_name = get_channel_pv_name(crate, slot, channel)
            if this_display.getWidget(channel_name).getChild('Include').getValue() == 1:
                channel_names.append(channel_name)

        # Create a PV value based on the channel names
        new_pv_value = " ".join(channel_names)
        this_display.getWidget('update').getPV().setValue(new_pv_value)


#The function called by CSS
update_channels(display, pvs)