import re
import os

class RGBDefinitionChecker():
    def __init__(self, element, definition) -> None:
        self.attributes = element.attrib
        self.definition = definition
        
    def check_match(self):
        if not all(attribute in self.attributes for attribute in ("name", "red", "green", "blue")): # If not all attributes exist, ignore color
            return True
        actual_colors = (self.attributes["red"], self.attributes["green"], self.attributes["blue"])
        return actual_colors == self.definition

class FontDefinitionChecker(): # To implement
    def __init__(self, element, definition) -> None:
        self.element = element
        self.definition = definition

    def check_match(self): # Check that the font name attribute matches the text of the widget.
        pass

class Checker():
    """
    This is the general checker for incorrect definitions in tags.
    """
    def __init__(self, root, definitions) -> None:
        self.root = root # Root node to iterate from
        self.definitions = definitions # Dictionary containing {name : definition} for .def line e.g RGB/Font
        self.incorrect_elements = [] # Incorrect elements attribute is a list that will be added to when checks are done.

    def check_definitions(self, tag_type): # This is the function that will check if the tag matches the definition.
        for tag in self.root.iter(tag_type):
            if tag_type == "color" and "name" in tag.attrib: 
                name = tag.attrib["name"]
                if name not in self.definitions:
                    continue
                definition = self.definitions[name]
                tag_checker = RGBDefinitionChecker(tag, definition)
                if not tag_checker.check_match():
                    self.incorrect_elements.append(tag)
            
            if tag_type == "font":
                pass # Implement font checking
    
    def output_errors(self):
        return [(element.sourceline, element.attrib["name"]) for element in self.incorrect_elements]

class DefinitionPopulator():
    """
    This is where the definitions dictionary to be checked against can be populated.
    """
    def __init__(self, pattern, file_path) -> None:
        self.pattern = pattern
        self.file_path = file_path
        self.definitions = {}

    def populate_definitions(self):
        with open(self.file_path) as f: 
            for line in f.readlines():
                line = line.strip()
                if self.pattern.match(line):
                    split_line = line.split(" = ")
                    definition_name = split_line[0]
                    definition = split_line[1] if len(split_line[1].split(",")) != 3 else tuple(split_line[1].split(","))
                    self.definitions[definition_name] = definition    
        return self.definitions

class DefinitionChecker(): #This will be updated to more generally apply to checking font or rgb definitions and outputting errors.
    """
    Class containing full RGB definition checking and error output for an OPI file.
    """
    def __init__(self, root) -> None:
        self.root = root
        self.rgb_definition_regex = re.compile("^[a-zA-Z0-9_ ]+ = [0-9, ]+$") 
        self.isis_colours_path = os.path.join("/Instrument/Apps/EPICS/CSS/master/Share", "isis_colours.def")

    def check_and_output_errors(self):
        rgb_populator = DefinitionPopulator(self.rgb_definition_regex, self.isis_colours_path)
        rgb_populator.populate_definitions()
        checker_rgb = Checker(self.root, rgb_populator.definitions)
        checker_rgb.check_definitions("color")
        return checker_rgb.output_errors()