import re
import os


def populate_rgb_definitions(): 
    """
    Populates a dictionary with the definitions of all ISIS colours from isis_colours.def
    
    Arguments: 
        None
    Returns: 
        A dictionary of colour definitions: {str<name>: (str<red>, str<green>, str<blue>)}
    """
    colour_rgb_definitions = {} 

    def add_definition(line):
        line = line.strip()
        pattern = re.compile("^[a-zA-Z0-9_ ]+ = [0-9, ]+$") 
        if pattern.match(line):
            split_line = line.split(" = ")
            colour_name = split_line[0]
            rgb_values = tuple(split_line[1].split(","))
            colour_rgb_definitions[colour_name] = rgb_values
    
    with open(os.path.join("/Instrument/Apps/EPICS/CSS/master/Share", "isis_colours.def")) as f: 
        for line in f.readlines():
            add_definition(line)    
    return colour_rgb_definitions

def check_rgb_definitions(root):
    """
    Checks that all <color> XML tags with a name attribute have RGB values consistent with their definition in isis_colours.def

    Arguments: 
        root (etree): The root of the xml to search.
    Returns: 
        list: A list of tuples (str<line number>, str<colour name>) for "color" tags that do not conform to their RGB definition
    """
    colour_rgb_definitions = populate_rgb_definitions()
    errors = [] 

    def check_definition(color):
        attributes = color.attrib
        if not all(attribute in attributes for attribute in ("name", "red", "green", "blue")): # If not all attributes exist, ignore color
            return False
        name = attributes["name"]
        actual_colors = (attributes["red"], attributes["green"], attributes["blue"])
        try:
            rgb_definition = colour_rgb_definitions[name]
            if actual_colors != rgb_definition:
                return (color.sourceline, name)
        except:
            print(f"'{name}' - colour not recognised from definitions file.")
    
    for color in root.iter("color"):
        error = check_definition(color)
        if error:
            errors.append(error)
    return errors

