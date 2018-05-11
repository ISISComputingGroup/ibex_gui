import os
import sys
import argparse
import unittest

from lxml import etree
from lxml.etree import LxmlError

from check_OPI_format_utils.colour_checker import check_colour, check_plot_area_backgrounds
from check_OPI_format_utils.text import check_label_punctuation, check_label_case_inside_containers, \
    check_label_case_outside_containers
from check_OPI_format_utils.container import get_items_not_in_grouping_container
from check_OPI_format_utils.font import get_incorrect_fonts
from xmlrunner import XMLTestRunner

from check_OPI_format_utils.xy_graph import get_traces_with_different_buffer_sizes
from check_opi_format_tests import TestCheckOpiFormatMethods

# Directory to iterate through
DEFAULT_ROOT_DIR = r"./resources/"

# Specify a logs directory
DEFAULT_LOGS_DIR = r"./check_OPI_format_logs/"


def file_iterator(directory, file_name=None):
    """
    Generator that returns the opi files that should be checked.
    Args:
        directory (str): The root directory to check for OPIs
        file_name (str): The name of the single file to check, every OPI in root_dir is checked if this is None
    Yields:
        str: Path to the OPI that must be checked
    """
    if file_name is None:
        # No single file was defined - iterate through all files
        for path, _, files in os.walk(directory):
            for file_name in files:
                if file_name.endswith(".opi"):
                    yield os.path.join(path, file_name)
    else:
        # Only check one file
        yield os.path.join(directory, file_name)


class CheckOpiFormat(unittest.TestCase):
    # Need the 'None' default because unittest's test loader uses a
    # no-argument constructor when getting the test names.
    def __init__(self, methodName, xml_root=None):
        # Boilerplate so that unittest knows how to run these tests.
        super(CheckOpiFormat, self).__init__(methodName=methodName)
        self.xml_root = xml_root

    def _assert_colour_correct(self, location, widget, colours):
        errors = check_colour(self.xml_root, widget, location, colours)

        if len(errors):
            self.fail("\n".join(["On line {}, text '{}', colour was not correct.".format(*error) for error in errors]))

    def _assert_trace_buffers_are_the_same(self):
        errors = get_traces_with_different_buffer_sizes(self.xml_root)

        if len(errors):
            self.fail("\n".join(["On line {}, buffer size {}, was different to the first, {}, in same graph xy widget."
                                .format(*error) for error in errors]))

    def test_GIVEN_an_opi_file_with_grouping_containers_WHEN_checking_the_background_colour_THEN_it_is_the_isis_background(self):
        self._assert_colour_correct("background_color", "groupingContainer", ["ISIS_OPI_Background"])

    def test_GIVEN_an_opi_file_with_labels_WHEN_checking_the_background_colour_THEN_it_is_the_isis_background(self):
        self._assert_colour_correct("background_color", "Label", ["ISIS_Label_Background", "ISIS_Title_Background_NEW"])

    def test_GIVEN_an_opi_file_with_textbox_WHEN_checking_background_colour_THEN_it_is_the_isis_textbox_background(self):
        self._assert_colour_correct("background_color", "TextInput", ["ISIS_Textbox_Background"])

    def test_GIVEN_an_opi_file_with_textbox_WHEN_checking_foreground_colour_THEN_it_is_the_isis_textbox_foreground(self):
        self._assert_colour_correct("foreground_color", "TextInput", ["ISIS_Standard_Text"])

    def test_GIVEN_an_opi_file_with_led_WHEN_checking_on_colour_THEN_it_is_the_isis_led_on_colour(self):
        self._assert_colour_correct("on_color", "LED", ["ISIS_Green_LED_On", "ISIS_Red_LED_On"])

    def test_GIVEN_an_opi_file_with_led_WHEN_checking_off_colour_THEN_it_is_the_isis_led_off_colour(self):
        self._assert_colour_correct("off_color", "LED", ["ISIS_Green_LED_Off", "ISIS_Red_LED_Off"])

    def test_GIVEN_an_opi_file_with_graph_widgets_WHEN_checking_buffer_sizes_THEN_all_buffer_sizes_are_the_same(self):
        self._assert_trace_buffers_are_the_same()

    def test_GIVEN_a_label_THEN_it_ends_in_a_colon(self):
        errors = check_label_punctuation(self.xml_root)
        if len(errors):
            message = "\n".join(["Label on line {} with text '{}' did not end in a colon."
                                .format(*error) for error in errors])
            self.fail(message)

    def test_GIVEN_a_label_within_a_grouping_container_THEN_it_is_sentence_case(self):
        errors = check_label_case_inside_containers(self.xml_root)
        if len(errors):
            message = "\n".join(["Label on line {} with text '{}' is not sentence case"
                                .format(*error) for error in errors])
            self.fail(message)

    def test_GIVEN_a_label_outside_a_grouping_container_THEN_it_is_title_case(self):
        errors = check_label_case_outside_containers(self.xml_root)
        if len(errors):
            message = "\n".join(["Label on line {} with text '{}' is not title case"
                                .format(*error) for error in errors])
            self.fail(message)

    def _assert_widget_not_outside_container(self, widget):
        errors = get_items_not_in_grouping_container(self.xml_root, widget)
        if len(errors):
            message = "\n".join(["{} on line {} ({}) was not in a grouping container"
                                .format(widget, *error) for error in errors])
            self.fail(message)

    def test_GIVEN_a_push_button_THEN_it_is_within_a_grouping_container(self):
        self._assert_widget_not_outside_container("NativeButton")

    def test_GIVEN_a_LED_THEN_it_is_within_a_grouping_container(self):
        self._assert_widget_not_outside_container("LED")

    def test_GIVEN_a_combo_THEN_it_is_within_a_grouping_container(self):
        self._assert_widget_not_outside_container("combo")

    def test_GIVEN_a_TextInput_THEN_it_is_within_a_grouping_container(self):
        self._assert_widget_not_outside_container("TextInput")

    def test_GIVEN_a_label_THEN_it_has_correct_font(self):
        errors = get_incorrect_fonts(self.xml_root)
        if len(errors):
            message = "\n".join(["Label on line {} with text '{}' has incorrect font"
                                .format(*error) for error in errors])
            self.fail(message)

    def test_GIVEN_plot_area_THEN_it_has_correct_background_colour(self):
        errors = check_plot_area_backgrounds(self.xml_root)
        if len(errors):
            message = "\n".join(["Plot on line {} with name '{}' has incorrect background colour"
                                .format(*error) for error in errors])
            self.fail(message)


if __name__ == "__main__":
    parser = argparse.ArgumentParser(prog='check_opi_format')

    parser.add_argument('-file', type=str, default=None,
                        help='A single file to check')
    parser.add_argument('-directory', type=str, default=DEFAULT_ROOT_DIR,
                        help='A directory to check files in')
    parser.add_argument('-logs_directory', type=str, default=DEFAULT_LOGS_DIR,
                        help='A directory to save the logs into')

    args = parser.parse_args()

    single_file = args.file
    root_dir = args.directory
    logs_dir = args.logs_directory

    loader = unittest.TestLoader()
    
    def self_valid():
        self_test_suite = unittest.TestSuite()
        self_test_suite.addTests(loader.loadTestsFromTestCase(TestCheckOpiFormatMethods))
        runner = XMLTestRunner(output=os.path.join(logs_dir, "check_opi_format"), stream=sys.stdout)
        return runner.run(self_test_suite).wasSuccessful()

    if not self_valid():
        print("Check OPI format test script failed own tests. Aborting")
        sys.exit(1)

    return_values = []
    xml_parser = etree.XMLParser(remove_blank_text=True)

    # Add test suite a dynamic number of times with an argument.
    # unittest's test loader is unable to take arguments to test classes by default so have
    # to use the getTestCaseNames() syntax and explicitly add the argument ourselves.
    for filename in file_iterator(root_dir, single_file):

        print("Testing '{}'".format(filename))

        suite = unittest.TestSuite()

        try:
            root = etree.parse(filename, xml_parser)
        except LxmlError as e:
            print("XML failed to parse {}".format(e))
            return_values.append(False)
            continue

        suite.addTests([CheckOpiFormat(test, root) for test in loader.getTestCaseNames(CheckOpiFormat)])
        runner = XMLTestRunner(output=os.path.join(logs_dir, filename), stream=sys.stdout)
        return_values.append(runner.run(suite).wasSuccessful())

    sys.exit(False in return_values)
