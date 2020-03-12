from py4j.clientserver import ClientServer, JavaParameters, PythonParameters
from py4j.java_collections import ListConverter, JavaList, JavaMap, MapConverter
from py4j.protocol import Py4JError
from genie_python.genie_script_generator import ScriptDefinition
from typing import Dict, AnyStr, Union, List, Tuple
from inspect import signature
import inspect
import argparse
import os
import sys
from jinja2 import Environment, FileSystemLoader, Markup, TemplateNotFound
import importlib.machinery
import importlib.util


class PythonActionParameter(object):
    """
    Class containing a parameter name and value.
    """
    class Java:
        implements = ['uk.ac.stfc.isis.ibex.scriptgenerator.pythoninterface.ActionParameter']
    
    def __init__(self, name, default_value):
        """
        Initialise the name and default value of the action parameter.
        """
        self.name = name
        self.default_value = default_value

    def getName(self) -> str:
        """
        Returns the name of this action parameter.

        Returns:
              name: String, the name of this action parameter
        """
        return self.name

    def getDefaultValue(self) -> str:
        """
        Returns the default value of this action parameter.

        Returns:
              name: String, default value of this action parameter.
        """
        return self.default_value


class ScriptDefinitionWrapper(object):
    """
    Class containing the definition and validation functions of a single script_definition.
    """
    class Java:
        implements = ['uk.ac.stfc.isis.ibex.scriptgenerator.pythoninterface.ScriptDefinitionWrapper']

    def __init__(self, name: str, script_definition: ScriptDefinition):
        self.script_definition = script_definition
        self.name = name

    def getName(self) -> str:
        """
        Returns the name of this script_definition

        Returns:
              name: String, the name of this script_definition
        """
        return self.name

    def getParameters(self) -> List[PythonActionParameter]:
        """
        Gets the parameters and default values from the script_definition defined in this script_definition

        Returns:
            arguments: List of the parameter names (strings)
        """
        arguments = signature(self.script_definition.run).parameters

        kwargs_with_defaults = []

        for arg in arguments:
            with open("C:\\Instrument\\Apps\PythonLog.txt", "w") as log_file:
                print(kwargs_with_defaults, file=log_file)
            if arguments[arg].default == arguments[arg].empty:
                action_parameter = PythonActionParameter(arg, arg)
            else:
                action_parameter = PythonActionParameter(arg, str(arguments[arg].default))
            kwargs_with_defaults.append(action_parameter)

        with open("C:\\Instrument\\Apps\PythonLog.txt", "w") as log_file:
            print(kwargs_with_defaults, file=log_file)
            
        return ListConverter().convert(kwargs_with_defaults, gateway._gateway_client)

    def getHelp(self) -> str:
        """
        Returns:
            A string to be displayed in the script generator UI to help a user when using a script_definition.
        """
        return self.script_definition.get_help()

    def doAction(self, action) -> Union[None, AnyStr]:
        """
        Executes the action with the parameters provided

        Args:
            The 

        Returns:      
            None if run runs without exception, otherwise a String containing the error message.
        """
        return self.script_definition.run(**action)

    def parametersValid(self, action) -> Union[None, AnyStr]:
        """
        Checks if the parameters are valid for the script_definition

        Args:
            The parameters to check for validity.

        Returns:
            None if all parameters are valid, otherwise a String containing an error message.
        """
        try:
            return self.script_definition.parameters_valid(**action)
        except Exception as e:
            return str(e) # If there is an error validating return to the user

    def equals(self, other_script_definition) -> bool:
        """ Implement equals needed for py4j

        Returns:
            bool, True if other_script_definition is equal to this instance, otherwise False.
        """
        return other_script_definition.getName() == self.name

    def hashCode(self) -> int:
        """ Calculates the hash of the script_definition"""
        return hash(self.name)

    def toString(self) -> str:
        """Return the name of the script_definition"""
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

    def areParamsValid(self, list_of_actions, script_definition: ScriptDefinitionWrapper) -> bool:
        """
        Checks if a list of parameters are valid for the script_definition

        Returns:
            True if valid, False if not
        """
        for action in list_of_actions:
            if script_definition.parametersValid(action) != None:
                return False
        return True

    def getValidityErrors(self, list_of_actions, script_definition: ScriptDefinitionWrapper) -> Dict[int, AnyStr]:
        """
        Get a map of validity errors

        Returns:
            Dictionary containing keys of the line numbers where errors are and values of the error messages.
        """
        current_action_index = 0
        validityCheck: Dict[int, AnyStr] = {}
        for action in list_of_actions:
            singleActionValidityCheck = script_definition.parametersValid(action)
            if singleActionValidityCheck != None:
                validityCheck[current_action_index] = singleActionValidityCheck
            current_action_index += 1
        return validityCheck

    def generate(self, list_of_actions, script_definition: ScriptDefinitionWrapper) -> Union[None, AnyStr]:
        """
        Generates a script from a list of parameters and script_definition

        Returns:
           None if there is an error or parameters are invalid, otherwise a string of a generated script.
        """
        if self.areParamsValid(list_of_actions, script_definition):
            script_definition_file_path = "{}.py".format(script_definition.getName())
            script_definition_template = self.env.get_template(script_definition_file_path)
            try:
                rendered_template = self.template.render(inserted_script_definition=script_definition_template,
                    script_generator_actions=list_of_actions)
            except Exception:
                rendered_template = None
            return rendered_template
        else:
            return None
    
    def get_script_definition_filepath(self, script_definition: ScriptDefinitionWrapper) -> str:
        """
        Find the absolute file path to the script_definition

        Parameters:
            script_definition: ScriptDefinitionWrapper
                The script_definition to get the filepath for
        Returns:
            str: The absolute filepath to the script_definition
        """
        return os.path.basename(script_definition.script_definition.get_file())



class ScriptDefinitionsWrapper(object):
    """
    Exposes all ScriptDefinitions which have been found supplied to the constructor.
    """
    class Java:
        implements = ['uk.ac.stfc.isis.ibex.scriptgenerator.pythoninterface.ScriptDefinitionsWrapper']

    def __init__(self, available_script_definitions: Dict[AnyStr, ScriptDefinition], generator: Generator, script_definition_load_errors: Dict[AnyStr, AnyStr] = {}):
        self.script_definitions = [ScriptDefinitionWrapper(instrument, script_definition()) for instrument, script_definition in available_script_definitions.items()]
        self.generator = generator
        self.script_definition_load_errors = script_definition_load_errors

    def getScriptDefinitionLoadErrors(self) -> Dict[AnyStr, AnyStr]:
        """
        Returns a dictionary mapping of a script_definition that has failed to load mapped to it's error when loading

        Returns:
           script_definition_load_errors: The Dictionary mapping script_definitions to load errors.
        """
        return MapConverter().convert(self.script_definition_load_errors, gateway._gateway_client)

    def getScriptDefinitions(self) -> list:
        """
        Returns a list of script_definition names available locally

        Returns:
            script_definitions: The list of script_definition names

        """
        return ListConverter().convert(self.script_definitions, gateway._gateway_client)

    def convert_list_of_actions_to_python(self, list_of_actions) -> List[Dict[AnyStr, AnyStr]]:
        python_list_of_actions: List = ListConverter().convert(list_of_actions, gateway._gateway_client)
        return [MapConverter().convert(action, gateway._gateway_client) for action in python_list_of_actions]
    
    def areParamsValid(self, list_of_actions, script_definition: ScriptDefinitionWrapper) -> bool:
        """
        Checks if a list of parameters are valid for the script_definition

        Returns:
            True if valid, False if not.
        """
        return self.generator.areParamsValid(self.convert_list_of_actions_to_python(list_of_actions), script_definition)

    def getValidityErrors(self, list_of_actions, script_definition: ScriptDefinitionWrapper) -> Dict[int, AnyStr]:
        """
        Get the validity errors of the current actions

        Returns:
            Dictionary containing keys of the line numbers where errors are and values of the error messages.
        """
        return MapConverter().convert(self.generator.getValidityErrors(
                self.convert_list_of_actions_to_python(list_of_actions), script_definition),
            gateway._gateway_client)

    def generate(self, list_of_actions, script_definition: ScriptDefinitionWrapper) -> Union[None, AnyStr]:
        """
        Generates a script from a list of parameters and script_definitionuration

        Returns:
           None if parameters are invalid, otherwise a string of a generated script.
        """
        return self.generator.generate(self.convert_list_of_actions_to_python(list_of_actions), script_definition)

    def isPythonReady(self) -> bool:
        """
        Tells Java the Python is ready.
        """
        return True

def get_script_definitions(search_folders: List[str] = None) -> Tuple[Dict[AnyStr, ScriptDefinition], Dict[AnyStr, AnyStr]]:
    """
    Dynamically import all the Python modules in the search folders
    
    Parameters:
        search_folders: List[str]
            The folders to search for actions in

    Returns:
        A tuple. 
        The first element being a dictionary with keys as the module name and actions as the values.
        The second element is also a dictionary with keys as the module name, 
         but with values as the reason they could not be imported.
    """
    if search_folders is None:
        this_file_path = os.path.split(__file__)[0]
        search_folder = [os.path.join(this_file_path, "instruments")]
    script_definitions: Dict[AnyStr, ScriptDefinition] = {}
    script_definition_load_errors: Dict[AnyStr, AnyStr] = {}
    for search_folder in search_folders:
        try:
            for filename in os.listdir(search_folder):
                # Check if we have found a python file or not
                filenameparts = filename.split(".")
                module_name = filenameparts[0]
                if len(filenameparts) > 1:
                    file_extension = filenameparts[-1]
                else:
                    file_extension = ""
                # Found a script definition (python file) import the DoRun class
                if file_extension == "py":
                    try:
                        loader = importlib.machinery.SourceFileLoader(module_name, os.path.join(search_folder, filename))
                        spec = importlib.util.spec_from_loader(module_name, loader)
                        sys.modules[module_name] = importlib.util.module_from_spec(spec)
                        loader.exec_module(sys.modules[module_name])
                        script_definitions[module_name] = sys.modules[module_name].DoRun
                    except Exception as e:
                        # On failure to load ensure we return the reason
                        script_definition_load_errors[module_name] = str(e)
                        # Print any errors to stderr, Java will catch and throw to the user
                        print("Error loading {}: {}".format(module_name, e), file=sys.stderr)
        except FileNotFoundError as e:
            # On failure to load ensure we return the reason
            script_definition_load_errors[search_folder] = str(e)
            print("Error loading from {}".format(search_folder), file=sys.stderr)
    return script_definitions, script_definition_load_errors


if __name__ == '__main__':
    parser = argparse.ArgumentParser()
    parser.add_argument('java_port', type=int, help='the java port to connect on')
    parser.add_argument('python_port', type=int, help='the python port to connect on')
    parser.add_argument('search_folders', type=str, help='the folders containing the script generator script_definitions to search')

    args = parser.parse_args()
    search_folders = args.search_folders.split(",")

    script_definitions: Dict[AnyStr, ScriptDefinition] = {}
    script_definition_load_errors: Dict[AnyStr, AnyStr] = {}
    script_definitions, script_definition_load_errors = get_script_definitions(search_folders=search_folders)

    script_definitions_wrapper = ScriptDefinitionsWrapper(script_definitions, Generator(search_folders=search_folders), script_definition_load_errors=script_definition_load_errors)

    gateway = ClientServer(
        java_parameters=JavaParameters(port=args.java_port),
        python_parameters=PythonParameters(port=args.python_port),
        python_server_entry_point=script_definitions_wrapper)
    
