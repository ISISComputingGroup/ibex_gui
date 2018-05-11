from org.csstudio.opibuilder.scriptUtil import PVUtil
from org.csstudio.opibuilder.scriptUtil import ConsoleUtil


def get_available_channels(crate_name_pvs, this_display,
                           get_string_from_pv=PVUtil.getString, log=ConsoleUtil.writeError):
    """
    Generator for the available Caen channels

    Args:
        crate_name_pvs: The PVs containing the names of the crates
        this_display: The display that contains the macros for the maximum number of crates, slots and channels
        get_string_from_pv: Method used to get strings from PVs
        log: Method to send strings to the logs

    Yields:
        A tuple of crate (string), slot (int), channel (int)
    """
    for crate_name_pv in crate_name_pvs:
        try:
            crate = PVUtil.getString(crate_name_pv)
        except Exception, e:
            ConsoleUtil.writeError("Unable to get crate name for PV: " + crate_name_pv + " , error: " + str(e))
            continue

        if len(crate) == 0:
            continue

        for slot in range(get_max_slots(this_display)):
            for channel in range(get_max_channels(this_display)):
                yield crate, slot, channel


def get_summary_channels(channel_list_pv, get_string_from_pv=PVUtil.getString, log=ConsoleUtil.writeError):
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


def get_max_crates(this_display):
    """
    The absolute maximum value is 15, determined by the limitations of EPICS MMBO records.

    Args:
        The display. The value should be available via a macro.

    Returns:
        The maximum number of crates supported by the OPI
    """
    absolute_maximum = 15  # The crates are controlled by an MBBO which can only address up to 15 crates
    return min(2, absolute_maximum)


def get_max_slots(this_display):
    """
    Args:
        The display. The value should be available via a macro.

    Returns:
        The maximum number of slots supported by the OPI
    """
    return 5


def get_max_channels(this_display):
    """
    Args:
        The display. The value should be available via a macro.

    Returns:
        The maximum number of channels supported by the OPI
    """
    return 10

