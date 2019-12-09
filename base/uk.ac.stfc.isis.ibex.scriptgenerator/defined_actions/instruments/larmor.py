from action_interface import ActionDefinition


class DoRun(ActionDefinition):
    def run(self, temperature: str="0.0", field: str="0.0", uamps: str="0.0"):
        g.cset("temperature", temperature)
        g.cset("field", field)
        g.begin()
        g.waitfor_uamps(uamps)
        g.end()

    def parameters_valid(self, temperature: str="0.0", field: str="0.0", uamps: str="0.0"):
        errors: str = ""
        try:
            if not 0.1 <= float(temperature) <= 300:
                errors += "Temperature outside range\n"
        except ValueError:
            errors += "Temperature not passed as float\n"
        try:
            if not -5 <= int(field) < 5:
                errors += "Field outside range"
        except ValueError:
            errors += "Field not passed as float"
        if errors != "":
            return errors
        return None

