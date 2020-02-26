# Type ID
TYPE_ID = "@typeId='org.csstudio.opibuilder.widgets.{}'"

#  Start of the XPATH for a widget
WIDGET_XPATH = "widget[{}]".format(TYPE_ID)


def get_text_of_widget(widget_node):
    """
    Returns a human readable representation of the given widget node.
    Args:
        widget_node (xml node): The node for the widget.
    Returns:
        str: A text representation of the widget
    """
    if len(widget_node.xpath("text")):
        return widget_node.xpath("text")[0].text
    if len(widget_node.xpath("name")):
        return widget_node.xpath("name")[0].text
    return ""


def get_type_of_widget(widget_node):
    """
    Returns a human readable representation of the given widget nodes type.
        widget_node (xml node): The node for the widget.
    Returns:
        str: A text representation of the widgets type
    """
    widget_type = widget_node.xpath("widget_type")[0].text
    return widget_type if len(widget_type) else ""


def get_position_of_widget(widget_node):
    """
    Returns the x, y position of the given widget node.
    Args:
        widget_node (xml node): The node for the widget.
    Returns:
        cords (list): x, y representation of the widgets positon
    """
    cords = [widget_node.xpath("x")[0].text, widget_node.xpath("y")[0].text]
    return cords
