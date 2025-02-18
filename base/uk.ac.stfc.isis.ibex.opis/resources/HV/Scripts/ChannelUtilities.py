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
            crate = get_string_from_pv(crate_name_pv)
        except Exception, e:
            log("Unable to get crate name for PV: " + crate_name_pv + " , error: " + str(e))
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

    channels = list()
    try:
        if channel_list_pv.getValue().getData().size() > 0:  # default method throws uncaught java exception if empty
    	    channels = [channel_formatter(chan) for chan in PVUtil.getStringArray(channel_list_pv)]
    except Exception, e:  # Jython slightly different to standard Python
        log("Unable to get channel list for HVCaen: " + str(e))  # Jython str has no 'format'
    return channels

def _get_max(this_display, macro, default_value, upper_limit=None):
    """

    Args:
        this_display: The display. The returned value should be available via a macro.
        macro: The macro containing the value
        default_value: The value to use if the macro isn't specified
        upper_limit: The absolute maximum value the property can take
        log: Where to send messages

    Returns:
        The value of the macro
    """
    max_value = this_display.getPropertyValue("macros").getMacrosMap().get(macro)
    
    try:
        max_value = int(max_value)
    except (TypeError, ValueError):
        max_value = default_value   
    
    if max_value is None:
        max_value = default_value

    if upper_limit is not None:
        max_value = min(max_value, upper_limit)

    return max_value


def get_max_crates(this_display):
    """
    The absolute maximum value is 15, determined by the limitations of EPICS MMBO records.

    Args:
        this_display: The display. The returned value should be available via a macro.

    Returns:
        The maximum number of crates supported by the OPI
    """
    return _get_max(this_display, "MAX_CRATES", 2, 15)


def get_max_slots(this_display):
    """
    Args:
        this_display: The display. The returned value should be available via a macro.

    Returns:
        The maximum number of slots supported by the OPI
    """
    return _get_max(this_display, "MAX_SLOTS", 5)


def get_max_channels(this_display):
    """
    Args:
        this_display: The display. The returned value should be available via a macro.

    Returns:
        The maximum number of channels supported by the OPI
    """
    return _get_max(this_display, "MAX_CHANNELS", 25)


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