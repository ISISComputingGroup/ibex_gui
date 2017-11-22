from check_OPI_format_utils.common import WIDGET_XPATH, get_text_of_widget


def check_colour(root, widget, colour_type, conditions):
    """
    Checks that the colour of the supplied widget type matches the supplied conditions.
    Args:
        root (etree): The root of the xml to search.
        widget (str): The widget type to check.
        colour_type (str): The type of colour to look for (e.g. background_color, foreground_color etc).
        conditions (list): List of xpath condition strings to be satisfied.
    Returns:
        list: A list of tuples containing the line number and text of the widgets that do not conform to conditions
    """
    xpath = WIDGET_XPATH.format(widget)

    xpath = "//{}/{}/color[not(@name) or ({})]".format(xpath, colour_type, " and ".join(conditions))

    return [(error.sourceline, get_text_of_widget(error.getparent().getparent())) for error in root.xpath(xpath)]


def check_any_isis_colour(root, widget, colour_type):
    """
    Checks that the supplied colour type of the supplied widget is any ISIS specific colour.
    Args:
        root (etree): The root of the xml to search.
        widget (str): The widget type to check.
        colour_type (str): The type of colour to look for (e.g. background_color, foreground_color etc).
    Returns:
        list: A list of tuples containing the line number and text of the widgets that do not conform to conditions
    """
    return check_colour(root, widget, colour_type, ["not(starts-with(@name, 'ISIS_'))"])


def check_specific_isis_colours(root, widget, colour_type, colours):
    """
    Checks that the supplied colour type of the supplied widget is any ISIS specific colour.
    Args:
        root (etree): The root of the xml to search.
        widget (str): The widget type to check.
        colour_type (str): The type of colour to look for (e.g. background_color, foreground_color etc).
        colours (list): List of the colour strings to check against.
    Returns:
        list: A list of tuples containing the line number and text of the widgets that do not conform to conditions
    """
    return check_colour(root, widget, colour_type, ["@name!='{}'".format(colour) for colour in colours])


def check_plot_area_backgrounds(root):
    return [(node.sourceline, get_text_of_widget(node.getparent()))
            for node in root.xpath("//plot_area_background_color/color[not(@name) or not(starts-with(@name, 'ISIS_'))]")]
