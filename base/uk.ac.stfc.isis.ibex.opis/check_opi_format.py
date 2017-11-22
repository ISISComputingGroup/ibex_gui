import os
import sys
import argparse
import unittest

from lxml import etree
from time import gmtime, strftime

from lxml.etree import LxmlError

from check_OPI_format_utils.colour_checker import check_colour
from check_OPI_format_utils.text import check_label_punctuation, check_label_case_inside_containers, \
    check_label_case_outside_containers
from check_OPI_format_utils.container import get_items_not_in_grouping_container

# Directory to iterate through
from xmlrunner import XMLTestRunner

root_directory = r"./resources/"

# Files ending with .opi are parsed:
file_extension = r".opi"

# Specify a logs directory
# Remember to update .gitignore so that logs don't get pushed to git.
logs_directory = r"./check_OPI_format_logs/"

# Single file
single_file = ""


class CapitalisationError(Exception):
    pass


class CheckOpiFormatOld(object):

    # If a word contains any of the following, the whole word will be ignored
    ignore = \
        ["$", "&", "#", '"', "'", "(", ")", "OPI", "PSU", "HW", "Hz", "LED", "A:", "B:", "C:", "D:", "CCD100", "ISIS", "JJ", "X-Ray", "LTC"]

    # When checking capitalisation will ignore words <= this length
    # This is to avoid
    ignore_short_words_limit = 3

    # Start of the XPATH for a widget
    widget_xpath = "widget[@typeId='org.csstudio.opibuilder.widgets."

    def __init__(self, single_file="", root_directory="", logs_directory="", file_extension=""):
        self.errors = []

        self.single_file = single_file
        self.root_directory = root_directory
        self.logs_directory = logs_directory
        self.file_extension = file_extension

    @staticmethod
    def get_tree(filepath):
        try:
            with open(filepath) as opi_file:
                parser = etree.XMLParser(remove_blank_text=True)
                return etree.parse(opi_file, parser)
        except IOError:
            print("Fatal: Could not open file {}".format(filepath))
            sys.exit(1)
        except LxmlError:
            print("Fatal: Could not parse file {} as XML.".format(filepath))
            sys.exit(1)

    def file_iterator(self):

        if self.single_file == "":
            # No single file was defined - iterate through all files
            for filename in os.listdir(self.root_directory):
                if filename.endswith(self.file_extension):
                    filepath = os.path.join(self.root_directory, filename)
                    yield filepath
        else:
            # Only check one file
            filepath = os.path.join(self.root_directory, self.single_file)
            yield filepath

    def check_condition(self, root, xpath, error_message):
        for error in root.xpath(xpath):
            err = "Error on line " + str(error.sourceline) + ": " + etree.tostring(error) \
                + "\n... " + error_message
            self.errors.append(err)

    def check_plot_area_background(self, root):

        # Select a plot area
        xpath = "//plot_area_background_color/"

        condition = "color[not(@name) or not(starts-with(@name, 'ISIS_'))]"
        error_message = "The plot area background color wasn't the ISIS_* color scheme"

        self.check_condition(root, xpath + condition, error_message)

    def check_opi_label_fonts(self, root):

        # Select labels
        xpath = "//" + self.widget_xpath + "Label']"

        condition = "/font/opifont.name[not(starts-with(., 'ISIS_')) and not(starts-with(@fontName, 'ISIS_'))]"
        error_message = "The font must be an ISIS_* font"
        self.check_condition(root, xpath + condition, error_message)

    def run(self, file, directory, logs_directory, file_extension):

        self.single_file = file
        self.root_directory = directory
        self.logs_directory = logs_directory
        self.file_extension = file_extension

        self.errors = []
        build_contains_errors = False
        time = strftime("%Y-%m-%d_%H-%M-%S", gmtime())

        for filepath in self.file_iterator():

            root = self.get_tree(filepath)

            self.check_led_colours(root)
            self.check_opi_label_fonts(root)
            self.check_plot_area_background(root)
            self.check_text_input_colors(root)
            self.check_items_are_in_grouping_containers(root)
            self.check_capitals_for_grouping_containers(root)
            self.check_capitals_for_labels(root)
            self.check_capitals_for_labels_outside_grouping_containers(root)
            self.check_colon_at_the_end_of_labels(root)
            self.check_background_colour_for_labels(root)
            self.check_background_colour_for_grouping_containers(root)

            if len(self.errors) > 0:

                build_contains_errors = True

                # Write to console
                print("\n --------------")
                print("{} errors found in file {}".format(len(self.errors), filepath))
                print(" --------------\n")
                for error in self.errors:
                    print(error)

                # Write to file
                f = open(self.logs_directory + "/" + time + '.txt', 'a')
                f.write("\n --------------\n")
                f.write(str(len(self.errors)) + " errors found in file " + filepath + "\n")
                f.write(" --------------\n\n")
                for error in self.errors:
                    f.write(error + "\n")

            self.errors = []

        if build_contains_errors:
            sys.exit(1)
        else:
            sys.exit(0)


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

    def test_GIVEN_a_label_THEN_it_ends_in_a_colon(self):
        errors = check_label_punctuation(self.xml_root)
        if len(errors):
            message = "\n".join(["Label on line {} with text '{}' did not end in a colon."
                                .format(*error) for error in errors])
            self.fail(message)

    def test_GIVEN_a_label_within_a_grouping_container_THEN_it_is_sentence_case(self):
        errors = check_label_case_inside_containers(self.xml_root)
        if len(errors):
            message = "\n".join(["Label on line {}: {}"
                                .format(*error) for error in errors])
            self.fail(message)

    def test_GIVEN_a_label_outside_a_grouping_container_THEN_it_is_title_case(self):
        errors = check_label_case_outside_containers(self.xml_root)
        if len(errors):
            message = "\n".join(["Label on line {}: {}"
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

if __name__ == "__main__":

    parser = argparse.ArgumentParser(prog='check_opi_format')

    parser.add_argument('-file', type=str, default=single_file,
                        help='A single file to check')
    parser.add_argument('-directory', type=str, default=root_directory,
                        help='A directory to check files in')
    parser.add_argument('-logs_directory', type=str, default=logs_directory,
                        help='A directory to save the logs into')
    parser.add_argument('-extension', type=str, default=file_extension,
                        help='The filename extension for OPIs')

    args = parser.parse_args()

    single_file = args.file
    root_directory = args.directory
    logs_directory = args.logs_directory
    file_extension = args.extension

    # CheckOpiFormat().run(single_file, root_directory, logs_directory, file_extension)

    ret_vals = []

    loader = unittest.TestLoader()
    xml_parser = etree.XMLParser(remove_blank_text=True)

    # Add test suite a dynamic number of times with an argument.
    # unittest's test loader is unable to take arguments to test classes by default so have
    # to use the getTestCaseNames() syntax and explicitly add the argument ourselves.
    for filename in CheckOpiFormatOld(file_extension=file_extension, single_file=single_file, logs_directory=logs_directory, root_directory=root_directory).file_iterator():

        print("Testing '{}'".format(filename))

        suite = unittest.TestSuite()

        root = etree.parse(filename, xml_parser)

        suite.addTests([CheckOpiFormat(test, root) for test in loader.getTestCaseNames(CheckOpiFormat)])

        runner = XMLTestRunner(output=str(os.curdir), stream=sys.stdout)
        ret_vals.append(runner.run(suite).wasSuccessful())

    sys.exit(False in ret_vals)
