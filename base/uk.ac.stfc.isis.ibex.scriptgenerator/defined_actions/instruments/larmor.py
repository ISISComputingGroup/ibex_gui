from action_interface import ActionDefinition


class DoRun(ActionDefinition):
    def run(self, temperature: str="0.0", field: str="0.0", uamps: str="0.0"):
        g.cset("temperature", temperature)
        g.cset("field", field)
        g.begin()
        g.waitfor_uamps(uamps)
        g.end()

    def parameters_valid(self, temperature: str="0.0", field: str="0.0", uamps: str="0.0"):
        # Cast the types
        try:
            temperature = float(temperature)
        except ValueError:
            return "Temperature not passed as float"
        try:
            field = float(field)
        except ValueError:
            return "Field not passed as float"
        # Check the parameters are valid
        if not 0.1 <= float(temperature) <= 300:
            return "Temperature outside range"
        if not -5 <= int(field) < 5:
            return "Field outside range"
        return None

