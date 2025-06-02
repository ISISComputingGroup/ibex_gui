import unittest
from hamcrest import *
from lxml import etree
import sys

from check_OPI_format_utils.colour_checker import check_specific_isis_colours, check_any_isis_colour, \
    check_plot_area_backgrounds
from check_OPI_format_utils.position import get_widgets_outside_of_boundary
from check_OPI_format_utils.text import check_label_punctuation, check_container_names,\
    check_label_case_inside_containers, check_label_case_outside_containers
from check_OPI_format_utils.container import get_items_not_in_grouping_container
from check_OPI_format_utils.font import get_incorrect_fonts
from check_OPI_format_utils.xy_graph import get_traces_with_different_buffer_sizes, get_trigger_pv

WIDGET_XML = '<widget typeId="org.csstudio.opibuilder.widgets.{type}" version="1.0">{widget_internals}</widget>'

LABEL_WITH_TEXT_XML = WIDGET_XML.format(type='Label', widget_internals='<text>{text}</text>')

LABEL_WITH_FONT_XML = WIDGET_XML.format(type='Label', widget_internals='<font>' \
          '<opifont.name fontName="{font_name}" height="18" style="1">{font_text}</opifont.name>' \
          '</font>')

GROUP_CONTAINER_XML = WIDGET_XML.format(type='groupingContainer', widget_internals='<name>{name}</name>{containing_widget}')


def get_xml_for_widget_with_colour(widget, colour_type, colour_name=None):
    colour_str = '<color {} red="0" green="255" blue="0" />'
    colour_str = colour_str.format("" if colour_name is None else 'name="{}"'.format(colour_name))

    return WIDGET_XML.format(type=widget, widget_internals='<{colour_type}>' \
          '{colour_str}' \
          '</{colour_type}>').format(colour_type=colour_type, colour_str=colour_str)


def get_xml_for_widget_with_position(widget, x_position, y_position):
    return WIDGET_XML.format(type=widget, widget_internals="<x>{}</x>"
                                                           "<y>{}</y>"
                                                           "<widget_type>{}</widget_type>")\
                                                           .format(x_position, y_position, widget)


def make_widget_with_x_y_position(widget, x_position, y_position):
    return etree.fromstring(get_xml_for_widget_with_position(widget, x_position, y_position))


def make_widget_with_colour(widget, colour_type, colour_name=None):
    return etree.fromstring(get_xml_for_widget_with_colour(widget, colour_type, colour_name))


def make_label_in_grouping_container(text):
    return etree.fromstring(GROUP_CONTAINER_XML.format(name="", containing_widget=LABEL_WITH_TEXT_XML.format(text=text)))


def make_label_outside_grouping_container(text):
    xml = WIDGET_XML.format(type='notAgroupingContainer', widget_internals=LABEL_WITH_TEXT_XML.format(text=text))
    return etree.fromstring(xml)


def make_grouping_container(name):
    return etree.fromstring(GROUP_CONTAINER_XML.format(name=name, containing_widget=""))


def make_grouping_container_in_tabbed_container(grouping_colour):
    xml = '<widget typeId="org.csstudio.opibuilder.widgets.tab" version="1.0">{}</widget>' \
        .format(GROUP_CONTAINER_XML.format(name="", containing_widget=""))
    return etree.fromstring(xml)


def make_label_with_font(font_name, font_text):
    return etree.fromstring(LABEL_WITH_FONT_XML.format(font_name=font_name, font_text=font_text))


class TestCheckOpiFormatMethods(unittest.TestCase):

    def test_that_if_a_widget_with_valid_position_is_parsed_it_causes_no_errors(self):
        # Arrange
        widget, x, y = "LED", "10", "10"
        root = make_widget_with_x_y_position(widget, x, y)

        # Act
        errors = get_widgets_outside_of_boundary(root)

        # Assert
        self.assertEqual(len(errors), 0)

    def test_that_if_a_widget_with_invalid_position_is_parsed_it_causes_errors(self):
        # Arrange
        widget, x, y = "LED", "-100", "10"
        root = make_widget_with_x_y_position(widget, x, y)

        # Act
        errors = get_widgets_outside_of_boundary(root)

        # Assert
        self.assertNotEqual(len(errors), 0)

    def test_that_if_a_valid_led_tag_is_parsed_it_causes_no_errors(self):
        # Arrange
        widget, colour_type, colour = "LED", "on_color", "ISIS_Green_LED_On"
        root = make_widget_with_colour(widget, colour_type, colour)

        # Act
        errors = check_specific_isis_colours(root, widget, colour_type, [colour])

        # Assert
        self.assertEqual(len(errors), 0)

    def test_that_if_an_on_led_tag_without_a_named_colour_is_parsed_it_causes_one_error(self):
        # Arrange
        widget, colour_type = "LED", "on_color"
        root = make_widget_with_colour(widget, colour_type)

        # Act
        errors = check_specific_isis_colours(root, widget, colour_type, ["ISIS_Green_LED_On"])

        # Assert
        self.assertEqual(len(errors), 1)

    def test_that_if_an_on_led_tag_with_an_incorrect_named_colour_is_parsed_it_causes_one_error(self):
        # Arrange
        widget, colour_type, colour = "LED", "on_color", "THIS IS MADE UP"
        root = make_widget_with_colour(widget, colour_type, colour)

        # Act
        errors = check_specific_isis_colours(root, widget, colour_type, ["ISIS_Green_LED_On"])

        # Assert
        self.assertEqual(len(errors), 1)

    def test_that_if_an_off_led_tag_without_a_named_colour_is_parsed_it_causes_one_error(self):
        # Arrange
        widget, colour_type = "LED", "off_color"
        root = make_widget_with_colour(widget, colour_type)

        # Act
        errors = check_specific_isis_colours(root, widget, colour_type, ["ISIS_Green_LED_Off"])

        # Assert
        self.assertEqual(len(errors), 1)

    def test_that_if_an_off_led_tag_with_an_incorrect_named_colour_is_parsed_it_causes_one_error(self):
        # Arrange
        widget, colour_type, colour = "LED", "off_color", "THIS IS MADE UP"
        root = make_widget_with_colour(widget, colour_type, colour)

        # Act
        errors = check_specific_isis_colours(root, widget, colour_type, ["ISIS_Green_LED_Off"])

        # Assert
        self.assertEqual(len(errors), 1)

    def test_that_if_a_label_tag_with_a_valid_font_is_parsed_it_causes_no_errors(self):
        # Arrange
        root = make_label_with_font("Segoe UI", "ISIS_Header1_NEW")

        # Act
        errors = get_incorrect_fonts(root)

        # Assert
        self.assertEqual(len(errors), 0)

    def test_that_if_a_label_tag_with_a_valid_font_in_the_fontname_attribute_is_parsed_it_causes_no_errors(self):
        # Arrange
        root = make_label_with_font("ISIS_VALID_FONT", "MADE UP FONT")

        # Act
        errors = get_incorrect_fonts(root)

        # Assert
        self.assertEqual(len(errors), 0)

    def test_that_if_a_label_tag_with_an_invalid_font_is_parsed_it_causes_one_error(self):
        # Arrange
        root = make_label_with_font("Segoe UI", "MADE UP FONT")

        # Act
        errors = get_incorrect_fonts(root)

        # Assert
        self.assertEqual(len(errors), 1)

    def test_that_if_a_plot_area_with_an_valid_background_colour_is_parsed_it_causes_no_errors(self):
        # Arrange
        xml = '<plot_area_background_color>' \
              '<color name="ISIS_Something_valid" red="255" green="255" blue="255" />' \
              '</plot_area_background_color>'
        root = etree.fromstring(xml)

        # Act
        errors = check_plot_area_backgrounds(root)

        # Assert
        self.assertEqual(len(errors), 0)

    def test_that_if_a_plot_area_with_an_invalid_background_colour_is_parsed_it_causes_one_error(self):
        # Arrange
        xml = '<plot_area_background_color>' \
              '<color name="not_anything_valid" red="255" green="255" blue="255" />' \
              '</plot_area_background_color>'
        root = etree.fromstring(xml)

        # Act
        errors = check_plot_area_backgrounds(root)

        # Assert
        self.assertEqual(len(errors), 1)

    def test_that_if_a_textbox_with_a_valid_background_colour_is_parsed_it_causes_no_errors(self):
        # Arrange
        widget, colour_type, colour = "TextInput", "background_color", "ISIS_Textbox_Background"
        root = make_widget_with_colour(widget, colour_type, colour)

        # Act
        errors = check_specific_isis_colours(root, widget, colour_type, [colour])

        # Assert
        self.assertEqual(len(errors), 0)

    def test_that_if_a_textbox_with_an_invalid_background_colour_is_parsed_it_causes_one_error(self):
        # Arrange
        widget, colour_type, colour = "TextInput", "background_color", "invalid"
        root = make_widget_with_colour(widget, colour_type, colour)

        # Act
        errors = check_specific_isis_colours(root, widget, colour_type, ["ISIS_Textbox_Background"])

        # Assert
        self.assertEqual(len(errors), 1)

    def test_that_if_a_textbox_with_no_named_background_colour_is_parsed_it_causes_one_error(self):
        # Arrange
        widget, colour_type = "TextInput", "background_color"
        root = make_widget_with_colour(widget, colour_type)

        # Act
        errors = check_specific_isis_colours(root, widget, colour_type, ["ISIS_Textbox_Background"])

        # Assert
        self.assertEqual(len(errors), 1)

    def test_that_if_a_textbox_with_a_valid_border_colour_is_parsed_it_causes_no_errors(self):
        # Arrange
        widget, colour_type, colour = "TextInput", "border_color", "ISIS_something"
        root = make_widget_with_colour(widget, colour_type, colour)

        # Act
        errors = check_any_isis_colour(root, widget, colour_type)

        # Assert
        self.assertEqual(len(errors), 0)

    def test_that_if_a_textbox_with_an_invalid_border_colour_is_parsed_it_causes_one_error(self):
        # Arrange
        widget, colour_type, colour = "TextInput", "border_color", "invalid"
        root = make_widget_with_colour(widget, colour_type, colour)

        # Act
        errors = check_any_isis_colour(root, widget, colour_type)

        # Assert
        self.assertEqual(len(errors), 1)

    def test_that_if_a_textbox_with_no_named_border_colour_is_parsed_it_causes_one_error(self):
        # Arrange
        widget, colour_type = "TextInput", "border_color"
        root = make_widget_with_colour(widget, colour_type)

        # Act
        errors = check_any_isis_colour(root, widget, colour_type)

        # Assert
        self.assertEqual(len(errors), 1)

    def test_that_if_a_textbox_with_a_valid_foreground_colour_is_parsed_it_causes_no_errors(self):
        # Arrange
        widget, colour_type, colour = "TextInput", "foreground_color", "ISIS_Standard_Text"
        root = make_widget_with_colour(widget, colour_type, colour)

        # Act
        errors = check_specific_isis_colours(root, widget, colour_type, [colour])

        # Assert
        self.assertEqual(len(errors), 0)

    def test_that_if_a_textbox_with_an_invalid_foreground_colour_is_parsed_it_causes_one_error(self):
        # Arrange
        widget, colour_type, colour = "TextInput", "foreground_color", "invalid"
        root = make_widget_with_colour(widget, colour_type, colour)

        # Act
        errors = check_specific_isis_colours(root, widget, colour_type, ["ISIS_Standard_Text"])

        # Assert
        self.assertEqual(len(errors), 1)

    def test_that_if_a_textbox_with_no_named_foreground_colour_is_parsed_it_causes_one_error(self):
        # Arrange
        widget, colour_type = "TextInput", "foreground_color"
        root = make_widget_with_colour(widget, colour_type)

        # Act
        errors = check_specific_isis_colours(root, widget, colour_type, ["ISIS_Standard_Text"])

        # Assert
        self.assertEqual(len(errors), 1)

    def test_that_if_a_push_button_is_defined_within_a_grouping_container_it_causes_no_errors(self):
        # Arrange
        xml = WIDGET_XML.format(type="groupingContainer", widget_internals=WIDGET_XML.format(type='NativeButton', widget_internals=''))
        root = etree.fromstring(xml)

        # Act
        errors = get_items_not_in_grouping_container(root, "NativeButton")

        # Assert
        self.assertEqual(len(errors), 0)

    def test_that_if_a_push_button_is_defined_outside_a_grouping_container_it_causes_one_error(self):
        # Arrange

        xml = WIDGET_XML.format(type='NativeButton', widget_internals='')
        root = etree.fromstring(xml)

        # Act
        errors = get_items_not_in_grouping_container(root, "NativeButton")

        # Assert
        self.assertEqual(len(errors), 1)

    def test_that_a_grouping_container_with_a_title_case_name_causes_no_errors(self):
        # Arrange
        root = make_grouping_container("This is a Group Box")

        # Act
        errors = check_container_names(root)

        # Assert
        self.assertEqual(len(errors), 0)

    def test_that_a_grouping_container_with_a_no_capitalisation_causes_one_error(self):
        # Arrange
        root = make_grouping_container("this is not capitalised properly")

        # Act
        errors = check_container_names(root)

        # Assert
        self.assertEqual(len(errors), 1)

    def test_that_a_label_with_a_properly_capitalised_name_causes_no_errors(self):
        # Arrange
        root = make_label_in_grouping_container("Some text goes here")

        # Act
        errors = check_label_case_inside_containers(root)

        # Assert
        self.assertEqual(len(errors), 0)

    def test_that_a_label_with_a_properly_capitalised_name_and_a_capitalised_abbreviation_causes_no_errors(self):
        # Arrange
        root = make_label_in_grouping_container("This is an OPI")

        # Act
        errors = check_label_case_inside_containers(root)

        # Assert
        self.assertEqual(len(errors), 0)

    def test_that_a_label_with_a_short_first_word_that_is_not_capitalised_causes_one_error(self):
        # Arrange
        root = make_label_in_grouping_container("a label this is")

        # Act
        errors = check_label_case_inside_containers(root)

        # Assert
        self.assertEqual(len(errors), 1)

    def test_that_a_label_with_a_badly_capitalized_name_causes_one_error(self):

        # Arrange
        root = make_label_in_grouping_container("VOLTAGE should not have been in block capitals")

        # Act
        errors = check_label_case_inside_containers(root)

        # Assert
        self.assertEqual(len(errors), 1)

    def test_that_a_label_with_a_colon_at_the_end_causes_no_errors(self):
        # Arrange
        root = make_label_in_grouping_container("Voltage:")

        # Act
        errors = check_label_punctuation(root)

        # Assert
        self.assertEqual(len(errors), 0)

    def test_that_a_label_with_no_colon_at_the_end_causes_one_error(self):
        # Arrange
        root = make_label_in_grouping_container("Voltage")

        # Act
        errors = check_label_punctuation(root)

        # Assert
        self.assertEqual(len(errors), 1)

    def test_that_a_label_with_ellipsis_at_the_end_causes_no_errors(self):
        # Arrange
        root = make_label_in_grouping_container("Voltage...")

        # Act
        errors = check_label_punctuation(root)

        # Assert
        self.assertEqual(len(errors), 0)

    def test_that_if_a_label_has_only_a_number_as_text_then_it_causes_no_errors(self):
        # Arrange
        root = make_label_in_grouping_container("123")

        # Act
        errors = check_label_punctuation(root)

        # Assert
        self.assertEqual(len(errors), 0)

    def test_that_if_a_label_contains_an_ignored_word_then_it_causes_no_errors(self):
        # Arrange
        root = make_label_in_grouping_container("ISIS")

        # Act
        errors = check_label_punctuation(root)

        # Assert
        self.assertEqual(len(errors), 0)

    def test_that_if_a_label_outside_a_grouping_container_is_capitalised_in_title_case_it_causes_no_errors(self):
        # Arrange
        root = make_label_outside_grouping_container("This Is In Proper Case And Should Not Throw An Error")

        # Act
        errors = check_label_case_outside_containers(root)

        # Assert
        self.assertEqual(len(errors), 0)

    def test_that_if_a_label_outside_a_grouping_container_is_not_capitalised_in_title_case_it_causes_one_error(self):
        # Arrange
        root = make_label_outside_grouping_container("This is not in title case and should cause an error")

        # Act
        errors = check_label_case_outside_containers(root)

        # Assert
        self.assertEqual(len(errors), 1)

    def test_that_if_a_graph_has_different_buffer_sizes_then_error(self):
        # Arrange
        point0 = "1000"
        point1 = "1000"
        point2 = "100"
        trace0 = "trace1"
        trace1 = "trace2"
        trace2 = "trace3"
        xml = WIDGET_XML.format(type='xyGraph', widget_internals="""
                    <trace_0_buffer_size>{point0}</trace_0_buffer_size>
                    <trace_0_name>{trace0}</trace_0_name>
                    <trace_1_buffer_size>{point1}</trace_1_buffer_size>
                    <trace_1_name>{trace1}</trace_1_name>
                    <trace_2_buffer_size>{point2}</trace_2_buffer_size>
                    <trace_2_name>{trace2}</trace_2_name>
              """.format(point0=point0, point1=point1, point2=point2, trace0=trace0, trace1=trace1, trace2=trace2))
        root = etree.fromstring(xml)

        # Act
        errors = get_traces_with_different_buffer_sizes(root)

        # Assert
        assert_that(errors, has_length(1), "number of errors")
        line_number, buffer_size, expected_size = errors[0]
        assert_that(line_number, is_(6), "line number")
        assert_that(buffer_size, is_(point2), "buffer size")
        assert_that(expected_size, is_(point0), "expected buffer size")

    def test_that_if_there_are_two_graphs_with_different_buffer_sizes_there_is_no_error(self):
        # Arrange
        point0 = "1000"
        point1 = "100"
        xml = """<root>
                <widget typeId="org.csstudio.opibuilder.widgets.xyGraph" version="1.0">
                    <trace_0_buffer_size>{point0}</trace_0_buffer_size>
                    <trace_1_buffer_size>{point0}</trace_1_buffer_size>
                </widget>
                <widget typeId="org.csstudio.opibuilder.widgets.xyGraph" version="1.0">
                    <trace_0_buffer_size>{point1}</trace_0_buffer_size>
                    <trace_1_buffer_size>{point1}</trace_1_buffer_size>
                </widget>
                </root>
              """.format(point0=point0, point1=point1)
        root = etree.fromstring(xml)

        # Act
        errors = get_traces_with_different_buffer_sizes(root)

        # Assert
        assert_that(errors, has_length(0))

    def test_that_if_a_graph_has_an_empty_trigger_pv_then_error(self):
        # Arrange
        root = etree.fromstring("""
                <widget typeId="org.csstudio.opibuilder.widgets.xyGraph" version="1.0">
                    <trigger_pv></trigger_pv>
                    <axis_0_time_format>1</axis_0_time_format>
                </widget>
              """)

        # Act
        errors = get_trigger_pv(root)

        # Assert
        assert_that(errors, has_length(1))

    def test_that_if_a_graph_has_a_trigger_pv_then_no_error(self):
        # Arrange
        root = etree.fromstring("""
                <widget typeId="org.csstudio.opibuilder.widgets.xyGraph" version="1.0">
                    <trigger_pv>TRIGGER_PV</trigger_pv>
                    <axis_0_time_format>1</axis_0_time_format>
                </widget>
              """)

        # Act
        errors = get_traces_with_different_buffer_sizes(root)

        # Assert
        assert_that(errors, has_length(0))

    def test_that_if_a_graph_has_no_time_format_then_no_error(self):
        # Arrange
        root = etree.fromstring("""
                <widget typeId="org.csstudio.opibuilder.widgets.xyGraph" version="1.0">
                </widget>
              """)

        # Act
        errors = get_traces_with_different_buffer_sizes(root)

        # Assert
        assert_that(errors, has_length(0))

    def test_that_if_a_graph_has_0_time_format_then_no_error(self):
        # Arrange
        root = etree.fromstring("""
                   <widget typeId="org.csstudio.opibuilder.widgets.xyGraph" version="1.0">
                        <axis_0_time_format>0</axis_0_time_format>
                   </widget>
                 """)

        # Act
        errors = get_traces_with_different_buffer_sizes(root)

        # Assert
        assert_that(errors, has_length(0))

    def test_GIVEN_a_colourless_grouping_container_inside_a_tabbed_container_THEN_no_error(self):
        # This is a CSS bug where colours are not propagated through tabbed containers correctly
        widget, colour_type = "groupingContainer", "background_color"
        grouping_container = get_xml_for_widget_with_colour(widget, colour_type, None)
        tabbed_container = etree.fromstring(WIDGET_XML.format(type="tab", widget_internals=grouping_container))

        errors = check_any_isis_colour(tabbed_container, widget, colour_type)

        assert_that(errors, has_length(0))


if __name__ == '__main__':
    unittest.main()
