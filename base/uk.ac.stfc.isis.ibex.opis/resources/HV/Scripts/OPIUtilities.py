from org.csstudio.opibuilder.scriptUtil import WidgetUtil


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


def create_channel_widget_model(name, is_summary):
    """
    Creates a widget to display information about a given channel

    Args:
        name: Name of the channel
        is_summary: Is this a summary? If so use the summary OPI template, else use the maintenance template

    Returns:
        The target widget
    """
    model = WidgetUtil.createWidgetModel("org.csstudio.opibuilder.widgets.linkingContainer")
    model.setPropertyValue("opi_file",
                           "HVChannelMonitorSummary.opi" if is_summary else "HVChannelSummaryMaintenance.opi")
    model.setPropertyValue("auto_size", "true")
    model.setPropertyValue("zoom_to_fit", "false")
    model.setPropertyValue("border_style", 0)
    model.setPropertyValue("name", name)
    model.addMacro("SEL", name)
    return model
