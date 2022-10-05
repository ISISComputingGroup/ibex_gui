from py4j.clientserver import ClientServer, JavaParameters, PythonParameters
from py4j.java_collections import ListConverter, JavaList, JavaMap, MapConverter
from py4j.protocol import Py4JError
from genie_python.genie_script_generator import ScriptDefinition, CopyPreviousRow, GlobalParamValidationError
from typing import Dict, AnyStr, Union, List, Tuple
from inspect import signature
import inspect
import argparse
import os
import sys
from git_utils import DefinitionsRepository, DEFAULT_REPO_PATH
from glob import iglob
from jinja2 import Environment, FileSystemLoader
from genie_python import utilities
import importlib.machinery
import importlib.util


class PythonActionParameter(object):
    """
    Class containing a parameter name and value.
    """

    class Java:
        implements = ['uk.ac.stfc.isis.ibex.scriptgenerator.pythoninterface.ActionParameter']

    def __init__(self, name, default_value, copyPreviousRow):
        """
        Initialise the name and default value of the action parameter.
        """
        self.name = name
        self.default_value = default_value
        self.copyPreviousRow = copyPreviousRow

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

    def getCopyPreviousRow(self) -> bool:
        return self.copyPreviousRow


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
            if arguments[arg].default == arguments[arg].empty:
                action_parameter = PythonActionParameter(arg, arg, False)
            elif isinstance(arguments[arg].default, CopyPreviousRow):
                # If none copy the previous row's value over
                action_parameter = PythonActionParameter(arg, str(arguments[arg].default.value), True)
            else:
                action_parameter = PythonActionParameter(arg, str(arguments[arg].default), False)
            kwargs_with_defaults.append(action_parameter)

        return ListConverter().convert(kwargs_with_defaults, gateway._gateway_client)

    def getGlobalParameters(self) -> List[PythonActionParameter]:
        """
        Gets the global parameters and default values from the script_definition defined in this script_definition

        Returns:
            arguments: List of the parameter names (strings)
        """
        list_of_globals = []
        if self.hasGlobalParameters():
            arguments = self.script_definition.global_params_definition
            for arg in arguments:
                name = arg
                default = arguments[arg][0]
                action_parameter = PythonActionParameter(name, default, False)
                list_of_globals.append(action_parameter)

        return ListConverter().convert(list_of_globals, gateway._gateway_client)

    def getCustomOutputs(self) -> List[str]:
        """
        Gets the custom outputs names from the script_definition defined in this script_definition

        Returns:
            arguments: List of the outputs names (strings)
        """
        list_of_names = []
        if self.hasCustomOutputs():
            list_of_names = self.script_definition.custom_outputs.keys()
        return ListConverter().convert(list_of_names, gateway._gateway_client)
    
    def setGlobalParameters(self, global_params):
        """
        Using global_params set the global parameters for the script definition.

        Args:
            global_params: the values to assign to the global parameters.

        Throws:
            ValueError: if at least one of the global parameters cannot be cast to the correct type.
        """
        defs_and_vals = zip(self.script_definition.global_params_definition.items(), global_params)
        self.script_definition.global_params = {name: type_[1](val) for (name, type_), val in defs_and_vals}

    def hasGlobalParameters(self):
        """
        Returns:
            True if this script definition has global parameters, or False if not.
        """
        return hasattr(self.script_definition, "global_params_definition")

    def hasCustomOutputs(self):
        """
        Returns:
            True if this script definition has custom outputs, or False if not.
        """
        return hasattr(self.script_definition, "custom_outputs")

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

    def globalParamsValid(self, global_param, index) -> Union[None, AnyStr]:
        """
        checks if global params are valid for the script definition

        Args:
            The values of the global parameters to check for validity.

        Returns:
            None if all params are valid.
        """
        if not self.hasGlobalParameters():
            return

        try:
            list(self.script_definition.global_params_definition.values())[index][1](global_param)
            return
        except (TypeError, ValueError):
            return 'Expected type: "{}" for global: "{}" but received: "{}"\n'.format(
                str(list(self.script_definition.global_params_definition.values())[index][1])[8:-2],
                list(self.script_definition.global_params_definition.keys())[index], global_param)  
        except GlobalParamValidationError as e:
            return str(e)
        # Fix for edge case where python hasn't changed the script definition yet but java thinks it has.
        except IndexError:
            return 'Tried to find validity for global: "{}" but no such global was found.\n'.format(global_param)

    def parametersValid(self, action, global_params) -> Union[None, AnyStr]:
        """
        Checks if the parameters are valid for the script_definition

        Args:
            The parameters to check for validity.

        Returns:
            None if all parameters are valid, otherwise a String containing an error message.
        """
        try:
            if self.hasGlobalParameters():
                try:
                    self.setGlobalParameters(global_params)
                except ValueError as e:
                    return f"Global parameter value is of the wrong type.\nDetails: {e}"

            return self.script_definition.parameters_valid(**action)
        except Exception as e:
            return str(e)  # If there is an error validating return to the user

    def estimateTime(self, action, global_params) -> Union[None, int]:
        """
        Returns an estimate (in seconds) of the time necessary to complete the action

        Args:
            The action to estimate

        Returns:
            An int representing the estimated time in seconds
            or None if the parameters are invalid or the estimate could not be calculated
        """
        try:
            if self.hasGlobalParameters():
                self.setGlobalParameters(global_params)
            estimate = self.script_definition.estimate_time(**action)
            return round(estimate)
        except (ValueError, TypeError, KeyError) as ex:
            return None

    def estimateCustom(self, action, global_params) -> Union[None, List[int]]:
        """
        Returns a list of custom estimates of an action
        Args:
            The action to estimate

        Returns:
            A list of int representing the custom estimations
            or None if the parameters are invalid or the estimate could not be calculated
        """
        try:
            if self.hasGlobalParameters():
                self.setGlobalParameters(global_params)
            return ListConverter().convert(self.script_definition.estimate_custom(**action), gateway._gateway_client)
        except Exception as ex:
            return None

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

    def __init__(self, repo_path: str):
        """
        Set up the template to generate with
        """
        cwd = os.path.dirname(os.path.abspath(__file__))
        self.loader = FileSystemLoader(["{}/templates".format(cwd), repo_path])
        self.env = Environment(loader=self.loader, keep_trailing_newline=True)
        self.template = self.env.get_template('generator_template.py')

    def areParamsValid(self, list_of_actions, script_definition: ScriptDefinitionWrapper, global_params) -> bool:
        """
        Checks if a list of parameters are valid for the script_definition

        Returns:
            True if valid, False if not
        """
        for action in list_of_actions:
            if script_definition.parametersValid(action, global_params) != None:
                return False
        i = 0
        for global_param in global_params:
            if script_definition.globalParamsValid(global_param, i) != None:
                return False
            i += 1
        return True

    def getValidityErrors(self, global_params, list_of_actions, script_definition: ScriptDefinitionWrapper) -> List[
                        Dict[int, AnyStr]]:
        """
        Get a map of validity errors

        Returns:
            List of dictionaries containing keys of the line numbers where errors are and values of the error messages.
        """
        current_action_index = 0
        param_type_index = 0
        validityCheck: List[Dict[int, AnyStr]] = [{}, {}]
        for global_param in global_params:
            singleParamValidityCheck = script_definition.globalParamsValid(global_param, current_action_index)
            if singleParamValidityCheck != None:
                validityCheck[param_type_index][current_action_index] = singleParamValidityCheck
            current_action_index += 1
        param_type_index = 1
        current_action_index = 0
        for action in list_of_actions:
            singleActionValidityCheck = script_definition.parametersValid(action, global_params)
            if singleActionValidityCheck != None:
                validityCheck[param_type_index][current_action_index] = singleActionValidityCheck
            current_action_index += 1
        return validityCheck

    def estimateTime(self, list_of_actions, script_definition: ScriptDefinitionWrapper, global_params) -> Dict[
        int, int]:
        """
        Estimates the time necessary to complete each action.
        Actions are only estimated if their parameters are valid.

        Returns:
            Dictionary containing line numbers as keys and estimates as values
        """
        time_estimates: Dict[int, int] = {}
        for current_action_index, action in enumerate(list_of_actions, 0):
            if script_definition.parametersValid(action, global_params) is None:
                time_estimate = script_definition.estimateTime(action, global_params)
                if time_estimate != None:
                    time_estimates[current_action_index] = time_estimate
            current_action_index += 1
        return time_estimates

    def estimateCustom(self, list_of_actions, script_definition: ScriptDefinitionWrapper, global_params) -> Dict[
        int, List[int]]:
        """
        Custom Estimates for each action.
        Actions are only estimated if their parameters are valid.

        Returns:
            Dictionary containing line numbers as keys and lists of estimates as values
        """
        custom_estimates: Dict[int, List[int]] = {}
        for current_action_index, action in enumerate(list_of_actions, 0):
            if script_definition.parametersValid(action, global_params) is None:
                custom_estimate = script_definition.estimateCustom(action, global_params)
                if custom_estimate != None:
                    custom_estimates[current_action_index] = custom_estimate
            current_action_index += 1
        return custom_estimates

    def generate(self, list_of_actions, jsonString, global_params,
                 script_definition: ScriptDefinitionWrapper) -> Union[None, AnyStr]:
        """
        Generates a script from a list of parameters and script_definition

        Returns:
           None if there is an error or parameters are invalid, otherwise a string of a generated script.
        """
        if self.areParamsValid(list_of_actions, script_definition, global_params):

            try:
                script_definition_file_path = "{}.py".format(script_definition.getName())
                script_definition_template = self.env.get_template(script_definition_file_path)
                val = str(utilities.compress_and_hex(jsonString))

                # if you need to change the template rendering, the file you need to change is
                # uk.ac.stfc.isis.ibex.scriptgenerator/python_support/templates/generator_template.py

                rendered_template = self.template.render(inserted_script_definition=script_definition_template,
                                                         script_generator_actions=list_of_actions,
                                                         global_params=global_params, hexed_value=val)
            except Exception as e:
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

    def __init__(self, path: str):
        self.repository = DefinitionsRepository(path=path)
        self.script_definitions, self.script_definition_load_errors = get_script_definitions(self.repository.path)

        self.generator = Generator(self.repository.path)

    def remoteAvailable(self) -> bool:
        """
        Returns True if the remote git repository can be reached/pulled from
        """
        try:
            self.repository.fetch_from_origin()
            return True
        except Exception:
            return False

    def updatesAvailable(self) -> bool:
        """
        Returns True if the repository path can be pulled, else False
        """
        return self.repository.updates_available()

    def isDirty(self) -> None:
        """
        Returns True if the repository has uncommitted changes
        """
        return self.repository.is_dirty()

    def getGitErrors(self):
        """
        Return the errors raised during git init for the Java code
        """
        return ListConverter().convert(self.repository.errors, gateway._gateway_client)

    def mergeOrigin(self):
        """
        Attempts to merge from origin
        """
        self.repository.merge_with_origin()

    def getRepoPath(self) -> str:
        """
        Returns the path to the script definitions repository
        """
        return str(self.repository.path)

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

    def areParamsValid(self, list_of_actions, global_params, script_definition: ScriptDefinitionWrapper) -> bool:
        """
        Checks if a list of parameters are valid for the script_definition

        Returns:
            True if valid, False if not.
        """
        return self.generator.areParamsValid(self.convert_list_of_actions_to_python(list_of_actions), script_definition,
                                             global_params)

    def getValidityErrors(self, global_params, list_of_actions,
                          script_definition: ScriptDefinitionWrapper) -> Dict[int, AnyStr]:
        """
        Get the validity errors of the current actions

        Returns:
            List of dictionaries containing keys of the line numbers where errors are and values of the error messages.
        """
        errors_list = self.generator.getValidityErrors(global_params, self.convert_list_of_actions_to_python(
            list_of_actions), script_definition)
        converted_list = [MapConverter().convert(errors, gateway._gateway_client) for errors in errors_list]
        return ListConverter().convert(converted_list, gateway._gateway_client)

    def estimateTime(self, list_of_actions, script_definition: ScriptDefinitionWrapper, global_parameters) -> Dict[
        int, int]:
        """
        Get the estimated time to complete the current actions

        Returns:
            Dictionary containing line numbers as keys and estimates as values
        """
        return MapConverter().convert(self.generator.estimateTime(
            self.convert_list_of_actions_to_python(list_of_actions), script_definition, global_parameters),
            gateway._gateway_client)

    def estimateCustom(self, list_of_actions, script_definition: ScriptDefinitionWrapper, global_parameters) -> Dict[
        int, List[int]]:
        """
        Get the custom estimated of the current actions

        Returns:
            Dictionary containing line numbers as keys and list of estimates as values
        """
        return MapConverter().convert(self.generator.estimateCustom(
            self.convert_list_of_actions_to_python(list_of_actions), script_definition, global_parameters),
            gateway._gateway_client)

    def generate(self, list_of_actions, jsonString, global_params,
                 script_definition: ScriptDefinitionWrapper) -> Union[None, AnyStr]:
        """
        Generates a script from a list of parameters and script_definition

        Returns:
           None if parameters are invalid, otherwise a string of a generated script.
        """
        return self.generator.generate(self.convert_list_of_actions_to_python(list_of_actions), jsonString,
                                       global_params, script_definition)

    def isPythonReady(self) -> bool:
        """
        Tells Java the Python is ready.
        """
        return True


def get_script_definitions(repo_path: str) -> Tuple[List[ScriptDefinitionWrapper], Dict[AnyStr, AnyStr]]:
    """
    Dynamically import all the Python modules in the search folders

    Parameters:
        repo_path: The path to the script definitions repository

    Returns:
        A tuple. 
        The first element being a dictionary with keys as the module name and actions as the values.
        The second element is also a dictionary with keys as the module name, 
         but with values as the reason they could not be imported.
    """
    script_definitions: List[ScriptDefinitionWrapper] = []
    script_definition_load_errors: Dict[AnyStr, AnyStr] = {}

    for filename in iglob("{folder}/*.py".format(folder=repo_path)):
        module_name = os.path.splitext(os.path.basename(filename))[0]
        # Attempt to import the DoRun class
        try:
            loader = importlib.machinery.SourceFileLoader(module_name, os.path.join(repo_path, filename))
            spec = importlib.util.spec_from_loader(module_name, loader)
            loaded_module = importlib.util.module_from_spec(spec)
            loader.exec_module(loaded_module)
            script_definitions.append(ScriptDefinitionWrapper(module_name, loaded_module.DoRun()))
        except Exception as e:
            # On failure to load ensure we return the reason
            script_definition_load_errors[module_name] = str(e)
            # Print any errors to stderr, Java will catch and throw to the user
            print("Error loading {}: {}".format(module_name, e), file=sys.stderr)
    return script_definitions, script_definition_load_errors


if __name__ == '__main__':
    parser = argparse.ArgumentParser()
    parser.add_argument('java_port', type=int, help='the java port to connect on')
    parser.add_argument('python_port', type=int, help='the python port to connect on')
    parser.add_argument('--repo_path', type=str, help='Path to the script generator repository',
                        default=DEFAULT_REPO_PATH)

    args = parser.parse_args()

    script_definitions_wrapper = ScriptDefinitionsWrapper(path=args.repo_path)

    gateway = ClientServer(
        java_parameters=JavaParameters(port=args.java_port),
        python_parameters=PythonParameters(port=args.python_port),
        python_server_entry_point=script_definitions_wrapper)
