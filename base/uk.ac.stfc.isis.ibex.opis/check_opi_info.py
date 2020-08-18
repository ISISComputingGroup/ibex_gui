import os
import win32api
import xml.etree.ElementTree as ET
import unittest

DEFAULT_ROOT_DIR = os.path.join(os.path.dirname(__file__), "resources")


class TestOpiInfo(unittest.TestCase):
    def _check_path_valid(self, path_str):
        full_path = os.path.join(DEFAULT_ROOT_DIR, path_str)
        # check path exists
        if not os.path.exists(full_path):
            return ["opi_info.xml error: {} was not found".format(path_str)]

        # check path has no windows-style backslashes
        if '\\' in path_str:
            return ['opi_info.xml error: "{}" contains windows-style backslash (\\)!'.format(path_str)]

        # check file case sensitivity on xml and system
        filesystem_path = win32api.GetLongPathName(win32api.GetShortPathName(full_path)).split("resources\\")[1]
        if path_str != filesystem_path:
            return ['opi_info.xml error: info path does not match filesystem path - "{}", should be "{}"'
                    .format(path_str, filesystem_path)]

        return []

    def test_opi_info(self):
        file_path = os.path.join(DEFAULT_ROOT_DIR, "opi_info.xml")

        # if opi_info.xml was not found
        if not os.path.exists(file_path):
            self.fail("Could not find file path {}".format(file_path))

        tree = ET.parse(file_path)
        root = tree.getroot()
        errors = []
        for entry in root.iter('path'):
            errors.extend(self.check_path_valid(entry.text))

        if errors:
            self.fail("\n" + "\n".join(errors))
