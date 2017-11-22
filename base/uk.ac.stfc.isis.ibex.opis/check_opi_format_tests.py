import unittest
from lxml import etree

from check_OPI_format_utils.colour_checker import check_specific_isis_colours, check_any_isis_colour
from check_OPI_format_utils.text import check_label_punctuation, check_container_names,\
    check_label_case_inside_containers, check_label_case_outside_containers
from check_OPI_format_utils.container import get_items_not_in_grouping_container

def make_widget_with_colour(widget, colour_type, colour_name=None):
    colour_str = '<color {} red="0" green="255" blue="0" />'
    colour_str = colour_str.format("" if colour_name is None else 'name="{}"'.format(colour_name))

    xml = '<widget typeId="org.csstudio.opibuilder.widgets.{widget}">' \
          '<{colour_type}>' \
          '{colour_str}' \
          '</{colour_type}>' \
          '</widget>'.format(widget=widget, colour_type=colour_type, colour_str=colour_str)

    return etree.fromstring(xml)


def make_label_in_grouping_container(text):
    xml = '<widget typeId="org.csstudio.opibuilder.widgets.groupingContainer" version="1.0">' \
          '<widget typeId="org.csstudio.opibuilder.widgets.Label" version="1.0">' \
          '<text>{}</text>' \
          '</widget>' \
          '</widget>'.format(text)
    return etree.fromstring(xml)


def make_label_outside_grouping_container(text):
    xml = '<widget typeId="org.csstudio.opibuilder.widgets.notAgroupingContainer" version="1.0">' \
          '<widget typeId="org.csstudio.opibuilder.widgets.Label" version="1.0">' \
          '<text>{}</text>' \
          '</widget>' \
          '</widget>'.format(text)
    return etree.fromstring(xml)


def make_grouping_container(text):
    xml = '<widget typeId="org.csstudio.opibuilder.widgets.groupingContainer" version="1.0">' \
          '<name>{}</name>' \
          '</widget>'.format(text)
    return etree.fromstring(xml)


class TestCheckOpiFormatMethods(unittest.TestCase):

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

    # def test_that_if_a_label_tag_with_a_valid_font_is_parsed_it_causes_no_errors(self):
    #     # Arrange
    #
    #     xml = '<widget typeId="org.csstudio.opibuilder.widgets.Label">' \
    #           '<font>' \
    #           '<opifont.name fontName="Segoe UI" height="18" style="1">ISIS_Header1_NEW</opifont.name>' \
    #           '</font>' \
    #           '</widget>'
    #     root = etree.fromstring(xml)
    #
    #     # Act
    #     self.checker.check_opi_label_fonts(root)
    #
    #     # Assert
    #     self.assertEqual(len(self.checker.errors), 0)
    #
    # def test_that_if_a_label_tag_with_a_valid_font_in_the_fontname_attribute_is_parsed_it_causes_no_errors(self):
    #     # Arrange
    #
    #     xml = '<widget typeId="org.csstudio.opibuilder.widgets.Label">' \
    #           '<font>' \
    #           '<opifont.name fontName="ISIS_VALID_FONT" height="18" style="1">MADE UP FONT</opifont.name>' \
    #           '</font>' \
    #           '</widget>'
    #     root = etree.fromstring(xml)
    #
    #     # Act
    #     self.checker.check_opi_label_fonts(root)
    #
    #     # Assert
    #     self.assertEqual(len(self.checker.errors), 0)
    #
    # def test_that_if_a_label_tag_with_an_invalid_font_is_parsed_it_causes_one_error(self):
    #     # Arrange
    #
    #     xml = '<widget typeId="org.csstudio.opibuilder.widgets.Label">' \
    #           '<font>' \
    #           '<opifont.name fontName="Segoe UI" height="18" style="1">MADE UP FONT</opifont.name>' \
    #           '</font>' \
    #           '</widget>'
    #     root = etree.fromstring(xml)
    #
    #     # Act
    #     self.checker.check_opi_label_fonts(root)
    #
    #     # Assert
    #     self.assertEqual(len(self.checker.errors), 1)
    #
    # def test_that_if_a_plot_area_with_an_valid_background_colour_is_parsed_it_causes_no_errors(self):
    #     # Arrange
    #
    #     xml = '<plot_area_background_color>' \
    #           '<color name="ISIS_Something_valid" red="255" green="255" blue="255" />' \
    #           '</plot_area_background_color>'
    #     root = etree.fromstring(xml)
    #
    #     # Act
    #     self.checker.check_plot_area_background(root)
    #
    #     # Assert
    #     self.assertEqual(len(self.checker.errors), 0)
    #
    # def test_that_if_a_plot_area_with_an_invalid_background_colour_is_parsed_it_causes_one_error(self):
    #     # Arrange
    #
    #     xml = '<plot_area_background_color>' \
    #           '<color name="not_anything_valid" red="255" green="255" blue="255" />' \
    #           '</plot_area_background_color>'
    #     root = etree.fromstring(xml)
    #
    #     # Act
    #     self.checker.check_plot_area_background(root)
    #
    #     # Assert
    #     self.assertEqual(len(self.checker.errors), 1)
    #
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

        xml = '<widget typeId="org.csstudio.opibuilder.widgets.groupingContainer" version="1.0">' \
              '<widget typeId="org.csstudio.opibuilder.widgets.NativeButton" version="1.0">' \
              '</widget>' \
              '</widget>'
        root = etree.fromstring(xml)

        # Act
        errors = get_items_not_in_grouping_container(root, "NativeButton")

        # Assert
        self.assertEqual(len(errors), 0)

    def test_that_if_a_push_button_is_defined_outside_a_grouping_container_it_causes_one_error(self):
        # Arrange

        xml = '<widget typeId="org.csstudio.opibuilder.widgets.NativeButton" version="1.0">' \
              '</widget>'
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

    # def test_that_if_a_label_uses_the_correct_background_colour_it_causes_no_errors(self):
    #     # Arrange
    #
    #     xml = '<widget typeId="org.csstudio.opibuilder.widgets.Label" version="1.0">' \
    #           '<background_color>' \
    #           '<color name="ISIS_Label_Background" red="240" green="240" blue="240" />' \
    #           '</background_color>' \
    #           '</widget>'
    #     root = etree.fromstring(xml)
    #
    #     # Act
    #     self.checker.check_background_colour_for_labels(root)
    #
    #     # Assert
    #     self.assertEqual(len(self.checker.errors), 0)
    #
    # def test_that_if_a_label_uses_an_incorrect_background_colour_it_causes_one_error(self):
    #     # Arrange
    #
    #     xml = '<widget typeId="org.csstudio.opibuilder.widgets.Label" version="1.0">' \
    #           '<background_color>' \
    #           '<color name="Incorrect" red="240" green="240" blue="240" />' \
    #           '</background_color>' \
    #           '</widget>'
    #     root = etree.fromstring(xml)
    #
    #     # Act
    #     self.checker.check_background_colour_for_labels(root)
    #
    #     # Assert
    #     self.assertEqual(len(self.checker.errors), 1)
    #
    # def test_that_if_a_label_uses_no_named_background_colour_it_causes_one_error(self):
    #     # Arrange
    #
    #     xml = '<widget typeId="org.csstudio.opibuilder.widgets.Label" version="1.0">' \
    #           '<background_color>' \
    #           '<color red="240" green="240" blue="240" />' \
    #           '</background_color>' \
    #           '</widget>'
    #     root = etree.fromstring(xml)
    #
    #     # Act
    #     self.checker.check_background_colour_for_labels(root)
    #
    #     # Assert
    #     self.assertEqual(len(self.checker.errors), 1)
    #
    # def test_that_if_a_grouping_container_uses_the_correct_background_colour_it_causes_no_errors(self):
    #     # Arrange
    #
    #     xml = '<widget typeId="org.csstudio.opibuilder.widgets.groupingContainer" version="1.0">' \
    #           '<background_color>' \
    #           '<color name="ISIS_OPI_Background" red="240" green="240" blue="240" />' \
    #           '</background_color>' \
    #           '</widget>'
    #     root = etree.fromstring(xml)
    #
    #     # Act
    #     self.checker.check_background_colour_for_grouping_containers(root)
    #
    #     # Assert
    #     self.assertEqual(len(self.checker.errors), 0)
    #
    # def test_that_if_a_grouping_container_uses_an_incorrect_background_colour_it_causes_one_error(self):
    #     # Arrange
    #
    #     xml = '<widget typeId="org.csstudio.opibuilder.widgets.groupingContainer" version="1.0">' \
    #           '<background_color>' \
    #           '<color name="Incorrect" red="240" green="240" blue="240" />' \
    #           '</background_color>' \
    #           '</widget>'
    #     root = etree.fromstring(xml)
    #
    #     # Act
    #     self.checker.check_background_colour_for_grouping_containers(root)
    #
    #     # Assert
    #     self.assertEqual(len(self.checker.errors), 1)
    #
    # def test_that_if_a_grouping_container_uses_no_named_background_colour_it_causes_one_error(self):
    #     # Arrange
    #
    #     xml = '<widget typeId="org.csstudio.opibuilder.widgets.groupingContainer" version="1.0">' \
    #           '<background_color>' \
    #           '<color red="240" green="240" blue="240" />' \
    #           '</background_color>' \
    #           '</widget>'
    #     root = etree.fromstring(xml)
    #
    #     # Act
    #     self.checker.check_background_colour_for_grouping_containers(root)
    #
    #     # Assert
    #     self.assertEqual(len(self.checker.errors), 1)

if __name__ == '__main__':
    unittest.main()
