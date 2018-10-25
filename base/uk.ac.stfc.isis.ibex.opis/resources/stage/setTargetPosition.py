from org.csstudio.opibuilder.scriptUtil import PVUtil


indices = [0, 1, 2]
positions = PVUtil.getStringArray(pvs[0])


def get_visible(index):
    """
    Args:
        index: The index of the set point

    Returns: True if a motion set point with this index exists, False otherwise.
    """
    if not positions:
        return False
    elif len(positions) <= index + 1:
        return False
    return True


def set_macros(widget, index):
    """
    Sets the proper index and name macros on the LED indicator widgets for a given motion setpoint.
    Args:
        widget: The target widget
        index: The index of the motion set point
    """
    macros = widget.getPropertyValue("macros")
    macros.put("INDEX", str(index))
    macros.put("POS", positions[index].strip())
    widget.setPropertyValue("macros", macros)


def reload_widget(widget):
    """
    We have to reload the widget to update the macros. Done by un-setting and resetting the
    target OPI.
    """
    widget.setPropertyValue("opi_file", "null.opi")
    widget.setPropertyValue("opi_file", "position_LED.opi")

for i in indices:
    widget = display.getWidget("lnk_pos_{}".format(i))

    visible = get_visible(i)
    if visible:
        set_macros(widget, i)

    widget.setPropertyValue("visible", visible)
    reload_widget(widget)
