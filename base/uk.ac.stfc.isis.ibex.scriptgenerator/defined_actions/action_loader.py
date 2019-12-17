from py4j.clientserver import ClientServer, JavaParameters, PythonParameters
from py4j.java_collections import ListConverter, JavaList, JavaMap, MapConverter
from py4j.protocol import Py4JError
from genie_python.genie_script_generator import ActionDefinition
from typing import Dict, AnyStr, Union, List
from inspect import signature
import inspect
import argparse
import os
import sys
from jinja2 import Environment, FileSystemLoader, Markup, TemplateNotFound


class Config(object):
    """
    Class containing the definition and validation functions of a single action
    .
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

    def doAction(self, list_of_arguments) -> None:
        """Executes the action with the parameters provided"""
        self.action.run(**list_of_arguments)

    def parametersValid(self, list_of_arguments) -> Union[None, AnyStr]:
        """
        Checks if the parameters are valid for the configuration

        Returns:
            None if all parameters are valid, otherwise a String containing an error message.
        """
        return self.action.parameters_valid(**list_of_arguments)

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

    def __init__(self, search_folder: str = "instruments"):
        """
        Set up the template to generate with
        """
        cwd = os.path.dirname(os.path.abspath(__file__))
        self.loader = FileSystemLoader(['{}/defined_actions/templates'.format(cwd), '{}defined_actions/{}'.format(cwd, search_folder),
            '{}/templates'.format(cwd), "{}/{}".format(cwd, search_folder)])
        self.env = Environment(loader=self.loader, keep_trailing_newline=True)
        self.env.globals['include_file'] = self.include_file
        self.template = self.env.get_template('generator_template.py')
        self.search_folder = search_folder

    def include_file(self, name):
        return Markup(self.loader.get_source(self.env, name)[0])

    def checkParamValidity(self, list_of_list_of_arguments, config: Config) -> Dict[int, AnyStr]:
        """
        Checks if a list of parameters are valid for the configuration

        Returns:
            Dictionary containing keys of the line numbers where errors are and values of the error messages.
        """
        current_list_of_arguments = 0
        validityCheck: Dict[int, AnyStr] = {}
        for list_of_arguments in list_of_list_of_arguments:
            singleActionValidityCheck = config.parametersValid(list_of_arguments)
            if singleActionValidityCheck != None:
                validityCheck[current_list_of_arguments] = singleActionValidityCheck
            current_list_of_arguments += 1
        return validityCheck

    def areParamsValid(self, list_of_list_of_arguments, config: Config) -> bool:
        for list_of_arguments in list_of_list_of_arguments:
            if config.parametersValid(list_of_arguments) is not None:
                return False
        return True

    def generate(self, list_of_list_of_arguments, config: Config) -> Union[None, AnyStr]:
        """
        Generates a script from a list of parameters and configuration

        Returns:
           None if parameters are invalid, otherwise a string of a generated script.
        """
        if self.areParamsValid(list_of_list_of_arguments, config):
            config_file_path = "{}.py".format(config.getName())
            config_template = self.env.get_template(config_file_path)
            return self.template.render(inserted_config=config_template,
                    script_generator_actions=list_of_list_of_arguments)
        else:
            return None
    
    def get_config_filepath(self, config: Config) -> str:
        """
        Find the absolute file path to the config

        Parameters:
            config: Config
                The config to get the filepath for
        Returns:
            str: The absolute filepath to the config
        """
        return os.path.basename(config.action.get_file())



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
    
    def checkParamValidity(self, list_of_list_of_arguments, config: Config) -> Dict[int, AnyStr]:
        return MapConverter().convert(self.generator.checkParamValidity(list_of_list_of_arguments, config), gateway._gateway_client)

    def generate(self, list_of_list_of_arguments, config: Config) -> Union[None, AnyStr]:
        return self.generator.generate(list_of_list_of_arguments, config)


def get_actions(search_folder: str = "instruments") -> Dict[AnyStr, ActionDefinition]:
    """ Dynamically import all the Python modules in this module's sub directory. """
    this_file_path = os.path.split(__file__)[0]
    for filename in os.listdir(os.path.join(this_file_path, search_folder)):
        try:
            module_name = filename.split(".")[0]
            __import__("{folder}.{module}".format(folder=search_folder, module=module_name))
            # TODO: importlib.import(...)
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
