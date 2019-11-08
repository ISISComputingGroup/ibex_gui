from py4j.clientserver import ClientServer, JavaParameters, PythonParameters
from py4j.java_collections import ListConverter
from action_interface import Action
from typing import Dict, AnyStr, Union
from inspect import signature
import inspect
import argparse
import os
import sys


class Config(object):
    """
    Exposes the definition and validate functions of a single action.
    """
    class Java:
        implements = ['uk.ac.stfc.isis.ibex.scriptgenerator.pythoninterface.Config']

    def __init__(self, name: str, action: Action):
        self.action = action
        self.name = name

    def getName(self) -> str:
        """
        Returns the name of this configuration

        Returns:
              name: String, the name of this configuration
        """
        return self.name

    def getParameters(self) -> list:
        """
        Gets the parameters from the action defined in this config

        Returns:
            arguments: List of the parameter names (strings)

        """
        arguments = signature(self.action.run).parameters

        return ListConverter().convert(arguments, gateway._gateway_client)

    def doAction(self, list_of_arguments) -> None:
        """Executes the action with the parameters provided"""
        self.action.run(*list_of_arguments)

    def parametersValid(self, list_of_arguments) -> Union[None, AnyStr]:
        """
        Checks if the parameters are valid for the configuration

        Returns:
            None if all parameters are valid, otherwise a String containing an error message.
        """
        return self.action.parameters_valid(*list_of_arguments)

    def equals(self, other_config) -> bool:
        """ Implement equals needed for py4j

        Returns:
            bool, True if other_config is equal to this instance, otherwise False.
        """
        return other_config.getName() == self.name
    

class ConfigWrapper(object):
    """
    Exposes the actions which have been found locally
    """
    class Java:
        implements = ['uk.ac.stfc.isis.ibex.scriptgenerator.pythoninterface.ConfigWrapper']

    def __init__(self, available_actions: Dict):
        self.actions = [Config(instrument, action()) for instrument, action in available_actions.items()]

    def getActions(self) -> list:
        """
        Returns a list of action names available locally

        Returns:
            actions: The list of action names

        """
        return ListConverter().convert(self.actions, gateway._gateway_client)


def get_actions() -> Dict[AnyStr, Action]:
    """ Dynamically import all the Python modules in this module's sub directory. """
    search_folder = "instruments"
    this_file_path = os.path.split(__file__)[0]
    for filename in os.listdir(os.path.join(this_file_path, search_folder)):
        try:
            module_name = filename.split(".")[0]
            __import__("{folder}.{module}".format(folder=search_folder, module=module_name))
        except Exception as e:
            # Print any errors to stderr, Java will catch and throw to the user
            print("Error loading {}: {}".format(filename, e), file=sys.stderr)

    return {os.path.basename(inspect.getfile(cls)).split(".")[0]: cls for cls in Action.__subclasses__()}


if __name__ == '__main__':
    parser = argparse.ArgumentParser()
    parser.add_argument('java_port', type=int, help='the java port to connect on')
    parser.add_argument('python_port', type=int, help='the python port to connect on')

    args = parser.parse_args()

    config_wrapper = ConfigWrapper(get_actions())

    gateway = ClientServer(
        java_parameters=JavaParameters(port=args.java_port),
        python_parameters=PythonParameters(port=args.python_port),
        python_server_entry_point=config_wrapper)
