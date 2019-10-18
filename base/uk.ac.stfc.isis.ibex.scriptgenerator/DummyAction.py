from cfgv import Map
from numpy.core.tests.test_scalarinherit import D
from py4j.java_gateway import JavaGateway, CallbackServerParameters, launch_gateway, get_java_class
from py4j.clientserver import ClientServer, JavaParameters, PythonParameters
from py4j.java_collections import SetConverter, MapConverter, ListConverter
import inspect
import six
import argparse
from abc import ABC, abstractmethod


class Action(ABC):
    @abstractmethod
    def run(self, *args):
        pass

    @abstractmethod
    def parameters_valid(self, *args):
        pass


class DoRun(Action):
    def run(self, temperature: float, field: float, uamps: float):
        g.cset("temperature", temperature)
        g.cset("field", field)
        g.begin()
        g.waitfor_uamps(uamps)
        g.end()

    def parameters_valid(self, temperature: float, field: float, uamps: float):
        if not 0.1 <= temperature <= 300:
            return "Temperature outside range"
        if not -5 <= field < 5:
            return "Field outside range"
        return ""


class ActionWrapper(object):

    def __init__(self, action: Action):
        self.action = action

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
        implements = ['uk.ac.stfc.isis.ibex.scriptgenerator.pythoninterface.ActionWrapper']


if __name__ == '__main__':
    parser = argparse.ArgumentParser()
    parser.add_argument('java_port', type=int, help='the java port to connect on')
    parser.add_argument('python_port', type=int, help='the java port to connect on')

    args = parser.parse_args()

    action_wrapper = ActionWrapper(DoRun())

    gateway = ClientServer(
        java_parameters=JavaParameters(port=args.java_port),
        python_parameters=PythonParameters(port=args.python_port),
        python_server_entry_point=action_wrapper)
