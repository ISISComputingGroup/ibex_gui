from genie_python.genie_script_generator import ActionDefinition, cast_parameters_to

def mytype(string_input):
    if string_input == "default":
        return 0.0
    else:
        return float(string_input)


class DoRun(ActionDefinition):

    @cast_parameters_to(temperature=float, field=float, uamps=mytype)
    def run(self, temperature=0.0, field=0.0, uamps=0.0):
        g.cset("temperature", temperature)
        g.cset("field", field)
        g.begin()
        g.waitfor_uamps(uamps)
        g.end()

    @cast_parameters_to(temperature=float, field=float, uamps=mytype)
    def parameters_valid(self, temperature=0.0, field=0.0, uamps=0.0):
        errors = ""
        if not 0.1 <= temperature <= 300:
            errors += "Temperature outside range\n"
        if not -5 <= field < 5:
            errors += "Field outside range"
        if not -20 <= uamps <=32:
            errors += "uamps outside of range"
        if errors != "":
            return errors
        return None    

