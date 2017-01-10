import unittest
from lxml import etree
from check_opi_format import CheckOpiFormat


class TestCheckOpiFormatMethods(unittest.TestCase):

    def test_that_if_a_valid_led_tag_is_parsed_it_causes_no_errors(self):
        # Arrange
        checker = CheckOpiFormat()
        xml = '<widget typeId="org.csstudio.opibuilder.widgets.LED">' \
              '<on_color>' \
              '<color name="ISIS_Green_LED_On" red="0" green="255" blue="0" />' \
              '</on_color>' \
              '</widget>'
        root = etree.fromstring(xml)

        # Act
        checker.check_led_colours(root)

        # Assert
        self.assertEqual(len(checker.errors), 0)

    def test_that_if_an_on_led_tag_without_a_named_colour_is_parsed_it_causes_one_error(self):
        # Arrange
        checker = CheckOpiFormat()
        xml = '<widget typeId="org.csstudio.opibuilder.widgets.LED">' \
              '<on_color>' \
              '<color red="0" green="255" blue="0" />' \
              '</on_color>' \
              '</widget>'
        root = etree.fromstring(xml)

        # Act
        checker.check_led_colours(root)

        # Assert
        self.assertEqual(len(checker.errors), 1)

    def test_that_if_an_on_led_tag_with_an_incorrect_named_colour_is_parsed_it_causes_one_error(self):
        # Arrange
        checker = CheckOpiFormat()
        xml = '<widget typeId="org.csstudio.opibuilder.widgets.LED">' \
              '<on_color>' \
              '<color name="THIS IS MADE UP" red="0" green="255" blue="0" />' \
              '</on_color>' \
              '</widget>'
        root = etree.fromstring(xml)

        # Act
        checker.check_led_colours(root)

        # Assert
        self.assertEqual(len(checker.errors), 1)

    def test_that_if_an_off_led_tag_without_a_named_colour_is_parsed_it_causes_one_error(self):
        # Arrange
        checker = CheckOpiFormat()
        xml = '<widget typeId="org.csstudio.opibuilder.widgets.LED">' \
              '<off_color>' \
              '<color red="0" green="255" blue="0" />' \
              '</off_color>' \
              '</widget>'
        root = etree.fromstring(xml)

        # Act
        checker.check_led_colours(root)

        # Assert
        self.assertEqual(len(checker.errors), 1)

    def test_that_if_an_off_led_tag_with_an_incorrect_named_colour_is_parsed_it_causes_one_error(self):
        # Arrange
        checker = CheckOpiFormat()
        xml = '<widget typeId="org.csstudio.opibuilder.widgets.LED">' \
              '<off_color>' \
              '<color name="THIS IS MADE UP" red="0" green="255" blue="0" />' \
              '</off_color>' \
              '</widget>'
        root = etree.fromstring(xml)

        # Act
        checker.check_led_colours(root)

        # Assert
        self.assertEqual(len(checker.errors), 1)

    def test_that_if_a_label_tag_with_a_valid_font_is_parsed_it_causes_no_errors(self):
        # Arrange
        checker = CheckOpiFormat()
        xml = '<widget typeId="org.csstudio.opibuilder.widgets.Label">' \
              '<font>' \
              '<opifont.name fontName="Segoe UI" height="18" style="1">ISIS_Header1_NEW</opifont.name>' \
              '</font>' \
              '</widget>'
        root = etree.fromstring(xml)

        # Act
        checker.check_opi_label_fonts(root)

        # Assert
        self.assertEqual(len(checker.errors), 0)

    def test_that_if_a_label_tag_with_a_valid_font_defined_in_the_fontname_attribute_is_parsed_it_causes_no_errors(self):
        # Arrange
        checker = CheckOpiFormat()
        xml = '<widget typeId="org.csstudio.opibuilder.widgets.Label">' \
              '<font>' \
              '<opifont.name fontName="ISIS_VALID_FONT" height="18" style="1">MADE UP FONT</opifont.name>' \
              '</font>' \
              '</widget>'
        root = etree.fromstring(xml)

        # Act
        checker.check_opi_label_fonts(root)

        # Assert
        self.assertEqual(len(checker.errors), 0)

    def test_that_if_a_label_tag_with_an_invalid_font_is_parsed_it_causes_one_error(self):
        # Arrange
        checker = CheckOpiFormat()
        xml = '<widget typeId="org.csstudio.opibuilder.widgets.Label">' \
              '<font>' \
              '<opifont.name fontName="Segoe UI" height="18" style="1">MADE UP FONT</opifont.name>' \
              '</font>' \
              '</widget>'
        root = etree.fromstring(xml)

        # Act
        checker.check_opi_label_fonts(root)

        # Assert
        self.assertEqual(len(checker.errors), 1)

    def test_that_if_a_plot_area_with_an_valid_background_colour_is_parsed_it_causes_no_errors(self):
        # Arrange
        checker = CheckOpiFormat()
        xml = '<plot_area_background_color>' \
              '<color name="ISIS_Something_valid" red="255" green="255" blue="255" />' \
              '</plot_area_background_color>'
        root = etree.fromstring(xml)

        # Act
        checker.check_plot_area_background(root)

        # Assert
        self.assertEqual(len(checker.errors), 0)

    def test_that_if_a_plot_area_with_an_invalid_background_colour_is_parsed_it_causes_one_error(self):
        # Arrange
        checker = CheckOpiFormat()
        xml = '<plot_area_background_color>' \
              '<color name="not_anything_valid" red="255" green="255" blue="255" />' \
              '</plot_area_background_color>'
        root = etree.fromstring(xml)

        # Act
        checker.check_plot_area_background(root)

        # Assert
        self.assertEqual(len(checker.errors), 1)

    def test_that_if_a_textbox_with_a_valid_background_colour_is_parsed_it_causes_no_errors(self):
        # Arrange
        checker = CheckOpiFormat()
        xml = '<widget typeId="org.csstudio.opibuilder.widgets.TextInput" version="1.0">' \
              '<background_color>' \
              '<color name="ISIS_Textbox_Background" red="255" green="255" blue="255" />' \
              '</background_color>' \
              '</widget>'
        root = etree.fromstring(xml)

        # Act
        checker.check_text_input_colors(root)

        # Assert
        self.assertEqual(len(checker.errors), 0)

    def test_that_if_a_textbox_with_an_invalid_background_colour_is_parsed_it_causes_one_error(self):
        # Arrange
        checker = CheckOpiFormat()
        xml = '<widget typeId="org.csstudio.opibuilder.widgets.TextInput" version="1.0">' \
              '<background_color>' \
              '<color name="invalid" red="255" green="255" blue="255" />' \
              '</background_color>' \
              '</widget>'
        root = etree.fromstring(xml)

        # Act
        checker.check_text_input_colors(root)

        # Assert
        self.assertEqual(len(checker.errors), 1)

    def test_that_if_a_textbox_with_no_named_background_colour_is_parsed_it_causes_one_error(self):
        # Arrange
        checker = CheckOpiFormat()
        xml = '<widget typeId="org.csstudio.opibuilder.widgets.TextInput" version="1.0">' \
              '<background_color>' \
              '<color red="255" green="255" blue="255" />' \
              '</background_color>' \
              '</widget>'
        root = etree.fromstring(xml)

        # Act
        checker.check_text_input_colors(root)

        # Assert
        self.assertEqual(len(checker.errors), 1)
        
    def test_that_if_a_textbox_with_a_valid_border_colour_is_parsed_it_causes_no_errors(self):
        # Arrange
        checker = CheckOpiFormat()
        xml = '<widget typeId="org.csstudio.opibuilder.widgets.TextInput" version="1.0">' \
              '<border_color>' \
              '<color name="ISIS_something" red="255" green="255" blue="255" />' \
              '</border_color>' \
              '</widget>'
        root = etree.fromstring(xml)

        # Act
        checker.check_text_input_colors(root)

        # Assert
        self.assertEqual(len(checker.errors), 0)

    def test_that_if_a_textbox_with_an_invalid_border_colour_is_parsed_it_causes_one_error(self):
        # Arrange
        checker = CheckOpiFormat()
        xml = '<widget typeId="org.csstudio.opibuilder.widgets.TextInput" version="1.0">' \
              '<border_color>' \
              '<color name="invalid" red="255" green="255" blue="255" />' \
              '</border_color>' \
              '</widget>'
        root = etree.fromstring(xml)

        # Act
        checker.check_text_input_colors(root)

        # Assert
        self.assertEqual(len(checker.errors), 1)

    def test_that_if_a_textbox_with_no_named_border_colour_is_parsed_it_causes_one_error(self):
        # Arrange
        checker = CheckOpiFormat()
        xml = '<widget typeId="org.csstudio.opibuilder.widgets.TextInput" version="1.0">' \
              '<border_color>' \
              '<color red="255" green="255" blue="255" />' \
              '</border_color>' \
              '</widget>'
        root = etree.fromstring(xml)

        # Act
        checker.check_text_input_colors(root)

        # Assert
        self.assertEqual(len(checker.errors), 1)

    def test_that_if_a_textbox_with_a_valid_foreground_colour_is_parsed_it_causes_no_errors(self):
        # Arrange
        checker = CheckOpiFormat()
        xml = '<widget typeId="org.csstudio.opibuilder.widgets.TextInput" version="1.0">' \
              '<foreground_color>' \
              '<color name="ISIS_Standard_Text" red="255" green="255" blue="255" />' \
              '</foreground_color>' \
              '</widget>'
        root = etree.fromstring(xml)

        # Act
        checker.check_text_input_colors(root)

        # Assert
        self.assertEqual(len(checker.errors), 0)

    def test_that_if_a_textbox_with_an_invalid_foreground_colour_is_parsed_it_causes_one_error(self):
        # Arrange
        checker = CheckOpiFormat()
        xml = '<widget typeId="org.csstudio.opibuilder.widgets.TextInput" version="1.0">' \
              '<foreground_color>' \
              '<color name="invalid" red="255" green="255" blue="255" />' \
              '</foreground_color>' \
              '</widget>'
        root = etree.fromstring(xml)

        # Act
        checker.check_text_input_colors(root)

        # Assert
        self.assertEqual(len(checker.errors), 1)

    def test_that_if_a_textbox_with_no_named_foreground_colour_is_parsed_it_causes_one_error(self):
        # Arrange
        checker = CheckOpiFormat()
        xml = '<widget typeId="org.csstudio.opibuilder.widgets.TextInput" version="1.0">' \
              '<foreground_color>' \
              '<color red="255" green="255" blue="255" />' \
              '</foreground_color>' \
              '</widget>'
        root = etree.fromstring(xml)

        # Act
        checker.check_text_input_colors(root)

        # Assert
        self.assertEqual(len(checker.errors), 1)

    def test_that_if_a_push_button_is_defined_within_a_grouping_container_it_causes_no_errors(self):
        # Arrange
        checker = CheckOpiFormat()
        xml = '<widget typeId="org.csstudio.opibuilder.widgets.groupingContainer" version="1.0">' \
              '<widget typeId="org.csstudio.opibuilder.widgets.NativeButton" version="1.0">' \
              '</widget>' \
              '</widget>'
        root = etree.fromstring(xml)

        # Act
        checker.check_items_are_in_grouping_containers(root)

        # Assert
        self.assertEqual(len(checker.errors), 0)

    def test_that_if_a_push_button_is_defined_outside_a_grouping_container_it_causes_one_error(self):
        # Arrange
        checker = CheckOpiFormat()
        xml = '<widget typeId="org.csstudio.opibuilder.widgets.NativeButton" version="1.0">' \
              '</widget>'
        root = etree.fromstring(xml)

        # Act
        checker.check_items_are_in_grouping_containers(root)

        # Assert
        self.assertEqual(len(checker.errors), 1)

    def test_that_if_an_led_is_defined_within_a_grouping_container_it_causes_no_errors(self):
        # Arrange
        checker = CheckOpiFormat()
        xml = '<widget typeId="org.csstudio.opibuilder.widgets.groupingContainer" version="1.0">' \
              '<widget typeId="org.csstudio.opibuilder.widgets.LED" version="1.0">' \
              '</widget>' \
              '</widget>'
        root = etree.fromstring(xml)

        # Act
        checker.check_items_are_in_grouping_containers(root)

        # Assert
        self.assertEqual(len(checker.errors), 0)

    def test_that_if_an_led_is_defined_outside_a_grouping_container_it_causes_one_error(self):
        # Arrange
        checker = CheckOpiFormat()
        xml = '<widget typeId="org.csstudio.opibuilder.widgets.LED" version="1.0">' \
              '</widget>'
        root = etree.fromstring(xml)

        # Act
        checker.check_items_are_in_grouping_containers(root)

        # Assert
        self.assertEqual(len(checker.errors), 1)

    def test_that_if_a_dropdown_menu_is_defined_within_a_grouping_container_it_causes_no_errors(self):
        # Arrange
        checker = CheckOpiFormat()
        xml = '<widget typeId="org.csstudio.opibuilder.widgets.groupingContainer" version="1.0">' \
              '<widget typeId="org.csstudio.opibuilder.widgets.combo" version="1.0">' \
              '</widget>' \
              '</widget>'
        root = etree.fromstring(xml)

        # Act
        checker.check_items_are_in_grouping_containers(root)

        # Assert
        self.assertEqual(len(checker.errors), 0)

    def test_that_if_a_dropdown_menu_is_defined_outside_a_grouping_container_it_causes_one_error(self):
        # Arrange
        checker = CheckOpiFormat()
        xml = '<widget typeId="org.csstudio.opibuilder.widgets.combo" version="1.0">' \
              '</widget>'
        root = etree.fromstring(xml)

        # Act
        checker.check_items_are_in_grouping_containers(root)

        # Assert
        self.assertEqual(len(checker.errors), 1)

    def test_that_a_grouping_container_with_a_properly_capitalised_name_causes_no_errors(self):
        # Arrange
        checker = CheckOpiFormat()
        xml = '<widget typeId="org.csstudio.opibuilder.widgets.groupingContainer" version="1.0">' \
              '<name>This is a Group Box</name>' \
              '</widget>'
        root = etree.fromstring(xml)

        # Act
        checker.check_capitals_for_grouping_containers(root)

        # Assert
        self.assertEqual(len(checker.errors), 0)

    def test_that_a_grouping_container_with_a_badly_capitalised_name_causes_one_error(self):
        # Arrange
        checker = CheckOpiFormat()
        xml = '<widget typeId="org.csstudio.opibuilder.widgets.groupingContainer" version="1.0">' \
              '<name>this is not capitalised properly</name>' \
              '</widget>'
        root = etree.fromstring(xml)

        # Act
        checker.check_capitals_for_grouping_containers(root)

        # Assert
        self.assertEqual(len(checker.errors), 1)

    def test_that_a_label_with_a_properly_capitalised_name_causes_no_errors(self):
        # Arrange
        checker = CheckOpiFormat()
        xml = '<widget typeId="org.csstudio.opibuilder.widgets.groupingContainer" version="1.0">' \
              '<widget typeId="org.csstudio.opibuilder.widgets.Label" version="1.0">' \
              '<text>Some text goes here</text>' \
              '</widget>' \
              '</widget>'
        root = etree.fromstring(xml)

        # Act
        checker.check_capitals_for_labels(root)

        # Assert
        self.assertEqual(len(checker.errors), 0)

    def test_that_a_label_with_a_properly_capitalised_name_and_a_capitalised_abbreviation_causes_no_errors(self):
        # Arrange
        checker = CheckOpiFormat()
        xml = '<widget typeId="org.csstudio.opibuilder.widgets.groupingContainer" version="1.0">' \
              '<widget typeId="org.csstudio.opibuilder.widgets.Label" version="1.0">' \
              '<text>This is an OPI</text>' \
              '</widget>' \
              '</widget>'
        root = etree.fromstring(xml)

        # Act
        checker.check_capitals_for_labels(root)

        # Assert
        self.assertEqual(len(checker.errors), 0)

    def test_that_a_label_with_a_badly_capitalized_name_causes_one_error(self):

        # Arrange
        checker = CheckOpiFormat()
        xml = '<widget typeId="org.csstudio.opibuilder.widgets.groupingContainer" version="1.0">' \
              '<widget typeId="org.csstudio.opibuilder.widgets.Label" version="1.0">' \
              '<text>VOLTAGE should not have been in block capitals</text>' \
              '</widget>' \
              '</widget>'
        root = etree.fromstring(xml)

        # Act
        checker.check_capitals_for_labels(root)

        # Assert
        self.assertEqual(len(checker.errors), 1)

    def test_that_a_label_outside_a_grouping_container_does_not_throw_an_error_when_it_is_in_title_case(self):
        # Arrange
        checker = CheckOpiFormat()
        xml = '<widget typeId="org.csstudio.opibuilder.widgets.Label" version="1.0">' \
              '<text>This Is In Proper Case</text>' \
              '</widget>'
        root = etree.fromstring(xml)

        # Act
        checker.check_capitals_for_labels(root)

        # Assert
        self.assertEqual(len(checker.errors), 0)

    def test_that_a_label_with_a_colon_at_the_end_causes_no_errors(self):
        # Arrange
        checker = CheckOpiFormat()
        xml = '<widget typeId="org.csstudio.opibuilder.widgets.groupingContainer" version="1.0">' \
              '<widget typeId="org.csstudio.opibuilder.widgets.Label" version="1.0">' \
              '<text>Voltage:</text>' \
              '</widget>' \
              '</widget>'
        root = etree.fromstring(xml)

        # Act
        checker.check_colon_at_the_end_of_labels(root)

        # Assert
        self.assertEqual(len(checker.errors), 0)

    def test_that_a_label_with_no_colon_at_the_end_causes_one_error(self):
        # Arrange
        checker = CheckOpiFormat()
        xml = '<widget typeId="org.csstudio.opibuilder.widgets.groupingContainer" version="1.0">' \
              '<widget typeId="org.csstudio.opibuilder.widgets.Label" version="1.0">' \
              '<text>Voltage</text>' \
              '</widget>' \
              '</widget>'
        root = etree.fromstring(xml)

        # Act
        checker.check_colon_at_the_end_of_labels(root)

        # Assert
        self.assertEqual(len(checker.errors), 1)


if __name__ == '__main__':
    unittest.main()