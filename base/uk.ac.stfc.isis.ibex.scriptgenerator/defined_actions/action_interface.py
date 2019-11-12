from abc import abstractmethod, ABC
from typing import Union, AnyStr


class ActionDefinition(ABC):
    @abstractmethod
    def run(self, *args) -> None:
        """
        Defines the steps of an action which can be used in the script generator.

        Args:
            *args: The parameters of this action

        Returns:
            None

        """
        pass

    @abstractmethod
    def parameters_valid(self, *args) -> Union[None, AnyStr]:
        """
        Contains tests to check whether a given set of inputs is valid

        Args:
            *args: The parameters for this action

        Returns:
            None if all parameters are valid, otherwise a String containing an error message.

        """
        pass
