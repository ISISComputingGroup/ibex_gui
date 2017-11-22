from check_OPI_format_utils.common import WIDGET_XPATH, get_text_of_widget

INCORRECT_FONT_XPATH = "//{label}/font/opifont.name[not(starts-with(., 'ISIS_')) and not(starts-with(@fontName, 'ISIS_'))]"\
    .format(label=WIDGET_XPATH.format("Label"))


def get_incorrect_fonts(root):
    """
    Gets any widgets that do not have the correct font.
    Args:
        root (etree): The root of the xml to search.
    Returns:
        list: A list of tuples containing the line number and text of the widgets that do not have the correct font.
    """
    return [(node.sourceline, get_text_of_widget(node.getparent().getparent())) for node in root.xpath(INCORRECT_FONT_XPATH)]
