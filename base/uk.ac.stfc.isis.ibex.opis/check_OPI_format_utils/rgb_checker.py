import re

def populate_rgb_definitions(): 
    """
    Populates a dictionary with the definitions of all ISIS colours from isis_colours.def
    Arguments: 
        None
    Returns: 
        A populated dictionary: {str<name>: (str<red>, str<green>, str<blue>)}
    """
    colour_rgb_definitions = {} 
    with open("isis_colours.def") as f: 
        lines = [line.strip() for line in f]

    def add_definition(line):
        pattern = re.compile("^[a-zA-Z0-9_ ]+ = [0-9, ]+$") 
        if pattern.match(line):
            split_line = line.split(" = ")
            colour_name = split_line[0]
            rgb_values = tuple(split_line[1].split(","))
            colour_rgb_definitions[colour_name] = rgb_values
    
    for line in lines:
        add_definition(line)    
    return colour_rgb_definitions

def check_rgb_definitions(root):
    """
    Checks that all <color> XML tags with a name attribute have RGB values consistent with their definition in isis_colours.def
    Arguments: 
        root: the root tag of the xml file
    Returns: 
        *ADD ERROR RETURN FUNCTIONALITY*
    """
    colour_rgb_definitions = populate_rgb_definitions()

    def check_definition(color):
        attributes = color.attrib 
        if "name" not in attributes:
            return True
        name = attributes["name"] # Key error for "Major" - not in definitions but used in <color> tag?
        actual_colors = (attributes["red"], attributes["green"], attributes["blue"])
        rgb_definition = colour_rgb_definitions[name]
        return actual_colors == rgb_definition
    
    for color in root.iter("color"):
        if not check_definition(color):
            print("Mismatch with definition")

###
# Testing functions work on fermi_chopper.opi
import xml.etree.ElementTree as ET
tree = ET.parse('../resources/fermi_chopper.opi')
root = tree.getroot()
check_rgb_definitions(root)
