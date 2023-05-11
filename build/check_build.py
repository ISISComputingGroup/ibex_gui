import sys
import os
import xml.etree.ElementTree
import glob
import fnmatch

SUCCESS = 0
INCORRECT_ARGS = 1
NO_BUILD_PROPERTIES = 2
ICONS_NOT_INCLUDED = 3
ICON_LOCATION_POINTER_WRONG = 4
TEST_CLASSES_NOT_RUNNING = 5

exceptions = ["ibex.client.product", "ibex.example", "uk.ac.stfc.isis.ibex.e4.client.product", "uk.ac.stfc.isis.scriptgenerator.client.product"]
icon_location = "platform:/plugin/uk.ac.stfc.isis.ibex.e4.product/icons/IBEX-icon-web16.gif," \
                "platform:/plugin/uk.ac.stfc.isis.ibex.e4.product/icons/IBEX-icon-web32.gif," \
                "platform:/plugin/uk.ac.stfc.isis.ibex.e4.product/icons/IBEX-icon-web48.gif," \
                "platform:/plugin/uk.ac.stfc.isis.ibex.e4.product/icons/IBEX-icon-web64.gif"


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
    

# Checks that Test files are following the correct naming convention in ibex_gui\base\
def check_test_file_format(root):
    file_named_wrong = False
    test_files_all = []
    test_files_failing = []
    test_exts = ("*Test.java", "Test*.java", "*Tests.java", "*TestCase*.java", "package-info.java")

    #recursively search through the base\ directory for all .java files
    entries = glob.glob("**/*.java", root_dir=root, recursive=True)
    
    for e in entries:
        _, b = os.path.split(e)
        e_flag = False

        for ext in test_exts: 
            if fnmatch.fnmatch(b, ext):
                e_flag = True

        if not e_flag:
            file_path = os.path.join(root, e)
            if search_str(file_path, "@Test"):
                file_named_wrong = True
                test_files_failing.append(e)

    if file_named_wrong: 
        print("The following Test file does not follow correct naming convention: ")
        print(test_files_failing)


#Searches a file for a string, returns True if found
def search_str(file_path, string):
    with open(file_path, 'r') as file:
        content = file.read()
        if string in content:
            return True
        else:
            return False


def main(build_root_path):
    try:
        os.chdir(build_root_path)
    except Exception as e:
        return INCORRECT_ARGS
    
    for name in os.listdir(build_root_path):
        if name.endswith("test") or name.endswith("tests"):
            check_test_file_format(os.path.join(build_root_path, name))
        if os.path.isdir(os.path.join(build_root_path, name, "icons")):
            if check_in_exceptions(name):
                continue
            if not os.path.isfile(os.path.join(build_root_path, name, "build.properties")):
                print("NO BUILD PROPERTIES FOUND IN SAME FOLDER AS ICONS: " + str(name))
                return NO_BUILD_PROPERTIES
            if not check_icons_in_build_properties(os.path.join(build_root_path, name)):
                print("ICONS FOLDER NOT INCLUDED IN BUILD PROPERTIES: " + str(name))
                return ICONS_NOT_INCLUDED
        if name == "uk.ac.stfc.isis.ibex.e4.client":
            if not check_icon_location_pointer_correct(os.path.join(build_root_path, name)):
                print("RED SQUARE ERROR: ICON LOCATION INCORRECT IN {}".format(os.path.join(name, "plugin.xml")))
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
