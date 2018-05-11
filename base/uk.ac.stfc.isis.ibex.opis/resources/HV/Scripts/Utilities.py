def get_cleared_group_widget(this_display):
    """
    Gets the widget called 'group' and clears its children.

    Args:
        this_display: The CSS display component

    Returns:
        The group widget having had its children removed

    """
    group_widget = this_display.getWidget('group')
    group_widget.removeAllChildren()
    return group_widget


def get_summary_channels(channel_list_pv, get_string_from_pv, log):
    """
    Gets the list of channels from the HVCaen.

    Args:
        channel_list_pv: The PV containing the list of channels.
        get_string_from_pv: Function for extracting a string from a named PV
        log: Function accepting a string that is written to a log

    Returns:
        A list of available channels, or an empty list if getting the channels fails.
    """

    def channel_formatter(channel):
        formatted_channel = channel.replace(" ", "")
        if formatted_channel.endswith(','):
            formatted_channel = formatted_channel[:-1]
        return formatted_channel

    try:
        return [channel_formatter(chan) for chan in get_string_from_pv(channel_list_pv).split(' ')]
    except Exception, e:  # Jython slightly different to standard Python
        log("Unable to get channel list for HVCaen: " + str(e))  # Jython str has no 'format'
        return list()
