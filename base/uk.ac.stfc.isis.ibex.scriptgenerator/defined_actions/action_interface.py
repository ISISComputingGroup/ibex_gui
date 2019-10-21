from abc import abstractmethod


class Action(object):
    @abstractmethod
    def run(self, *args):
        pass

    @abstractmethod
    def parameters_valid(self, *args):
        pass
