from py4j.java_gateway import JavaGateway, CallbackServerParameters, launch_gateway, get_java_class
from py4j.clientserver import ClientServer, JavaParameters, PythonParameters
from py4j.java_collections import MapConverter, ListConverter
import inspect
import argparse
from action_interface import Action
import os


class Config(object):
    def __init__(self, instrument: str, action: Action):
        self.action = action
        self.instrument = instrument

    def getInstrument(self) -> str:
        return self.instrument

    def getParameters(self):
        arguments = inspect.getfullargspec(self.action.run).annotations
        type_conversion = {float: "java.lang.Double", int: "java.lang.Integer", str: "java.lang.String",
                           bool: "java.lang.Boolean"}
        for arg, arg_type in arguments.items():
            try:
                arguments[arg] = gateway.jvm.Class.forName(type_conversion[arg_type])
            except KeyError as e:
                print("Type {} not recognised".format(arg_type))
        return MapConverter().convert(arguments, gateway._gateway_client)

    def doAction(self, list_of_arguments):
        self.action.run(*list_of_arguments)

    def parametersValid(self, list_of_arguments):
        return self.action.parameters_valid(*list_of_arguments)

    class Java:
        implements = ['uk.ac.stfc.isis.ibex.scriptgenerator.pythoninterface.Config']


class ConfigWrapper(object):
    def __init__(self, available_actions: dict):
        self.actions = [Config(instrument, action()) for instrument, action in available_actions.items()]

    def getActions(self) -> dict:
        return ListConverter().convert(self.actions, gateway._gateway_client)

    class Java:
        implements = ['uk.ac.stfc.isis.ibex.scriptgenerator.pythoninterface.ConfigWrapper']


def get_actions() -> dict:
    """ Dynamically import all the Python modules in this module's sub directory. """
    search_folder = "instruments"
    this_file_path = os.path.split(__file__)[0]

    [__import__(search_folder + "." + filename.split(".")[0]) for filename in
     os.listdir(os.path.join(this_file_path, search_folder))]

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
