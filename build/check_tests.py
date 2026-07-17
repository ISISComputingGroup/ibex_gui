import os
import xml.etree.ElementTree as ET
import sys

SUCCESS = 0
INCORRECT_ARGS = 1
TESTS_NOT_IN_POM = 2
fileType=".tests"

def check_tests_in_pom(build_root_path):
    pathsxml = build_root_path + r"uk.ac.stfc.isis.ibex.client.tycho.parent\pom.xml"
    
    folderFiles = os.listdir(build_root_path)
    testFiles = [name for name in folderFiles if name.endswith(fileType)]
    
    tree = ET.parse(pathsxml)
    root = tree.getroot()
    xmlmodulenames = []
    for module in root.iter():
        if module.tag.endswith("module"):
            name = module.text[3:]
            if name.endswith(fileType):
                xmlmodulenames.append(name)
    missingtests = [file for file in testFiles if file not in xmlmodulenames]
    
    if missingtests:
        print(f".test files in base/ not found in pom.xml")
        print(f"Missing files: {missingtests}")
        print(f"To fix this: add missing files to the list in client.tycho.parent/pom.xml")
        return False
    else:
        return True

def main(build_root_path):
    try:
        os.chdir(build_root_path)
    except Exception as e:
        return INCORRECT_ARGS
    if not check_tests_in_pom(build_root_path):
        return TESTS_NOT_IN_POM
        print("check_tests: all test modules in pom.xml")
    return SUCCESS

if __name__ == '__main__':
    success = SUCCESS
    if len(sys.argv) != 2:
        print("Incorrect arguments, expected path to build base directory")
        success = INCORRECT_ARGS
    else:
        success = main(sys.argv[1])
    sys.exit(success)
