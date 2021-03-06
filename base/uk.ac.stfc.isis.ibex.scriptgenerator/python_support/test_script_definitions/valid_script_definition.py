from genie_python import genie as g

from genie_python.genie_script_generator import ScriptDefinition, cast_parameters_to

class DoRun(ScriptDefinition):

    @cast_parameters_to(param1=float, param2=float)
    def run(self, param1=0.0, param2=0.0):
        pass

    @cast_parameters_to(param1=float, param2=float)
    def parameters_valid(self, param1=0.0, param2=0.0):
        reason = ""
        if param1 != 1.0:
            reason += "param1 is not 1.0\n"
        if param2 != 2.0:
            reason += "param2 is not 2.0\n"
        if reason != "":
            return reason
        else:
            return None

    @cast_parameters_to(param1=float, param2=float)
    def estimate_time(self, param1=0.0, param2=0.0):
        return param1 * param2

    def get_help(self):
        return "Help"

