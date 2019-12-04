from action_interface import ActionDefinition


class DoRun(ActionDefinition):
    def run(self, temperature: str="0.0", field: str="0.0", uamps: str="0.0"):
        g.cset("temperature", temperature)
        g.cset("field", field)
        g.begin()
        g.waitfor_uamps(uamps)
        g.end()

    def parameters_valid(self, temperature: str="0.0", field: str="0.0", uamps: str="0.0"):
        if not 0.1 <= temperature <= 300:
            return "Temperature outside range"
        if not -5 <= field < 5:
            return "Field outside range"
        return None

