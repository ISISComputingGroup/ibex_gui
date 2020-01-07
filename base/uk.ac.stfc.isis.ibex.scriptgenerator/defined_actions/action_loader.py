from py4j.clientserver import ClientServer, JavaParameters, PythonParameters
from py4j.java_collections import ListConverter, JavaList, JavaMap, MapConverter
from py4j.protocol import Py4JError
from action_interface import ActionDefinition
from typing import Dict, AnyStr, Union, List, Mapping
from inspect import signature
import inspect
import argparse
import os
import sys


class Config(object):
    """
    Class containing the definition and validation functions of a single action.
    """
    class Java:
        implements = ['uk.ac.stfc.isis.ibex.scriptgenerator.pythoninterface.Config']

    def __init__(self, name: str, action: ActionDefinition):
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

    def doAction(self, map_of_arguments: Mapping[AnyStr, AnyStr]) -> Union[None, AnyStr]:
        """
        Executes the action with the parameters provided

        Returns:      
            None if run runs without exception, otherwise a String containing the error message.
        """
        self.action.run(**map_of_arguments)

    def parametersValid(self, map_of_arguments: Mapping[AnyStr, AnyStr]) -> Union[None, AnyStr]:
        """
        Checks if the parameters are valid for the configuration

        Returns:
            None if all parameters are valid, otherwise a String containing an error message.
        """
        return self.action.parameters_valid(**map_of_arguments)

    def equals(self, other_config) -> bool:
        """ Implement equals needed for py4j

        Returns:
            bool, True if other_config is equal to this instance, otherwise False.
        """
        return other_config.getName() == self.name

    def hashCode(self) -> int:
        """ Calculates the hash of the config"""
        return hash(self.name)

class Generator(object):

    class Java:
        implements = ['uk.ac.stfc.isis.ibex.scriptgenerator.generation.PythonGeneratorInterface']

    def areParamsValid(self, list_of_map_of_arguments: List[Mapping[AnyStr, AnyStr]], config: Config) -> Dict[int, AnyStr]:
        """
        Checks if a list of parameters are valid for the configuration

        Returns:
            Dictionary containing keys of the line numbers where errors are and values of the error messages.
        """
        current_list_of_arguments = 0
        validityCheck: Dict[int, AnyStr] = {}
        for list_of_arguments in list_of_map_of_arguments:
            singleActionValidityCheck = config.parametersValid(list_of_arguments)
            if singleActionValidityCheck != None:
                validityCheck[current_list_of_arguments] = singleActionValidityCheck
            current_list_of_arguments += 1
        return validityCheck

    def generate(self, list_of_map_of_arguments: List[Mapping[AnyStr, AnyStr]], config: Config) -> Union[None, AnyStr]:
        """
        Generates a script from a list of parameters and configuration

        Returns:
           None if parameters are invalid, otherwise a string of a generated script.
        """
        if self.areParamsValid(list_of_map_of_arguments, config):
            return "Generated python script"
        else:
            return None


class ConfigWrapper(object):
    """
    Exposes all Configs which have been found supplied to the constructor.
    """
    class Java:
        implements = ['uk.ac.stfc.isis.ibex.scriptgenerator.pythoninterface.ConfigWrapper']

    def __init__(self, available_actions: Dict, generator: Generator):
        self.actions = [Config(instrument, action()) for instrument, action in available_actions.items()]
        self.generator = generator

    def getActionDefinitions(self) -> list:
        """
        Returns a list of action names available locally

        Returns:
            actions: The list of action names

        """
        return ListConverter().convert(self.actions, gateway._gateway_client)

    def getGenerator(self) -> Generator:
        """
        Returns the generator to create python scripts.

        Returns:
           generator: The generator to create python scripts with.
        """
        self.generator
    
    def areParamsValid(self, list_of_map_of_arguments: List[Mapping[AnyStr, AnyStr]], config: Config) -> Dict[int, AnyStr]:
        return MapConverter().convert(self.generator.areParamsValid(list_of_map_of_arguments, config), gateway._gateway_client)

    def generate(self, list_of_map_of_arguments: List[Mapping[AnyStr, AnyStr]], config: Config) -> Union[None, AnyStr]:
        return self.generator.generate(list_of_map_of_arguments, config)


def get_actions() -> Dict[AnyStr, ActionDefinition]:
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

    return {os.path.basename(inspect.getfile(cls)).split(".")[0]: cls for cls in ActionDefinition.__subclasses__()}


if __name__ == '__main__':
    parser = argparse.ArgumentParser()
    parser.add_argument('java_port', type=int, help='the java port to connect on')
    parser.add_argument('python_port', type=int, help='the python port to connect on')

    args = parser.parse_args()

    config_wrapper = ConfigWrapper(get_actions(), Generator())

    gateway = ClientServer(
        java_parameters=JavaParameters(port=args.java_port),
        python_parameters=PythonParameters(port=args.python_port),
        python_server_entry_point=config_wrapper)
