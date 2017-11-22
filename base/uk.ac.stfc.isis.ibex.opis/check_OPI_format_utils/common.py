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
