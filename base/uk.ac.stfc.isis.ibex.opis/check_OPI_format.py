import os
import sys
from lxml import etree

root_directory = r"C:\Instrument\Dev\ibex_gui\base\uk.ac.stfc.isis.ibex.opis\resources"
file_extension = r".opi"


def get_tree(filepath):
    with open(filepath) as opi_file:
        parser = etree.XMLParser(remove_blank_text=True)
        return etree.parse(opi_file, parser)


def file_iterator():
    for filename in os.listdir(root_directory):
        if filename.endswith(file_extension):
            filepath = os.path.join(root_directory, filename)
            print "\nAnalysing " + filepath
            yield filepath


for filepath in file_iterator():
    root = get_tree(filepath)
    errors = []
    build_contains_errors = False

    led_xpath = "//widget[@typeId='org.csstudio.opibuilder.widgets.LED']"
    namelabel_xpath = "//widget[@typeId='org.csstudio.opibuilder.widgets.Label']"

    for error in root.xpath("//plot_area_background_color/color[not(@name)]"):
        errors.append("Error on line " + str(error.sourceline) + ": " + etree.tostring(error))
        errors.append("  The plot area background color didn't use the ISIS_* color scheme")

    for error in root.xpath(namelabel_xpath + "/font/opifont.name[not(@fontName)]"):
        errors.append("Error on line " + str(error.sourceline) + ": " + etree.tostring(error))
        errors.append("  Must declare a font, which must be an ISIS_* font")

    for error in root.xpath(namelabel_xpath + "/font/opifont.name[@fontName!='ISIS_*' and /text()!='ISIS_*']"):
        errors.append("Error on line " + str(error.sourceline) + ": " + etree.tostring(error))
        errors.append("  The font must be an ISIS_* font")

    for error in root.xpath(namelabel_xpath + "/off_color/color[@name!='ISIS_Green_LED_Off' and @name!='ISIS_Red_LED_Off']"):
        errors.append("Error on line " + str(error.sourceline) + ": " + etree.tostring(error))
        errors.append("  An LED indicator didn't use a correct ISIS colour scheme when turned off.")

    for error in root.xpath(led_xpath + "/on_color/color[not(@name)]"):
        errors.append("Error on line " + str(error.sourceline) + ": " + etree.tostring(error))
        errors.append("  An LED indicator didn't use the ISIS colour scheme when turned on.")

    for error in root.xpath(led_xpath + "/on_color/color[@name!='ISIS_Green_LED_On' and @name!='ISIS_Red_LED_On']"):
        errors.append("Error on line " + str(error.sourceline) + ": " + etree.tostring(error))
        errors.append("  An LED indicator didn't use a correct ISIS colour scheme when turned on.")

    for error in root.xpath(led_xpath + "/off_color/color[not(@name)]"):
        errors.append("Error on line " + str(error.sourceline) + ": " + etree.tostring(error))
        errors.append("  An LED indicator didn't use the ISIS colour scheme when turned off.")

    for error in root.xpath(led_xpath + "/off_color/color[@name!='ISIS_Green_LED_Off' and @name!='ISIS_Red_LED_Off']"):
        errors.append("Error on line " + str(error.sourceline) + ": " + etree.tostring(error))
        errors.append("  An LED indicator didn't use a correct ISIS colour scheme when turned off.")


    if(len(errors) == 0):
        print "No errors found in file."
    else:
        build_contains_errors = True
        for error in errors:
            print error

if build_contains_errors:
    sys.exit(1)
else:
    sys.exit(0)