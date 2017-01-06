import unittest
from lxml import etree
from check_opi_format import CheckOpiFormat


class TestCheckOpiFormatMethods(unittest.TestCase):

    def test_that_if_a_valid_led_tag_is_parsed_it_causes_no_errors(self):
        # Arrange
        checker = CheckOpiFormat()
        xml = '<widget typeId="org.csstudio.opibuilder.widgets.LED"><on_color><color name="ISIS_Green_LED_On" red="0" green="255" blue="0" /></on_color></widget>'
        root = etree.fromstring(xml)

        # Act
        checker.check_LED_colours(root)

        # Assert
        self.assertEqual(len(checker.errors), 0)

    def test_that_if_an_led_tag_without_a_named_colour_is_parsed_it_causes_one_error(self):
        # Arrange
        checker = CheckOpiFormat()
        xml = '<widget typeId="org.csstudio.opibuilder.widgets.LED"><on_color><color red="0" green="255" blue="0" /></on_color></widget>'
        root = etree.fromstring(xml)

        # Act
        checker.check_LED_colours(root)

        # Assert
        self.assertEqual(len(checker.errors), 1)

    def test_that_if_an_led_tag_with_an_incorrect_named_colour_is_parsed_it_causes_one_error(self):
        # Arrange
        checker = CheckOpiFormat()
        xml = '<widget typeId="org.csstudio.opibuilder.widgets.LED"><on_color><color name="THIS IS MADE UP" red="0" green="255" blue="0" /></on_color></widget>'
        root = etree.fromstring(xml)

        # Act
        checker.check_LED_colours(root)

        # Assert
        self.assertEqual(len(checker.errors), 1)



if __name__ == '__main__':
    unittest.main()