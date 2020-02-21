from check_OPI_format_utils.common import WIDGET_XPATH
"""
Check properties of the graph xy widget
"""

XY_GRAPH_WIDGETS_XPATH = "//{}".format(WIDGET_XPATH.format("xyGraph"))
BUFFER_SIZES_XPATH = "*[{0} = substring(name(), string-length(name()) - string-length({0}) +1) ]"\
    .format("'_buffer_size'")
TRIGGER_PV_XPATH = r"trigger_pv"
TIME_FORMAT_XPATH = r"axis_0_time_format"


def get_trigger_pv(root):
    """
    Gets any xy widget and checks it for a trigger PV
    Args:
        root (etree): The root of the xml to search.
    Returns:
        error list tuple: Error line
    """
    errors = []
    for graph in root.xpath(XY_GRAPH_WIDGETS_XPATH):
        # Check if it's a plot against time
        time_format_nodes = graph.xpath(TIME_FORMAT_XPATH)
        if not time_format_nodes or time_format_nodes[0].text == "0":
            continue
        trigger_nodes = graph.xpath(TRIGGER_PV_XPATH)
        if not trigger_nodes or not trigger_nodes[0].text:
            errors.append((graph.sourceline,))
    return errors


def get_traces_with_different_buffer_sizes(root):
    """
    Gets any graph xy widget and check it for buffer sizes different to the first.
    Args:
        root (etree): The root of the xml to search.
    Returns:
        error list tuple: Error line, buffer size and expected buffer size
    """
    errors = []
    for graph_node in root.xpath(XY_GRAPH_WIDGETS_XPATH):
        node_errors = _get_buffer_difference_in_single_graph(graph_node)
        errors.extend(node_errors)

    return errors


def _get_buffer_difference_in_single_graph(node):
    nodes = node.xpath(BUFFER_SIZES_XPATH)
    if len(nodes) == 0:
        return []

    buffer_size = nodes[0].text
    return [(node.sourceline, node.text, buffer_size) for node in nodes if node.text != buffer_size]
