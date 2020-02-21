from check_OPI_format_utils.common import get_type_of_widget, get_position_of_widget


def get_widgets_outside_of_boundary(root, x_min=-15, x_max=850, y_min=-15, y_max=650):
    xpath = "//widget[x<{} or x>{} or y<{} or y>{}]".format(x_min, x_max, y_min, y_max)
    return [(widget.sourceline, get_type_of_widget(widget), get_position_of_widget(widget))
            for widget in root.xpath(xpath)]

