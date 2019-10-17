from py4j.java_gateway import JavaGateway, CallbackServerParameters, launch_gateway
from py4j.clientserver import ClientServer, JavaParameters, PythonParameters

class ActionWrapper(object):
    def getSomeString(self):
        return "Hello"

    class Java:
        implements = ['uk.ac.stfc.isis.ibex.scriptgenerator.pythoninterface.ActionWrapper']


if __name__ == '__main__':
    action_wrapper = ActionWrapper()

    gateway = ClientServer(
        java_parameters=JavaParameters(port=56871),
        python_parameters=PythonParameters(port=56870),
        python_server_entry_point=action_wrapper)

    from py4j.java_gateway import JavaGateway, CallbackServerParameters

    simple_hello = ActionWrapper()
    gateway = JavaGateway(
        callback_server_parameters=CallbackServerParameters(),
        python_server_entry_point=action_wrapper)