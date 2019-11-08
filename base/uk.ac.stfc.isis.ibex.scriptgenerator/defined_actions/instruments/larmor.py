from action_interface import Action


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
        return None

