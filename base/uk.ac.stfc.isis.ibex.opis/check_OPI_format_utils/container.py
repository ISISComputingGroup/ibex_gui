from check_OPI_format_utils.common import WIDGET_XPATH, TYPE_ID, get_text_of_widget


WIDGET_OUTSIDE_CONTAINER_XPATH = "//widget[{{widget}} and not(ancestor::{groupingContainer})]" \
    .format(groupingContainer=WIDGET_XPATH.format("groupingContainer"))


def get_items_not_in_grouping_container(root, widget_type):
    """
    Gets any widgets that are not in a grouping container.
    Args:
        root (etree): The root of the xml to search.
        widget_type (str): The type of widget to search for.
    Returns:
        list: A list of tuples containing the line number and text of the widgets that are not within groups
    """
    xpath = WIDGET_OUTSIDE_CONTAINER_XPATH.format(widget=TYPE_ID.format(widget_type))
    return [(node.sourceline, get_text_of_widget(node)) for node in root.xpath(xpath)]
