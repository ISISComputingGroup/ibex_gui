from org.csstudio.opibuilder.scriptUtil import WidgetUtil

def get_cleared_group_widget(this_display, group_container_name):
    """
    Gets the widget called 'group' and clears its children.

    Args:
        this_display: The CSS display component
        group_container_name: the name of the grouping container to get

    Returns:
        The group widget having had its children removed

    """
    group_widget = this_display.getWidget(group_container_name)
    group_widget.removeAllChildren()
    return group_widget

def create_channel_widget_model(name, opi_file):
    """
    Creates a widget to display information about a given channel

    Args:
        name: Name of the channel
        opi_file: The OPI file template to use

    Returns:
        The target widget
    """
    model = WidgetUtil.createWidgetModel("org.csstudio.opibuilder.widgets.linkingContainer")
    model.setPropertyValue("opi_file", opi_file)
    model.setPropertyValue("auto_size", True)
    model.setPropertyValue("zoom_to_fit", False)
    model.setPropertyValue("border_style", 0)
    model.setPropertyValue("name", name)
    model.addMacro("SEL", name)
    return model