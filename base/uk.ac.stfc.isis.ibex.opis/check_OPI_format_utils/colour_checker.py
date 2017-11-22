# Start of the XPATH for a widget
WIDGET_XPATH = "widget[@typeId='org.csstudio.opibuilder.widgets.{}']"


def check_colour(root, widget, colours):
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

    condition = "/background_color/color[not(@name) or " \
                "(@name!='ISIS_Label_Background' and @name!='ISIS_Title_Background_NEW')]"

    return [(error.sourceline, error.getparent().xpath("text")) for error in root.xpath(xpath + condition)]