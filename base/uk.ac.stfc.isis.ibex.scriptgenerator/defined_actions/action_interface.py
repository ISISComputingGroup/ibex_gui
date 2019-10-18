from abc import abstractmethod
from yapsy.IPlugin import IPlugin


class Action(IPlugin):
    @abstractmethod
    def run(self, *args):
        pass

    @abstractmethod
    def parameters_valid(self, *args):
        pass
