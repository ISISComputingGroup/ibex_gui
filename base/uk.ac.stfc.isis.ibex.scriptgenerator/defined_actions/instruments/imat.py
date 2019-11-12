from action_interface import ActionDefinition


class DoRun(ActionDefinition):
    def run(self, the: float, imat: float, fields: float, there: float, are: int, more: str):
        pass

    def parameters_valid(self, the: float, imat: float, fields: float, there: float, are: int, more: str):
        return None

