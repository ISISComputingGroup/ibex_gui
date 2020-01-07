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
            None if run runs without exception, otherwise a String containing the error message

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
    """
    A decorator to cast args and kwargs using the passed casters.

    Args:
        *args_casts: An ordered list of casters to use to case args.
        **kwargs_casts: A dictionary of casters to use to cast kwargs.
    
    Returns:
        return value of wrapped function or a string containing an error message if an error occurs.

    Examples:
        Cast run parameters (run only takes kwargs currently):

        >>> @cast_parameters_to(kwarg1=int, kwarg2=custom_caster, kwarg3=float, kwarg4=str)
        >>> def run(kwarg1="10", kwarg2="keyword", kwarg3="1.0", kwarg4="mystr")

        Cast parameters_valid parameters (parameters_valid only takes kwargs currently):

        >>> @cast_parameters_to(kwarg1=int, kwarg2=custom_caster, kwarg3=float, kwarg4=str)
        >>> def do_run(kwarg1="10", kwarg2="keyword", kwarg3="1.0", kwarg4="mystr")

        Cast a subset of parameters:

        >>> @cast_parameters_to(kwarg1=int, kwarg3=float)
        >>> def do_run(kwarg1="10", kwarg2="keyword", kwarg3="1.0", kwarg4="mystr")

        Error string returned when more args_casts than args:

        >>> @cast_parameters_to(int, custom_caster, float, str)
        >>> def func(arg1, arg2)
        >>> Returned: There are more casters for arguments than arguments

        Error string returnd when keyword_casts contains keys not present in kwargs:

        >>> @cast_parameters_to(kwarg1=int, kwarg2=custom_caster, kwargs3=float, kwarg4=str)
        >>> def func(kwarg1="10", kwarg2="20")
        >>> Returned: There are more casters for arguments than arguments

        Error string returned when cannot cast:

        >>> @cast_parameters_to(int)
        >>> def run("mystr")
        >>> Returned: "Cannot convert mystr from string to int"
    """
    def decorator(func):
        @functools.wraps(func)
        def wrapper(self, *args, **kwargs):
            # Check for incorrect keyword_casts and args_casts 
            if not set(keyword_casts.keys()).issubset(set(kwargs.keys())):
                return "Keyword argument casters contains keys that are not present in keyword arguments"
            if len(args_casts) > len(args):
                return "There are more casters for arguments than arguments"
            # Cast kwargs
            casting_failures: str = ""
            cast_kwargs={}
            for k, v in six.iteritems(kwargs):
                try:
                    cast_kwargs[k] = keyword_casts[k](v)
                except ValueError:
                    casting_failures += "Cannot convert {} from string to {}\n".format(str(v), str(keyword_casts[k]))
            # Cast args
            cast_args = []
            for i in range(len(args_casts)):
                try:
                    cast_args.append(args_casts[i](args[i]))
                except ValueError:
                    casting_failures += "Cannot convert {} from string to {}\n".format(args[i], str(args_casts[i]))
            if casting_failures != "":
                return casting_failures
            return func(self, *cast_args, **cast_kwargs)
        return wrapper
    return decorator
