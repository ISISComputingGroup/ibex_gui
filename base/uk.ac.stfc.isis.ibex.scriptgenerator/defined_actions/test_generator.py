import unittest
from mock import MagicMock
from action_loader import Generator, Config
from typing import Dict, AnyStr
from hamcrest.core import assert_that, equal_to

class TestGenerator(unittest.TestCase):

    def setUp(self):
        self.generator: Generator = Generator()

    def test_GIVEN_config_return_invalid_WHEN_get_generator_invalidity_reasons_THEN_context_as_expected(self):
        # Arrange
        mock_config: Config = MagicMock()
        mock_config.parametersValid.return_value = "invalid reason"
        validityCheck: Dict[int, AnyStr] = self.generator.areParamsValid([
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
        validityCheck: Dict[int, AnyStr] = self.generator.areParamsValid([
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
        validityCheck: Dict[int, AnyStr] = self.generator.areParamsValid([
            {"param1": "param1Val"}, {"param2": "param2Val"}
        ], mock_config)
        # Act and Assert
        assert_that(validityCheck, equal_to({1: "invalid"}), "2 actions, one invalid with reason")


if __name__ == '__main__':
    unittest.main()
