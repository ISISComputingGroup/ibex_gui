from action_interface import ActionDefinition, cast_parameters_to


class DoRun(ActionDefinition):

    @cast_parameters_to(temperature=float, field=float, uamps=str)
    def run(self, temperature: float=0.0, field: float=0.0, uamps: str="0.0"):
        g.cset("temperature", temperature)
        g.cset("field", field)
        g.begin()
        g.waitfor_uamps(uamps)
        g.end()

    @cast_parameters_to(temperature=float, field=float, uamps=str)
    def parameters_valid(self, temperature: float=0.0, field: float=0.0, uamps: str="0.0"):
        errors: str = ""
        if not 0.1 <= float(temperature) <= 300:
            errors += "Temperature outside range\n"
        if not -5 <= int(field) < 5:
            errors += "Field outside range"
        if errors != "":
            return errors
        return None    

