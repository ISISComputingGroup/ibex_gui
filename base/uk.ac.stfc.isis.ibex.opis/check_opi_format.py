import os
import sys
import argparse
import unittest

from lxml import etree
from time import gmtime, strftime

from lxml.etree import LxmlError

from check_OPI_format_utils.colour_checker import check_colour

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

    def check_case(self, root, xpath, error_message, title_case):
        for name in root.xpath(xpath):
            try:
                if name.text is None:
                    raise CapitalisationError("No text identified")

                words = name.text.split()
                if len(words)==0:
                    raise CapitalisationError("Text is empty")

                # Special case for the manager mode indicator
                if title_case and "manager mode" in name.text.lower():
                    continue

                # Handle special case of first word (which should be capitalised regardless of length)
                word = words[0]
                if not word.title() == word and not any(s in word for s in self.ignore):
                    raise CapitalisationError("First word is not capitalised")

            # Ignore words less than 4 characters as these are probably prepositions
            # Also don't check first word as that is a special case handled above
                for i, word in enumerate(words):
                    is_long_word = len(word) > self.ignore_short_words_limit
                    is_first_word = i==0
                    is_ignored = any(s in word for s in self.ignore)
                    if is_long_word and not is_first_word and not is_ignored:
                        if title_case and word.title() != word:
                            raise CapitalisationError("Word '{}' is not in title case".format(word))
                        elif not title_case and word.lower() != word:
                            raise CapitalisationError("Word '{}' is not in sentence case".format(word))

            except CapitalisationError as caps_err:
                err = "Error on line {source}, {name}: {broad_message}\n    {fine_message}".format(
                    source=name.sourceline,
                    name=etree.tostring(name),
                    broad_message=error_message,
                    fine_message=caps_err)
                self.errors.append(err)

    def check_title_case(self, root, xpath, error_message):
        self.check_case(root, xpath, error_message, True)

    def check_sentence_case(self, root, xpath, error_message):
        self.check_case(root, xpath, error_message, False)

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

    def check_led_colours(self, root):

        # Select LEDs
        xpath = "//" + self.widget_xpath + "LED']"

        condition = "/on_color/color[not(@name)]"
        error_message = "An LED indicator didn't use a correct ISIS colour scheme when turned on."
        self.check_condition(root, xpath + condition, error_message)

        condition = "/on_color/color[@name!='ISIS_Green_LED_On' and @name!='ISIS_Red_LED_On']"
        error_message = "An LED indicator didn't use a correct ISIS colour scheme when turned on."
        self.check_condition(root, xpath + condition, error_message)

        condition = "/off_color/color[not(@name)]"
        error_message = "An LED indicator didn't use a correct ISIS colour scheme when turned off."
        self.check_condition(root, xpath + condition, error_message)

        condition = "/off_color/color[@name!='ISIS_Green_LED_Off' and @name!='ISIS_Red_LED_Off']"
        error_message = "An LED indicator didn't use a correct ISIS colour scheme when turned off."
        self.check_condition(root, xpath + condition, error_message)

    def check_text_input_colors(self, root):

        # Select text input fields
        xpath = "//" + self.widget_xpath + "TextInput']"

        condition = "/background_color/color[not(@name) or @name!='ISIS_Textbox_Background']"
        error_message = "A text input field didn't use ISIS_Textbox_Background as it's background color."
        self.check_condition(root, xpath + condition, error_message)

        condition = "/foreground_color/color[not(@name) or @name!='ISIS_Standard_Text']"
        error_message = "A text input field didn't use ISIS_Standard_Text as it's foreground color."
        self.check_condition(root, xpath + condition, error_message)

        condition = "/border_color/color[not(@name) or not(starts-with(@name, 'ISIS_'))]"
        error_message = "A text input field didn't use ISIS_* color as it's border color."
        self.check_condition(root, xpath + condition, error_message)

    def check_items_are_in_grouping_containers(self, root):

        element_xpath = "//" + self.widget_xpath
        ancestor_xpath = "' and not(ancestor::" + self.widget_xpath + "groupingContainer'])]"

        # Select a push button outside a grouping container
        xpath = element_xpath + "NativeButton" + ancestor_xpath
        error_message = "A button was not within a grouping container."
        self.check_condition(root, xpath, error_message)

        # Select an LED outside a grouping container
        xpath = element_xpath + "LED" + ancestor_xpath
        error_message = "An LED indicator was not within a grouping container."
        self.check_condition(root, xpath, error_message)

        # Select a drop down menu outside a grouping container
        xpath = element_xpath + "combo" + ancestor_xpath
        error_message = "A drop down menu was not within a grouping container."
        self.check_condition(root, xpath, error_message)

        # Select a text input field outside a grouping container
        xpath = element_xpath + "TextInput" + ancestor_xpath
        error_message = "A text input field was not within a grouping container."
        self.check_condition(root, xpath, error_message)

    def check_capitals_for_grouping_containers(self, root):
        xpath = "//" + self.widget_xpath + "groupingContainer']/name"
        error_message = "Grouping container titles should be in 'Title Case'."

        self.check_title_case(root, xpath, error_message)

    def check_capitals_for_labels_outside_grouping_containers(self, root):

        # Select a Label outside a grouping container
        xpath = \
            "//" + self.widget_xpath + "Label' " \
            "and not(ancestor::" + self.widget_xpath + "groupingContainer'])]/text"

        error_message = "Labels outside grouping containers are titles, and therefore should be 'Title Case'."
        self.check_title_case(root, xpath, error_message)

    def check_capitals_for_labels(self, root):

        # Select a Label within a grouping container
        xpath = "//" + self.widget_xpath + "groupingContainer']" \
                "/" + self.widget_xpath + "Label']/text"

        error_message = "Labels should be in 'sentence case'"
        self.check_sentence_case(root, xpath, error_message)

    def check_colon_at_the_end_of_labels(self, root):
        container_name_xpath = "//" + self.widget_xpath + "groupingContainer']" \
                             "/" + self.widget_xpath + "Label']/text"

        for name in root.xpath(container_name_xpath):
            text = name.text
            if text is None or len(text)==0:
                continue

            # Check last character in the string is a colon
            last_character_is_colon = text[-1:] != ":"
            ends_in_ellipsis = len(text)>= 3 and text[-3:]=="..."
            text_is_numeric = text.isdigit()
            is_ignored = any(s==text for s in self.ignore)
            if  not last_character_is_colon and not ends_in_ellipsis and not text_is_numeric and not is_ignored:
                    err = "Error on line " + str(name.sourceline) + ": " + etree.tostring(name) \
                        + "\n... Labels should usually have a colon at the end, unless this is a tabular layout"
                    self.errors.append(err)

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

    #def test_GIVEN_an_opi_file_with_grouping_containers_WHEN_checking_the_background_colour_THEN_it_is_the_isis_background(self):
        # xpath = "//" + self.widget_xpath + "groupingContainer']"
        #
        # condition = "/background_color/color[not(@name) or " \
        #             "(@name!='ISIS_Label_Background' and @name!='ISIS_OPI_Background')]"
        #
        # for _ in self.xml_root.xpath(xpath + condition):
        #     self.fail("A grouping container didn't use ISIS_OPI_Background as it's background color.")

    def test_GIVEN_an_opi_file_with_labels_WHEN_checking_the_background_colour_THEN_it_is_the_isis_background(self):
        errors = check_colour(self.xml_root, "Label", ["ISIS_Label_Background", "ISIS_Title_Background_NEW"])

        if len(errors):
            message = ""
            for error in errors:
                message += "On line {}, text '{}', colour failed\n".format(*error)

            self.fail(message)

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
