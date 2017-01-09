import os
import sys
from lxml import etree

class CheckOpiFormat:

    def __init__(self):
        self.root_directory = r"C:\Instrument\Dev\ibex_gui\base\uk.ac.stfc.isis.ibex.opis\resources"
        self.file_extension = r".opi"
        self.errors = []

    def get_tree(self, filepath):
        with open(filepath) as opi_file:
            parser = etree.XMLParser(remove_blank_text=True)
            return etree.parse(opi_file, parser)

    def file_iterator(self):
        for filename in os.listdir(self.root_directory):
            if filename.endswith(self.file_extension):
                filepath = os.path.join(self.root_directory, filename)
                yield filepath

    def check_plot_area_background(self, root):
        for error in root.xpath("//plot_area_background_color/color[not(@name) or not(starts-with(@name, 'ISIS_'))]"):
            err = "Error on line " + str(error.sourceline) + ": " + etree.tostring(error) \
                  + "\n... The plot area background color wasn't the ISIS_* color scheme"
            self.errors.append(err)

    def check_opi_label_fonts(self, root):
        namelabel_xpath = "//widget[@typeId='org.csstudio.opibuilder.widgets.Label']"

        for error in root.xpath(
                        namelabel_xpath + "/font/opifont.name[not(starts-with(., 'ISIS_')) and not(starts-with(@fontName, 'ISIS_'))]"):
            err = "Error on line " + str(error.sourceline) + ": " + etree.tostring(error) \
                  + "\n... The font must be an ISIS_* font"
            self.errors.append(err)

    def check_led_colours(self, root):
        led_xpath = "//widget[@typeId='org.csstudio.opibuilder.widgets.LED']"

        for error in root.xpath(led_xpath + "/on_color/color[not(@name)]"):
            err = "Error on line " + str(error.sourceline) + ": " + etree.tostring(error) \
                  + "\n... An LED indicator didn't use a correct ISIS colour scheme when turned on."
            self.errors.append(err)

        for error in root.xpath(led_xpath + "/on_color/color[@name!='ISIS_Green_LED_On' and @name!='ISIS_Red_LED_On']"):
            err = "Error on line " + str(error.sourceline) + ": " + etree.tostring(error) \
                  + "\n... An LED indicator didn't use a correct ISIS colour scheme when turned on."
            self.errors.append(err)

        for error in root.xpath(led_xpath + "/off_color/color[not(@name)]"):
            err = "Error on line " + str(error.sourceline) + ": " + etree.tostring(error) \
                  + "\n... An LED indicator didn't use a correct ISIS colour scheme when turned off."
            self.errors.append(err)

        for error in root.xpath(
                        led_xpath + "/off_color/color[@name!='ISIS_Green_LED_Off' and @name!='ISIS_Red_LED_Off']"):
            err = "Error on line " + str(error.sourceline) + ": " + etree.tostring(error) \
                  + "\n... An LED indicator didn't use a correct ISIS colour scheme when turned off."
            self.errors.append(err)

    def check_text_input_colors(self, root):
        text_input_xpath = "//widget[@typeId='org.csstudio.opibuilder.widgets.TextInput']"

        for error in root.xpath(text_input_xpath + "/background_color/color[not(@name) or @name!='ISIS_Textbox_Background']"):
            err = "Error on line " + str(error.sourceline) + ": " + etree.tostring(error) \
                  + "\n... A text input field didn't use ISIS_Textbox_Background as it's background color."
            self.errors.append(err)

        for error in root.xpath(
                        text_input_xpath + "/foreground_color/color[not(@name) or @name!='ISIS_Standard_Text']"):
            err = "Error on line " + str(error.sourceline) + ": " + etree.tostring(error) \
                  + "\n... A text input field didn't use ISIS_Standard_Text as it's foreground color."
            self.errors.append(err)

        for error in root.xpath(
                        text_input_xpath + "/border_color/color[not(@name) or not(starts-with(@name, 'ISIS_'))]"):
            err = "Error on line " + str(error.sourceline) + ": " + etree.tostring(error) \
                  + "\n... A text input field didn't use ISIS_* color as it's border color."
            self.errors.append(err)

    def check_items_are_in_grouping_containers(self, root):
        button_xpath = \
            "//widget[@typeId='org.csstudio.opibuilder.widgets.NativeButton' " \
            "and not(ancestor::widget[@typeId='org.csstudio.opibuilder.widgets.groupingContainer'])]"
        led_xpath = \
            "//widget[@typeId='org.csstudio.opibuilder.widgets.LED' " \
            "and not(ancestor::widget[@typeId='org.csstudio.opibuilder.widgets.groupingContainer'])]"
        dropdown_xpath = \
            "//widget[@typeId='org.csstudio.opibuilder.widgets.combo' " \
            "and not(ancestor::widget[@typeId='org.csstudio.opibuilder.widgets.groupingContainer'])]"

        for error in root.xpath(button_xpath):
            err = "Error on line " + str(error.sourceline) + ": " + etree.tostring(error) \
                  + "\n... A button was not within a grouping container."
            self.errors.append(err)

        for error in root.xpath(led_xpath):
            err = "Error on line " + str(error.sourceline) + ": " + etree.tostring(error) \
                  + "\n... An LED indicator was not within a grouping container."
            self.errors.append(err)

        for error in root.xpath(dropdown_xpath):
            err = "Error on line " + str(error.sourceline) + ": " + etree.tostring(error) \
                  + "\n... An dropdown menu was not within a grouping container."
            self.errors.append(err)

    def check_capitals_for_grouping_containers(self, root):
        container_name_xpath = "//widget[@typeId='org.csstudio.opibuilder.widgets.groupingContainer']/name"

        for name in root.xpath(container_name_xpath):
            capitalisation_error = False
            words = name.text.split()

            # Ignore words less than 4 characters as these are probably prepositions
            for word in words:
                if len(word)>=4 and word.title() != word:
                    capitalisation_error = True

            if capitalisation_error:
                err = "Error on line " + str(name.sourceline) + ": " + etree.tostring(name) \
                      + "\n... Grouping container titles should be in 'Proper Case (except prepositions)'."
                self.errors.append(err)

    def run(self):

        self.errors = []
        build_contains_errors = False

        for filepath in self.file_iterator():

            root = self.get_tree(filepath)

            self.check_led_colours(root)
            self.check_opi_label_fonts(root)
            self.check_plot_area_background(root)
            self.check_text_input_colors(root)
            self.check_items_are_in_grouping_containers(root)
            self.check_capitals_for_grouping_containers(root)

            if len(self.errors) > 0:
                print "\n --------------"
                print str(len(self.errors)) + " errors found in file " + filepath
                print " --------------\n"
                build_contains_errors = True
                for error in self.errors:
                    print error

            self.errors = []

        if build_contains_errors:
            sys.exit(1)
        else:
            sys.exit(0)

if __name__ == "__main__":
    checker = CheckOpiFormat()
    checker.run()