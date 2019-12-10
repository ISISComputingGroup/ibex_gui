from abc import abstractmethod, ABC
from typing import Union, AnyStr
import six
import sys
import functools


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

def cast_parameters_to(*args_casts, **keyword_casts):
    def decorator(func):
        @functools.wraps(func)
        def wrapper(self, *args, **kwargs):
            casting_failures: str = ""
            cast_kwargs={}
            for k, v in six.iteritems(kwargs):
                try:
                    cast_kwargs[k] = keyword_casts[k](v)
                except ValueError:
                    casting_failures += "Cannot convert {} from string to {}\n".format(str(v), str(keyword_casts[k]))
            cast_args = []
            for i in range(0, len(args_casts)):
                try:
                    cast_args.append(args_casts[i](args[i]))
                except ValueError:
                    casting_failures += "Cannot convert {} from string to {}\n".format(args[i], str(args_casts[i]))
            if casting_failures != "":
                return casting_failures
            return func(self, *cast_args, **cast_kwargs)
        return wrapper
    return decorator

