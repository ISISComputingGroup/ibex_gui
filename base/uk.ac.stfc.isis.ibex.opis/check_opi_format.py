import os
import sys
from lxml import etree
from time import gmtime, strftime


class CheckOpiFormat:

    # Directory to iterate through
    root_directory = r"C:\Instrument\Dev\ibex_gui\base\uk.ac.stfc.isis.ibex.opis\resources"

    # Files ending with .opi are parsed:
    file_extension = r".opi"

    # If a word contains any of the following, the whole word will be ignored
    ignore = \
        ["$", "&", "#", '"', "'", "(", ")", "OPI", "PSU", "HW", "Hz", "LED", "A:", "B:", "C:", "D:"]

    # When checking capitalisation will ignore words <= this length
    # This is to avoid
    ignore_short_words_limit = 3

    # Start of the XPATH for a widget
    widget_xpath = "widget[@typeId='org.csstudio.opibuilder.widgets."

    def __init__(self):
        self.errors = []

    @staticmethod
    def get_tree(filepath):
        with open(filepath) as opi_file:
            parser = etree.XMLParser(remove_blank_text=True)
            return etree.parse(opi_file, parser)

    def file_iterator(self):
        for filename in os.listdir(self.root_directory):
            if filename.endswith(self.file_extension):
                filepath = os.path.join(self.root_directory, filename)
                yield filepath

    def check_condition(self, root, xpath, error_message):
        for error in root.xpath(xpath):
            err = "Error on line " + str(error.sourceline) + ": " + etree.tostring(error) \
                + "\n... " + error_message
            self.errors.append(err)

    def check_title_case(self, root, xpath, error_message):
        for name in root.xpath(xpath):
            capitalisation_error = False
            words = name.text.split()

            # Ignore words less than 4 characters as these are probably prepositions
            for word in words:
                if len(word) > self.ignore_short_words_limit \
                        and word.title() != word \
                        and not any(s in word for s in self.ignore):
                    capitalisation_error = True

            if capitalisation_error:
                err = "Error on line " + str(name.sourceline) + ": " + etree.tostring(name) \
                      + "\n... " + error_message
                self.errors.append(err)

    def check_sentence_case(self, root, xpath, error_message):

        for name in root.xpath(xpath):
            capitalisation_error = False
            words = name.text.split()

            # Ignore words less than 4 characters as these are probably prepositions
            for word in words:
                if len(word) > self.ignore_short_words_limit \
                        and not words[0] == words[0].title() \
                        and not word.lower() == word \
                        and not any(s in word for s in self.ignore):
                    capitalisation_error = True

            if capitalisation_error:
                err = "Error on line " + str(name.sourceline) + ": " + etree.tostring(name) \
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
            words = name.text

            # Check last character in the string is a colon
            if words[-1:] != ":" and words[-3:] != "..." and not words.isdigit() \
                and not any(s in words for s in self.ignore):
                    err = "Error on line " + str(name.sourceline) + ": " + etree.tostring(name) \
                        + "\n... Labels should usually have a colon at the end, unless this is a tabular layout"
                    self.errors.append(err)

    def check_background_colour_for_labels(self, root):
        # Select labels
        xpath = "//" + self.widget_xpath + "Label']"

        condition = "/background_color/color[not(@name) or @name!='ISIS_Label_Background']"
        error_message = "A label didn't use ISIS_Label_Background as it's background color."
        self.check_condition(root, xpath + condition, error_message)

    def check_background_colour_for_grouping_containers(self, root):
        # Select grouping containers
        xpath = "//" + self.widget_xpath + "groupingContainer']"

        condition = "/background_color/color[not(@name) or @name!='ISIS_OPI_Background']"
        error_message = "A label didn't use ISIS_OPI_Background as it's background color."
        self.check_condition(root, xpath + condition, error_message)

    def run(self):

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
                print "\n --------------"
                print str(len(self.errors)) + " errors found in file " + filepath
                print " --------------\n"
                for error in self.errors:
                    print error

                # Write to file
                f = open('check_OPI_format_logs/' + time + '.txt', 'a')
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

if __name__ == "__main__":
    CheckOpiFormat().run()
