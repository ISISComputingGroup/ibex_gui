import sys
import os
import xml.etree.ElementTree

SUCCESS = 0
INCORRECT_ARGS = 1
NO_BUILD_PROPERTIES = 2
ICONS_NOT_INCLUDED = 3
ICON_LOCATION_POINTER_WRONG = 4

exceptions = ["ibex.client.product", "ibex.example", "uk.ac.stfc.isis.ibex.e4.client.product", "uk.ac.stfc.isis.scriptgenerator.client.product", "uk.ac.stfc.isis.ibex.opis\\resources"]
icon_location = "platform:/plugin/uk.ac.stfc.isis.ibex.e4.product/icons/IBEX-icon-web16.gif," \
                "platform:/plugin/uk.ac.stfc.isis.ibex.e4.product/icons/IBEX-icon-web32.gif," \
                "platform:/plugin/uk.ac.stfc.isis.ibex.e4.product/icons/IBEX-icon-web48.gif," \
                "platform:/plugin/uk.ac.stfc.isis.ibex.e4.product/icons/IBEX-icon-web64.gif"


# Walks only a certain number of levels through the directory
def walklevel(some_dir, level=1):
    some_dir = some_dir.rstrip(os.path.sep)
    assert os.path.isdir(some_dir)
    num_sep = some_dir.count(os.path.sep)
    for root, dirs, files in os.walk(some_dir):
        yield root, dirs, files
        num_sep_this = root.count(os.path.sep)
        if num_sep + level <= num_sep_this:
            del dirs[:]


def check_in_exceptions(root):
    for exp in exceptions:
        if root.endswith(exp):
            return True
    return False


def check_icons_in_build_properties(root):
    with open(os.path.join(root, "build.properties")) as f:
        contents = f.read()
        if "icons" not in contents:
            return False
    return True


def check_icon_location_pointer_correct(root):
    tree = xml.etree.ElementTree.parse(os.path.join(root, "plugin.xml"))
    root = tree.getroot()
    return len(root.findall(".//property[@value='{}']".format(icon_location))) == 1


def main(build_root_path):
    try:
        os.chdir(build_root_path)
    except Exception as e:
        return INCORRECT_ARGS
    for root, dirs, files in walklevel(build_root_path, 2):
        if "icons" in dirs:
            if check_in_exceptions(root):
                continue
            if "build.properties" not in files:
                print("NO BUILD PROPERTIES FOUND IN SAME FOLDER AS ICONS: " + str(root))
                return NO_BUILD_PROPERTIES
            if not check_icons_in_build_properties(root):
                print("ICONS FOLDER NOT INCLUDED IN BUILD PROPERTIES: " + str(root))
                return ICONS_NOT_INCLUDED
        if root.endswith("uk.ac.stfc.isis.ibex.e4.client"):
            if not check_icon_location_pointer_correct(root):
                print("RED SQUARE ERROR: ICON LOCATION INCORRECT IN {}".format(os.path.join(root, "plugin.xml")))
                return ICON_LOCATION_POINTER_WRONG
    return SUCCESS


if __name__ == '__main__':
    success = SUCCESS
    if len(sys.argv) != 2:
        print("Incorrect arguments, expected path to build base directory")
        success = INCORRECT_ARGS
    else:
        print("check_build: Build correct")
        success = main(sys.argv[1])

    sys.exit(success)
