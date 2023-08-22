import argparse
from lxml import etree
from check_OPI_format_utils.rgb_checker import check_rgb_definitions, populate_rgb_definitions
from check_opi_format import file_iterator


# Directory to iterate through
DEFAULT_ROOT_DIR = r"./resources/"


def update_rgb_values(file_name):
    """
        Updates any incorrect RGB values in each <color> tag in an OPI XML file.

        Arguments:
            file_name (str):
    """
    rgb_definitions = populate_rgb_definitions()
    tree = etree.parse(file_name)
    root = tree.getroot()
    errors = check_rgb_definitions(root)

    for color in root.iter("color"):
        if "name" not in color.attrib:
            continue
        line_number = color.sourceline
        name = color.attrib["name"]
        if (line_number, name) in errors:
            correct_colours = rgb_definitions[name]
            color.set("red", correct_colours[0])
            color.set("green", correct_colours[1])
            color.set("blue", correct_colours[2])
    # tree.write(file_name) # Commented out to avoid unwantedly updating files during testing

if __name__ == "__main__":
    parser = argparse.ArgumentParser(prog='update_opi_rgb_values')
    parser.add_argument("-file", type=str, default=None,
                        help='A single file to update')
    parser.add_argument("-directory", type=str, default=DEFAULT_ROOT_DIR,
                        help='A directory to update files in')
    args = parser.parse_args()
    single_file = args.file
    root_dir = args.directory
    for file_name in file_iterator(root_dir, single_file):
        print("Updating incorrect RGB values in '{}'".format(file_name))
        update_rgb_values(file_name)

