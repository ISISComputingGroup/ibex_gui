import os
import win32api
import xml.etree.ElementTree as ET
import unittest

DEFAULT_ROOT_DIR = os.path.join(os.path.dirname(__file__), "resources")
OPI_INFO_PATH = os.path.join(DEFAULT_ROOT_DIR, "opi_info.xml")


class TestOpiInfo(unittest.TestCase):
    def _check_path_valid(self, path_str):
        """
        Checks that the given opi_info.xml entry path is valid.
        Args:
            path_str (str): The path to verify (of a device entry in the opi_info.xml file)
        Returns:
            string: The error message, or None if there is no error
        """
        full_path = os.path.join(DEFAULT_ROOT_DIR, path_str)
        # check path exists
        if not os.path.exists(full_path):
            return "opi_info.xml error: {} was not found".format(path_str)

        # check path has no windows-style backslashes
        if '\\' in path_str:
            return 'opi_info.xml error: "{}" contains windows-style backslash (\\)!'.format(path_str)

        # check file case sensitivity on xml and system
        filesystem_path = win32api.GetLongPathName(win32api.GetShortPathName(full_path)).split("resources\\")[1]
        if path_str != filesystem_path:
            return 'opi_info.xml error: info path does not match filesystem path - "{}", should be "{}"' \
                .format(path_str, filesystem_path)

        return None

    def test_opi_info(self):
        # if opi_info.xml was not found
        if not os.path.exists(OPI_INFO_PATH):
            self.fail("Could not find file path {}".format(OPI_INFO_PATH))

        tree = ET.parse(OPI_INFO_PATH)
        root = tree.getroot()
        errors = []
        for entry in root.iter('path'):
            err_msg = self._check_path_valid(entry.text)
            if err_msg:  # if an error message is returned
                errors.extend([err_msg])

        if errors:
            self.fail("\n" + "\n".join(errors))
