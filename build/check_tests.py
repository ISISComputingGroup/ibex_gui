import os
import sys
import xml.etree.ElementTree as ET

SUCCESS = 0
INCORRECT_ARGS = 1
TESTS_NOT_IN_POM = 2
FILE_TYPE = ".tests"


def check_tests_in_pom(ibex_gui_base):
    pathsxml = ibex_gui_base + r"uk.ac.stfc.isis.ibex.client.tycho.parent\pom.xml"

    folder_files = os.listdir(ibex_gui_base)
    test_files = [name for name in folder_files if name.endswith(FILE_TYPE)]

    tree = ET.parse(pathsxml)
    root = tree.getroot()
    xmlmodulenames = []
    for module in root.iter():
        if module.tag.endswith("module"):
            whole_name = module.text

            if whole_name:
                name = whole_name[3:]
            else:
                name = ""

            if name.endswith(FILE_TYPE):
                xmlmodulenames.append(name)
    missingtests = [file for file in test_files if file not in xmlmodulenames]

    if missingtests:
        print(".test files in base/ not found in pom.xml")
        print(f"Missing files: {missingtests}")
        print("To fix this: add missing files to the list in client.tycho.parent/pom.xml")
        return False
    else:
        return True


def main(ibex_gui_base):
    try:
        os.chdir(ibex_gui_base)
    except (FileNotFoundError, NotADirectoryError, PermissionError):
        return INCORRECT_ARGS
    if not check_tests_in_pom(ibex_gui_base):
        return TESTS_NOT_IN_POM
    print("check_tests: all test modules in pom.xml")
    return SUCCESS


if __name__ == "__main__":
    success = SUCCESS
    if len(sys.argv) != 2:
        print("Incorrect arguments, expected path to build base directory")
        success = INCORRECT_ARGS
    else:
        success = main(sys.argv[1])
    sys.exit(success)
