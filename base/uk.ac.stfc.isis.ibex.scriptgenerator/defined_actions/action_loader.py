from py4j.clientserver import ClientServer, JavaParameters, PythonParameters
from py4j.java_collections import ListConverter
import inspect
import argparse
from action_interface import Action
import os
import sys


class Config(object):
    class Java:
        implements = ['uk.ac.stfc.isis.ibex.scriptgenerator.pythoninterface.Config']

    def __init__(self, name: str, action: Action):
        self.action = action
        self.name = name

    def getName(self) -> str:
        return self.name

    def getParameters(self):
        arguments = inspect.getfullargspec(self.action.run).annotations
        return ListConverter().convert(arguments.keys(), gateway._gateway_client)

    def doAction(self, list_of_arguments):
        self.action.run(*list_of_arguments)

    def parametersValid(self, list_of_arguments):
        return self.action.parameters_valid(*list_of_arguments)

    def equals(self, other_config):
        return other_config.getName() == self.name
    

class ConfigWrapper(object):
    class Java:
        implements = ['uk.ac.stfc.isis.ibex.scriptgenerator.pythoninterface.ConfigWrapper']

    def __init__(self, available_actions: dict):
        self.actions = [Config(instrument, action()) for instrument, action in available_actions.items()]

    def getActions(self):
        return ListConverter().convert(self.actions, gateway._gateway_client)


def get_actions() -> dict:
    """ Dynamically import all the Python modules in this module's sub directory. """
    search_folder = "instruments"
    this_file_path = os.path.split(__file__)[0]
    for filename in os.listdir(os.path.join(this_file_path, search_folder)):
        try:
            __import__(search_folder + "." + filename.split(".")[0])
        except Exception as e:
            # Print any errors to stderr, Java will catch and throw to the user
            print("Error loading {}: {}".format(filename, e), file=sys.stderr)

    return {os.path.basename(inspect.getfile(cls)).split(".")[0]: cls for cls in Action.__subclasses__()}


if __name__ == '__main__':
    parser = argparse.ArgumentParser()
    parser.add_argument('java_port', type=int, help='the java port to connect on')
    parser.add_argument('python_port', type=int, help='the java port to connect on')

    args = parser.parse_args()

    config_wrapper = ConfigWrapper(get_actions())

    gateway = ClientServer(
        java_parameters=JavaParameters(port=args.java_port),
        python_parameters=PythonParameters(port=args.python_port),
        python_server_entry_point=config_wrapper)
