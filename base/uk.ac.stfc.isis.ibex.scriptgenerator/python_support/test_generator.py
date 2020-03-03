import unittest
from mock import MagicMock
from script_definition_loader import Generator, ScriptDefinitionWrapper
from typing import Dict, AnyStr, List
from hamcrest.core import assert_that, equal_to
from hamcrest.library.text import matches_regexp
from test_script_definitions.valid_script_definition import DoRun as ValidDoRun
from pprint import pprint

class TestGenerator(unittest.TestCase):

    def setUp(self):
        self.generator: Generator = Generator(search_folders=["test_script_definitions"])

    def test_GIVEN_script_definitions_return_invalid_WHEN_get_generator_invalidity_reasons_THEN_context_as_expected(self):
        # Arrange
        mock_script_definition: ScriptDefinitionWrapper = MagicMock()
        mock_script_definition.parametersValid.return_value = "invalid reason"
        validityCheck: Dict[int, AnyStr] = self.generator.getValidityErrors([
            {"param1": "param1Val", "param2": "param2Val"}, 
            {"param1": "param1Val", "param2": "param2Val"}
        ], mock_script_definition)
        # Act and Assert
        assert_that(validityCheck, equal_to({0: "invalid reason", 1: "invalid reason"}), 
        "2 actions, both invalid with reason")

    def test_GIVEN_script_definition_return_valid_WHEN_get_generator_invalidity_reasons_THEN_no_content(self):
        # Arrange
        mock_script_definition: ScriptDefinitionWrapper = MagicMock()
        mock_script_definition.parametersValid.return_value = None
        validityCheck: Dict[int, AnyStr] = self.generator.getValidityErrors([
            {"param1": "param1Val", "param2": "param2Val"}, 
            {"param1": "param1Val", "param2": "param2Val"}
        ], mock_script_definition)
        # Act and Assert
        assert_that(validityCheck, equal_to({}), "2 actions, both invalid with reason")

    # Allows us to return whether invalid or not based on passed vals
    def params_valid_side_effect(self, params) -> AnyStr:
        if params == {"param1": "param1Val"}:
            return None
        else:
            return "invalid"

    def test_GIVEN_script_definition_return_valid_and_invalid_WHEN_get_generator_invalidity_reasons_THEN_content_as_expected(self):
        # Arrange
        mock_script_definition: ScriptDefinitionWrapper = MagicMock()
        mock_script_definition.parametersValid.side_effect = self.params_valid_side_effect
        validityCheck: Dict[int, AnyStr] = self.generator.getValidityErrors([
            {"param1": "param1Val"}, {"param2": "param2Val"}
        ], mock_script_definition)
        # Act and Assert
        assert_that(validityCheck, equal_to({1: "invalid"}), "2 actions, one invalid with reason")
    
    def test_GIVEN_valid_script_definition_WHEN_generate_THEN_new_script_is_as_expected(self):
        # Arrange
        expected_script_lines: List[AnyStr] = \
            """# pylint: skip-file

from genie_python import genie as g

from genie_python.genie_script_generator import ScriptDefinition, cast_parameters_to

class DoRun(ScriptDefinition):

    @cast_parameters_to(param1=float, param2=float)
    def run(self, param1=0.0, param2=0.0):
        pass

    @cast_parameters_to(param1=float, param2=float)
    def parameters_valid(self, param1=0.0, param2=0.0):
        reason = ""
        if param1 != 1.0:
            reason += "param1 is not 1.0\\n"
        if param2 != 2.0:
            reason += "param2 is not 2.0\\n"
        if reason != "":
            return reason
        else:
            return None

    def get_help(self):
        return "Help"



def runscript():
    script_definition = DoRun()
    script_definition.run(**{'param1': '1', 'param2': '2'})
    script_definition.run(**{'param1': '1', 'param2': '2'})
    """.split("\n")
        params = [{"param1": "1", "param2": "2"}, {"param1": "1", "param2": "2"}]
        script_definition: ScriptDefinitionWrapper = ScriptDefinitionWrapper(name="valid_script_definition", script_definition=ValidDoRun())
        # Act
        generated_script: str = self.generator.generate(params, script_definition)
        # Assert
        self.assertIsNotNone(generated_script, "ScriptDefinitionWrapper is valid, so should not return None")

        generated_lines = generated_script.split("\n")
        assert_that(len(generated_lines), equal_to(len(expected_script_lines)), 
            "Should have the same amount of lines if they match")

        for i in range(len(generated_lines)):
            assert_that(generated_lines[i], equal_to(expected_script_lines[i]), 
                "Each line of the generated script should match the expected line")
    
    def test_GIVEN_invalid_script_definition_params_WHEN_generate_THEN_return_none(self):
        # Arrange
        params = [{"param1": "invalid", "param2": "2"}, {"param1": "1", "param2": "2"}]
        script_definition: ScriptDefinitionWrapper = ScriptDefinitionWrapper(name="valid_script_definition", script_definition=ValidDoRun())
        # Act
        generated_script: str = self.generator.generate(params, script_definition)
        # Assert
        self.assertIsNone(generated_script, "ScriptDefinitionWrapper is invalid, so should return None")

