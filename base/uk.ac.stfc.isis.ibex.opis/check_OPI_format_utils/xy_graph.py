"""
Check properties of the graph xy widget
"""

XY_GRAPH_WIDGETS_XPATH = r"//widget['org.csstudio.opibuilder.widgets.xyGraph']"
BUFFER_SIZES_XPATH = "*[{0} = substring(name(), string-length(name()) - string-length({0}) +1) ]"\
    .format("'_buffer_size'")


def get_traces_with_different_buffer_sizes(root):
    """
    Gets any graph xy widget and check it for buffer sizes different to the first.
    Args:
        root (etree): The root of the xml to search.
    Returns:
        error list tuple: Error line, buffer size and expected buffer size
    """
    graph_nodes = root.xpath(XY_GRAPH_WIDGETS_XPATH)
    errors = []
    for graph_node in graph_nodes:
        node_errors = _get_buffer_difference_in_single_graph(graph_node)
        errors.extend(node_errors)

    return errors


def _get_buffer_difference_in_single_graph(node):
    nodes = node.xpath(BUFFER_SIZES_XPATH)
    if len(nodes) == 0:
        return []

    buffer_size = nodes[0].text
    return [(node.sourceline, node.text, buffer_size) for node in nodes if node.text != buffer_size]
