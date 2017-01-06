import os
import sys
from lxml import etree

root_directory = r"C:\Instrument\Dev\ibex_gui\base\uk.ac.stfc.isis.ibex.opis\resources"
file_extension = r".opi"

errors = []
build_contains_errors = False


def get_tree(filepath):
    with open(filepath) as opi_file:
        parser = etree.XMLParser(remove_blank_text=True)
        return etree.parse(opi_file, parser)


def file_iterator():
    for filename in os.listdir(root_directory):
        if filename.endswith(file_extension):
            filepath = os.path.join(root_directory, filename)
            yield filepath


def check_plot_area_background(root):
    for error in root.xpath("//plot_area_background_color/color[not(@name)]"):
        err = "Error on line " + str(error.sourceline) + ": " + etree.tostring(error) \
              + "\n--> The plot area background color wasn't the ISIS_* color scheme"
        errors.append(err)

    for error in root.xpath("//plot_area_background_color/color[not(@name)]"):
        err = "Error on line " + str(error.sourceline) + ": " + etree.tostring(error) \
              + "\n--> The plot area background color wasn't the ISIS_* color scheme"
        errors.append(err)


def check_OPI_label_fonts(root):
    namelabel_xpath = "//widget[@typeId='org.csstudio.opibuilder.widgets.Label']"

    for error in root.xpath(namelabel_xpath + "/font/opifont.name[not(@fontName)]"):
        err = "Error on line " + str(error.sourceline) + ": " + etree.tostring(error) \
              + "\n--> The font must be an ISIS_* font"
        errors.append(err)

    for error in root.xpath(namelabel_xpath + "/font/opifont.name[@fontName!='ISIS_*' and /text()!='ISIS_*']"):
        err = "Error on line " + str(error.sourceline) + ": " + etree.tostring(error) \
              + "\n--> The font must be an ISIS_* font"
        errors.append(err)


def check_LED_colours(root):
    led_xpath = "//widget[@typeId='org.csstudio.opibuilder.widgets.LED']"

    for error in root.xpath(led_xpath + "/on_color/color[not(@name)]"):
        err = "Error on line " + str(error.sourceline) + ": " + etree.tostring(error) \
              + "\n--> An LED indicator didn't use a correct ISIS colour scheme when turned on."
        errors.append(err)

    for error in root.xpath(led_xpath + "/on_color/color[@name!='ISIS_Green_LED_On' and @name!='ISIS_Red_LED_On']"):
        err = "Error on line " + str(error.sourceline) + ": " + etree.tostring(error) \
              + "\n--> An LED indicator didn't use a correct ISIS colour scheme when turned on."
        errors.append(err)

    for error in root.xpath(led_xpath + "/off_color/color[not(@name)]"):
        err = "Error on line " + str(error.sourceline) + ": " + etree.tostring(error) \
              + "\n--> An LED indicator didn't use a correct ISIS colour scheme when turned off."
        errors.append(err)

    for error in root.xpath(led_xpath + "/off_color/color[@name!='ISIS_Green_LED_Off' and @name!='ISIS_Red_LED_Off']"):
        err = "Error on line " + str(error.sourceline) + ": " + etree.tostring(error) \
              + "\n--> An LED indicator didn't use a correct ISIS colour scheme when turned off."
        errors.append(err)


for filepath in file_iterator():
    root = get_tree(filepath)

    check_LED_colours(root)
    check_OPI_label_fonts(root)
    check_plot_area_background(root)

    if len(errors) > 0:
        print "\n --------------"
        print str(len(errors)) + " errors found in file " + filepath
        print " --------------\n"
        build_contains_errors = True
        for error in errors:
            print error

    errors = []

if build_contains_errors:
    sys.exit(1)
else:
    sys.exit(0)