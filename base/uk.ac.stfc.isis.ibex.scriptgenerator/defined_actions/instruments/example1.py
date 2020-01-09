from action_interface import ActionDefinition
from random import random


class DoRun(ActionDefinition):
    def run(self, the: str="1", imat: str="2", fields: str="2", there: str="2", are: str="2", more: str="2"):
        pass

    def parameters_valid(self, the: str="1", imat: str="2", fields: str="2", there: str="2", are: str="1", more: str="2"):
        if are != "1":
            return "are is not 1"
        else:
            return None

