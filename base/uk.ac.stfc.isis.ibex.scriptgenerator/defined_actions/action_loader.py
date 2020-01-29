from py4j.clientserver import ClientServer, JavaParameters, PythonParameters
from py4j.java_collections import ListConverter, JavaList, JavaMap, MapConverter
from py4j.protocol import Py4JError
from genie_python.genie_script_generator import ActionDefinition
from typing import Dict, AnyStr, Union, List, Tuple
from inspect import signature
import inspect
import argparse
import os
import sys
from jinja2 import Environment, FileSystemLoader, Markup, TemplateNotFound
import importlib.machinery
import importlib.util


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

    def getHelp(self) -> str:
        """
        Returns:
            A string to be displayed in the script generator UI to help a user when using a config.
        """
        return self.action.get_help()

    def doAction(self, action) -> Union[None, AnyStr]:
        """
        Executes the action with the parameters provided

        Args:
            The 

        Returns:      
            None if run runs without exception, otherwise a String containing the error message.
        """
        return self.action.run(**action)

    def parametersValid(self, action) -> Union[None, AnyStr]:
        """
        Checks if the parameters are valid for the configuration

        Args:
            The parameters to check for validity.

        Returns:
            None if all parameters are valid, otherwise a String containing an error message.
        """
        try:
            return self.action.parameters_valid(**action)
        except Exception as e:
            return str(e) # If there is an error validating return to the user

    def equals(self, other_config) -> bool:
        """ Implement equals needed for py4j

        Returns:
            bool, True if other_config is equal to this instance, otherwise False.
        """
        return other_config.getName() == self.name

    def hashCode(self) -> int:
        """ Calculates the hash of the config"""
        return hash(self.name)

    def toString(self) -> str:
        """Return the name of the config"""
        return self.getName()

class Generator(object):

    def __init__(self, search_folders: List[str] = ["instruments"]):
        """
        Set up the template to generate with
        """
        cwd = os.path.dirname(os.path.abspath(__file__))
        search_folders.append('{}/templates'.format(cwd)) # Add a search to find the template for jinja2
        self.loader = FileSystemLoader(search_folders)
        self.env = Environment(loader=self.loader, keep_trailing_newline=True)
        self.template = self.env.get_template('generator_template.py')

    def areParamsValid(self, list_of_actions, config: Config) -> bool:
        """
        Checks if a list of parameters are valid for the configuration

        Returns:
            True if valid, False if not
        """
        for action in list_of_actions:
            if config.parametersValid(action) != None:
                return False
        return True

    def getValidityErrors(self, list_of_actions, config: Config) -> Dict[int, AnyStr]:
        """
        Get a map of validity errors

        Returns:
            Dictionary containing keys of the line numbers where errors are and values of the error messages.
        """
        current_action_index = 0
        validityCheck: Dict[int, AnyStr] = {}
        for action in list_of_actions:
            singleActionValidityCheck = config.parametersValid(action)
            if singleActionValidityCheck != None:
                validityCheck[current_action_index] = singleActionValidityCheck
            current_action_index += 1
        return validityCheck

    def generate(self, list_of_actions, config: Config) -> Union[None, AnyStr]:
        """
        Generates a script from a list of parameters and configuration

        Returns:
           None if there is an error or parameters are invalid, otherwise a string of a generated script.
        """
        if self.areParamsValid(list_of_actions, config):
            config_file_path = "{}.py".format(config.getName())
            config_template = self.env.get_template(config_file_path)
            try:
                rendered_template = self.template.render(inserted_config=config_template,
                    script_generator_actions=list_of_actions)
            except Exception:
                rendered_template = None
            return rendered_template
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

    def __init__(self, available_actions: Dict[AnyStr, ActionDefinition], generator: Generator, config_load_errors: Dict[AnyStr, AnyStr] = {}):
        self.actions = [Config(instrument, action()) for instrument, action in available_actions.items()]
        self.generator = generator
        self.config_load_errors = config_load_errors

    def getConfigLoadErrors(self) -> Dict[AnyStr, AnyStr]:
        """
        Returns a dictionary mapping of a config that has failed to load mapped to it's error when loading

        Returns:
           config_load_errors: The Dictionary mapping configs to load errors.
        """
        return MapConverter().convert(self.config_load_errors, gateway._gateway_client)

    def getActionDefinitions(self) -> list:
        """
        Returns a list of action names available locally

        Returns:
            actions: The list of action names

        """
        return ListConverter().convert(self.actions, gateway._gateway_client)

    def convert_list_of_actions_to_python(self, list_of_actions) -> List[Dict[AnyStr, AnyStr]]:
        python_list_of_actions: List = ListConverter().convert(list_of_actions, gateway._gateway_client)
        return [MapConverter().convert(action, gateway._gateway_client) for action in python_list_of_actions]
    
    def areParamsValid(self, list_of_actions, config: Config) -> bool:
        """
        Checks if a list of parameters are valid for the configuration

        Returns:
            True if valid, False if not.
        """
        return self.generator.areParamsValid(self.convert_list_of_actions_to_python(list_of_actions), config)

    def getValidityErrors(self, list_of_actions, config: Config) -> Dict[int, AnyStr]:
        """
        Get the validity errors of the current actions

        Returns:
            Dictionary containing keys of the line numbers where errors are and values of the error messages.
        """
        return MapConverter().convert(self.generator.getValidityErrors(
                self.convert_list_of_actions_to_python(list_of_actions), config),
            gateway._gateway_client)

    def generate(self, list_of_actions, config: Config) -> Union[None, AnyStr]:
        """
        Generates a script from a list of parameters and configuration

        Returns:
           None if parameters are invalid, otherwise a string of a generated script.
        """
        return self.generator.generate(self.convert_list_of_actions_to_python(list_of_actions), config)

def get_actions(search_folders: List[str] = None) -> Tuple[Dict[AnyStr, ActionDefinition], Dict[AnyStr, AnyStr]]:
    """ Dynamically import all the Python modules in the search folders"""
    if search_folders is None:
        this_file_path = os.path.split(__file__)[0]
        search_folder = [os.path.join(this_file_path, "instruments")]
    configs: Dict[AnyStr, ActionDefinition] = {}
    config_load_errors: Dict[AnyStr, AnyStr] = {}
    with open("C:/Instrument/Dev/PythonLog.txt", "a+") as f:
        f.write("Starting search folders"+"\n")
    for search_folder in search_folders:
        with open("C:/Instrument/Dev/PythonLog.txt", "a+") as f:
            f.write(search_folder+"\n")
        try:
            for filename in os.listdir(search_folder):
                with open("C:/Instrument/Dev/PythonLog.txt", "a+") as f:
                    f.write(filename+"\n")
                filenameparts = filename.split(".")
                module_name = filenameparts[0]
                if len(filenameparts) > 1:
                    file_extension = filenameparts[-1]
                else:
                    file_extension = ""
                if file_extension == "py":
                    try:
                        loader = importlib.machinery.SourceFileLoader(module_name, os.path.join(search_folder, filename))
                        spec = importlib.util.spec_from_loader(module_name, loader)
                        sys.modules[module_name] = importlib.util.module_from_spec(spec)
                        loader.exec_module(sys.modules[module_name])
                        configs[module_name] = sys.modules[module_name].DoRun
                    except Exception as e:
                        config_load_errors[module_name] = str(e)
                        # Print any errors to stderr, Java will catch and throw to the user
                        print("Error loading {}: {}".format(module_name, e), file=sys.stderr)
        except FileNotFoundError as e:
            config_load_errors[search_folder] = str(e)
            print("Error loading from {}".format(search_folder), file=sys.stderr)
    with open("C:/Instrument/Dev/PythonLog.txt", "a+") as f:
        f.write(str(configs)+"\n"+str(config_load_errors)+"\n")
    return configs, config_load_errors


if __name__ == '__main__':
    parser = argparse.ArgumentParser()
    parser.add_argument('java_port', type=int, help='the java port to connect on')
    parser.add_argument('python_port', type=int, help='the python port to connect on')
    parser.add_argument('search_folders', type=str, help='the folders containing the script generator configs to search')

    args = parser.parse_args()
    search_folders = args.search_folders.split(",")

    configs: Dict[AnyStr, ActionDefinition] = {}
    config_load_errors: Dict[AnyStr, AnyStr] = {}
    configs, config_load_errors = get_actions(search_folders=search_folders)

    config_wrapper = ConfigWrapper(configs, Generator(search_folders=search_folders), config_load_errors=config_load_errors)

    print("Python Ready", file=sys.stderr)

    gateway = ClientServer(
        java_parameters=JavaParameters(port=args.java_port),
        python_parameters=PythonParameters(port=args.python_port),
        python_server_entry_point=config_wrapper)
    
