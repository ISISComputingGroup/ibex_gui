import argparse
import os
from lxml import etree
from lxml.etree import LxmlError
from check_OPI_format_utils.definition_checker import Checker, DefinitionPopulator, COLOR_REGEX_PATTERN, FONT_REGEX_PATTERN, COLOR_FILE_PATH, FONT_FILE_PATH
from check_opi_format import file_iterator

DEFAULT_ROOT_DIR = r"./resources/"

class XmlParser():
    """
    Parses an XML file, producing a tree. Also provides functionality to write the current tree to a given file.
    """
    def __init__(self, file_name) -> None:
        self.file_name = file_name
        self.tree = None

    def get_root(self):
        try:
            self.tree = etree.parse(self.file_name)
            return self.tree.getroot()
        except LxmlError as e:
            print("XML failed to parse {}".format(e))

    def write_to_file(self):
        self.tree.write(self.file_name)


class UpdateColor():
    """
    Updates incorrect RGB values with the correct ones supplied by the definition file.
    """
    def __init__(self, element, definitions) -> None:
        self.element = element
        self.definition = definitions[element.attrib["name"]]

    def update(self):
            correct_colours = self.definition
            self.element.set("red", correct_colours[0])
            self.element.set("green", correct_colours[1])
            self.element.set("blue", correct_colours[2])


class UpdateFont():
    """
    Updates incorrect font value with the correct one supplied by the definition file.
    """
    def __init__(self, element, definitions) -> None:
        self.element = element
        self.definition = definitions[element.findtext("opifont.name")]

    def update(self):
        correct_attributes = self.definition.split("-")
        correct_attributes[1] = "1" if correct_attributes[1] == "bold" else "0"
        opi_font_tag = list(self.element)[0]
        opi_font_tag.set("fontName", correct_attributes[0])
        opi_font_tag.set("style", correct_attributes[1])
        opi_font_tag.set("height", correct_attributes[2])


class Updater():
    """
    Updates the attributes of incorrect color or font tags using the definition file.
    """
    def __init__(self, file_name, tag_type) -> None:
        self.tag_type = tag_type
        self.regex_pattern = COLOR_REGEX_PATTERN if tag_type == "color" else FONT_REGEX_PATTERN
        self.definition_file_path = COLOR_FILE_PATH if self.tag_type == "color" else FONT_FILE_PATH
        self.xmlParser = XmlParser(file_name)
        self.root = self.xmlParser.get_root()
        self.definition_populator = DefinitionPopulator(self.regex_pattern, self.definition_file_path)
        self.definition_populator.populate_definitions()
        self.definitions = self.definition_populator.definitions
        self.checker = Checker(self.root, self.definition_populator.definitions)
        self.checker.check_definitions(tag_type)
        self.incorrect_elements = self.checker.incorrect_elements
    
    def update_elements(self):
        for element in self.incorrect_elements:
            update_class = UpdateColor(element, self.definitions) if self.tag_type == "color" else UpdateFont(element, self.definitions)
            update_class.update()
        self.xmlParser.write_to_file()

def main():
    """
    Collects user input from command line to determine which opis to update and whether to update colors or fonts. 
    """
    confirmation = input("Update incorrect definitions in .opi file(s)? [y/n]")
    if confirmation not in ("y", "n"):
        print("Invalid input. No files updated")
    if confirmation == "y":
        parser = argparse.ArgumentParser(prog='update_opi_rgb_values')
        parser.add_argument("-file", type=str, default=None,
                            help='A single file to update')
        parser.add_argument("-directory", type=str, default=DEFAULT_ROOT_DIR,
                            help='A directory to update files in')
        parser.add_argument("-attribute", type=str, default=None)
        args = parser.parse_args()
        single_file = args.file
        root_dir = args.directory
        attribute_check = args.attribute
        for file_name in file_iterator(root_dir, single_file):
            print("Updating incorrect {} values in '{}'".format(attribute_check, file_name))
            updater = Updater(file_name, attribute_check)
            updater.update_elements()
    if confirmation == "n":
        print("Confirmation denied. No files updated.")

if __name__ == "__main__":
    main()