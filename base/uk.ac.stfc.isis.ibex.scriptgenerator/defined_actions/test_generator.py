import unittest
from mock import MagicMock
from action_loader import Generator, Config
from typing import Dict, AnyStr, List
from hamcrest.core import assert_that, equal_to
from hamcrest.library.text import matches_regexp
from test_configs.valid_config import DoRun as ValidDoRun
from pprint import pprint

class TestGenerator(unittest.TestCase):

    def setUp(self):
        self.generator: Generator = Generator(search_folder="test_configs")

    def test_GIVEN_config_return_invalid_WHEN_get_generator_invalidity_reasons_THEN_context_as_expected(self):
        # Arrange
        mock_config: Config = MagicMock()
        mock_config.parametersValid.return_value = "invalid reason"
        validityCheck: Dict[int, AnyStr] = self.generator.checkParamValidity([
            {"param1": "param1Val", "param2": "param2Val"}, 
            {"param1": "param1Val", "param2": "param2Val"}
        ], mock_config)
        # Act and Assert
        assert_that(validityCheck, equal_to({0: "invalid reason", 1: "invalid reason"}), 
        "2 actions, both invalid with reason")

    def test_GIVEN_config_return_valid_WHEN_get_generator_invalidity_reasons_THEN_no_content(self):
        # Arrange
        mock_config: Config = MagicMock()
        mock_config.parametersValid.return_value = None
        validityCheck: Dict[int, AnyStr] = self.generator.checkParamValidity([
            {"param1": "param1Val", "param2": "param2Val"}, 
            {"param1": "param1Val", "param2": "param2Val"}
        ], mock_config)
        # Act and Assert
        assert_that(validityCheck, equal_to({}), "2 actions, both invalid with reason")

    # Allows us to return whether invalid or not based on passed vals
    def params_valid_side_effect(self, params: Dict[AnyStr, AnyStr]) -> AnyStr:
        if params == {"param1": "param1Val"}:
            return None
        else:
            return "invalid"

    def test_GIVEN_config_return_valid_and_invalid_WHEN_get_generator_invalidity_reasons_THEN_content_as_expected(self):
        # Arrange
        mock_config: Config = MagicMock()
        mock_config.parametersValid.side_effect = self.params_valid_side_effect
        validityCheck: Dict[int, AnyStr] = self.generator.checkParamValidity([
            {"param1": "param1Val"}, {"param2": "param2Val"}
        ], mock_config)
        # Act and Assert
        assert_that(validityCheck, equal_to({1: "invalid"}), "2 actions, one invalid with reason")
    
    def test_GIVEN_valid_config_WHEN_generate_THEN_new_script_is_as_expected(self):
        # Arrange
        expected_script_lines: List[AnyStr] = [r"from genie_python import genie as g", r"", r"from genie_python.genie_script_generator import ActionDefinition, cast_parameters_to", r"", 
            r"class DoRun\(ActionDefinition\):", r"", r"[\s]+@cast_parameters_to\(param1=float, param2=float\)", r"[\s]+def run\(self, param1=0\.0, param2=0\.0\):", r"[\s]+pass", r"",
            r"[\s]+@cast_parameters_to\(param1=float, param2=float\)", r"[\s]+def parameters_valid\(self, param1=0\.0, param2=0\.0\):", r"[\s]+reason = \"\"",
            r"if param1 != 1\.0:", r"[\s]+reason \+= \"param1 is not 1\.0[\\n]\"",
            r"if param2 != 2\.0:", r"[\s]+reason \+= \"param2 is not 2\.0[\\n]\"", r"[\s]+if reason != \"\":",
            r"[\s]+return reason", r"[\s]+else:",  r"[\s]+return None", r"", r"", r"if __name__ == \"__main__\":",
            r"[\s]+config = DoRun\(\)", r"[\s]+config\.run\(\*\*\{\"param1\": \"1\", \"param2\": \"2\"\}\)",
            r"[\s]+config\.run\(\*\*\{\"param1\": \"1\", \"param2\": \"2\"\}\)", r""]
        params = [{"param1": "1", "param2": "2"}, {"param1": "1", "param2": "2"}]
        config: Config = Config(name="valid_config", action=ValidDoRun())
        # Act
        generated_script: str = self.generator.generate(params, config)
        # Assert
        self.assertIsNotNone(generated_script, "Config is valid, so should not return None")

        generated_lines = generated_script.split("\n")
        assert_that(len(generated_lines), equal_to(len(expected_script_lines)), 
            "Should have the same amount of lines if they match")

        for i in range(len(generated_lines)):
            assert_that(generated_lines[i], matches_regexp(expected_script_lines[i]), 
            "Each line of the generated script should match the expected line")
    
    def test_GIVEN_invalid_config_params_WHEN_generate_THEN_return_none(self):
        # Arrange
        params = [{"param1": "invalid", "param2": "2"}, {"param1": "1", "param2": "2"}]
        config: Config = Config(name="valid_config", action=ValidDoRun())
        # Act
        generated_script: str = self.generator.generate(params, config)
        # Assert
        self.assertIsNone(generated_script, "Config is invalid, so should return None")


if __name__ == '__main__':
    unittest.main()
