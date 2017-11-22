# Start of the XPATH for a widget
WIDGET_XPATH = "widget[@typeId='org.csstudio.opibuilder.widgets.{}']"


def check_colour(colour_type, root, widget, colours):
    """
    Checks that the colour of the supplied widget type matches those in the supplied list of colours.
    Args:
        root (etree): The root of the xml to search
        widget (str): The widget type to check
        colours (list): A list of colours to check against
    Returns:
        list: A list of tuples containing the line number and text of the widgets that do not conform to the correct
              colour
    """
    xpath = WIDGET_XPATH.format(widget)

    subconditions = ["@name!='{}'".format(colour) for colour in colours]

    condition = "//{}/{}/color[not(@name) or ({})]".format(xpath, colour_type, " and ".join(subconditions))

    errors = []
    for error in root.xpath(condition):
        parent = error.getparent().getparent()
        try:
            text = parent.xpath("text")[0].text
        except IndexError:
            text = parent.xpath("name")[0].text

        errors.append((error.sourceline, text))

    return errors
