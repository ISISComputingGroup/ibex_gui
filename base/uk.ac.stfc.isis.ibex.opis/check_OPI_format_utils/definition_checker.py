import re
import os

class RGBDefinitionChecker():
    def __init__(self, element, definition) -> None:
        self.attributes = element.attrib
        self.definition = definition
        
    def check_match(self):
        if not all(attribute in self.attributes for attribute in ("name", "red", "green", "blue")):
            return True
        actual_colors = (self.attributes["red"], self.attributes["green"], self.attributes["blue"])
        return actual_colors == self.definition

class FontDefinitionChecker(): 
    def __init__(self, element, definition) -> None:
        self.attributes = list(element)[0].attrib
        self.definition = definition

    def check_match(self):
        name = self.attributes["fontName"]
        style = "regular" if self.attributes["style"] == "0" else "bold"
        size = self.attributes["height"]
        return "-".join((name, style, size)) == self.definition

class Checker():
    """
    This is the general checker for incorrect definitions in tags.
    """
    def __init__(self, root, definitions) -> None:
        self.root = root 
        self.definitions = definitions
        self.incorrect_elements = [] 

    def check_definitions(self, tag_type): 
        for tag in self.root.iter(tag_type):
            if tag_type == "color" and "name" in tag.attrib: 
                colour_name = tag.attrib["name"]
                if colour_name not in self.definitions:
                    continue
                definition = self.definitions[colour_name]
                tag_checker = RGBDefinitionChecker(tag, definition)
                if not tag_checker.check_match():
                    self.incorrect_elements.append(tag)
            
            if tag_type == "font":
                font_name = tag.findtext("opifont.name")
                if font_name not in self.definitions:
                    continue
                definition = self.definitions[font_name]
                tag_checker = FontDefinitionChecker(tag, definition)
                if not tag_checker.check_match():
                    self.incorrect_elements.append(tag)
    
    def output_errors(self, tag_type):
        errors = []
        for element in self.incorrect_elements:
            if tag_type == "color":
                name = element.attrib["name"]
            if tag_type == "font":
                name = element.findtext("opifont.name")
            print(element.sourceline, name)
            errors.append((element.sourceline, name))
        return errors

class DefinitionPopulator():
    """
    This is where the definitions dictionary to be checked against can be populated.
    """
    def __init__(self, regex_pattern, file_path) -> None:
        self.regex_pattern = regex_pattern
        self.file_path = file_path
        self.definitions = {}

    def populate_definitions(self):
        with open(self.file_path) as f: 
            for line in f.readlines():
                line = line.strip()
                if self.regex_pattern.match(line):
                    split_line = line.split(" = ")
                    definition_name = split_line[0]
                    definition = split_line[1] if len(split_line[1].split(",")) != 3 else tuple(split_line[1].split(","))
                    self.definitions[definition_name] = definition    
        return self.definitions

class DefinitionChecker():
    """
    Class containing full RGB definition checking and error output for an OPI file.
    """
    def __init__(self, root, tag_type) -> None:
        self.root = root
        self.tag_type = tag_type
        self.regex_pattern = re.compile("^[a-zA-Z0-9_ ]+ = [0-9, ]+$") if tag_type == "color" else re.compile("^[a-zA-Z0-9_ ()]+ = [a-zA-Z0-9- ]+$")
        self.definition_file_path = os.path.join("/Instrument/Apps/EPICS/CSS/master/Share", "isis_colours.def") if tag_type == "color" else os.path.join("/Instrument/Apps/EPICS/CSS/master/Share", "isis_fonts.def")

    def check_and_output_errors(self):
        definition_populator = DefinitionPopulator(self.regex_pattern, self.definition_file_path)
        definition_populator.populate_definitions()
        checker_rgb = Checker(self.root, definition_populator.definitions)
        checker_rgb.check_definitions(self.tag_type)
        return checker_rgb.output_errors(self.tag_type)

