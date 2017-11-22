from check_OPI_format_utils.common import WIDGET_XPATH, TYPE_ID, get_text_of_widget


WIDGET_OUTSIDE_CONTAINER_XPATH = "//widget[{{widget}} and not(ancestor::{groupingContainer})]" \
    .format(groupingContainer=WIDGET_XPATH.format("groupingContainer"))


def get_items_not_in_grouping_container(root, item):
    xpath = WIDGET_OUTSIDE_CONTAINER_XPATH.format(widget=TYPE_ID.format(item))
    return [(node.sourceline, get_text_of_widget(node)) for node in root.xpath(xpath)]
