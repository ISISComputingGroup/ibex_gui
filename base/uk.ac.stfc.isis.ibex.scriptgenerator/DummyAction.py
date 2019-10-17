from cfgv import Map
from numpy.core.tests.test_scalarinherit import D
from py4j.java_gateway import JavaGateway, CallbackServerParameters, launch_gateway
from py4j.clientserver import ClientServer, JavaParameters, PythonParameters
from py4j.java_collections import SetConverter, MapConverter, ListConverter
import inspect
import six
import argparse


class DoRun(object):
    def run(self, temperature: float, field: float, uamps: float):
        g.cset("temperature", temperature)
        g.cset("field", field)
        g.begin()
        g.waitfor_uamps(uamps)
        g.end()

    def parameters_valid(self, temperature, field, uamps):
        return 0.1 <= temperature <= 300 and -5 <= field < 5


class ActionWrapper(object):
    def getParameters(self):
        arguments = inspect.getfullargspec(DoRun.run).annotations
        for arg, arg_type in arguments.items():
            arguments[arg] = arg_type.__name__
        return MapConverter().convert(arguments, gateway._gateway_client)

    class Java:
        implements = ['uk.ac.stfc.isis.ibex.scriptgenerator.pythoninterface.ActionWrapper']


if __name__ == '__main__':
    parser = argparse.ArgumentParser()
    parser.add_argument('java_port', type=int, help='the java port to connect on')
    parser.add_argument('python_port', type=int, help='the java port to connect on')

    args = parser.parse_args()

    action_wrapper = ActionWrapper()

    gateway = ClientServer(
        java_parameters=JavaParameters(port=args.java_port),
        python_parameters=PythonParameters(port=args.python_port),
        python_server_entry_point=action_wrapper)