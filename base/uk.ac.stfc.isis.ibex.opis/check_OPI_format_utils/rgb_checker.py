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

    def add_definition(line):
        line = line.strip()
        pattern = re.compile("^[a-zA-Z0-9_ ]+ = [0-9, ]+$") 
        if pattern.match(line):
            split_line = line.split(" = ")
            colour_name = split_line[0]
            rgb_values = tuple(split_line[1].split(","))
            colour_rgb_definitions[colour_name] = rgb_values
    
    with open("isis_colours.def") as f: 
        for line in f.readlines():
            add_definition(line)    
    return colour_rgb_definitions

def check_rgb_definitions(root):
    """
    Checks that all <color> XML tags with a name attribute have RGB values consistent with their definition in isis_colours.def

    Arguments: 
        root (etree): The root of the xml to search.
    Returns: 
        *MISMATCH SHOULD THROW AN ERROR, THAT WILL THEN ADD TO THE ERRORS ARRAY*
    """
    colour_rgb_definitions = populate_rgb_definitions()
    errors = [] 

    def check_definition(color):
        attributes = color.attrib
        if "name" not in attributes:
            return False
        name = attributes["name"]
        actual_colors = (attributes["red"], attributes["green"], attributes["blue"])
        try:
            rgb_definition = colour_rgb_definitions[name]
            if actual_colors != rgb_definition:
                return (color.sourceline, name)
        except: # Bypassing key error for colour names that are not defined in isis_colours.def - "Major" on fermi_chopper.opi
            print("Name not recognised in definitions file.")
    
    for color in root.iter("color"):
        error = check_definition(color)
        if error:
            errors.append(error)
    return errors

