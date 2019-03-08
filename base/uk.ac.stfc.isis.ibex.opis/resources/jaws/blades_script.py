from org.csstudio.opibuilder.scriptUtil import PVUtil
from java.lang import Thread, Runnable

background_x = display.getWidget("JawBackground").getPropertyValue("x")
background_y = display.getWidget("JawBackground").getPropertyValue("y")

background_width = display.getWidget("JawBackground").getPropertyValue("width")
background_height = display.getWidget("JawBackground").getPropertyValue("height")

north_blade = display.getWidget("North_Blade")
south_blade = display.getWidget("South_Blade")
east_blade = display.getWidget("East_Blade")
west_blade = display.getWidget("West_Blade")


def bound_value(value, upper_bound):
    if value < 1:
        return 1
    elif value > upper_bound:
        return upper_bound
    else:
        return value


class UpdateJaws(Runnable):
    def get_pvs(self):
        self.north = PVUtil.getDouble(pvs[0])
        self.south = PVUtil.getDouble(pvs[1])
        self.east = PVUtil.getDouble(pvs[2])
        self.west = PVUtil.getDouble(pvs[3])

        self.max_y = PVUtil.getDouble(pvs[4])
        self.max_x = PVUtil.getDouble(pvs[5])

    def calc_pair_height(self, pv_vals, max_val, gui_max):
        """
        Calculates the height or width of a pair of blades
        :param pv_vals: the pv values in order (N, S) or (E, W)
        :param max_val: the max of the PVs (assumes symmetry)
        :param gui_max: the maximum width/height allowed for the jaws on the GUI
        :return: the heights in order (N, S) or (E, W)
        """
        nominal = gui_max / 2
        first = nominal - nominal * (pv_vals[0] / max_val)
        second = nominal + nominal * (pv_vals[1] / max_val)

        return bound_value(first, gui_max), bound_value(second, gui_max)

    def run(self):
        while True:
            if not display.isActive():
                return

            self.get_pvs()

            north_height, south_height = self.calc_pair_height((self.north, self.south), self.max_y, background_height)
            east_width, west_width = self.calc_pair_height((self.east, self.west), self.max_x, background_width)

            south_y = background_y + background_height - south_height
            east_x = background_x + background_width - east_width

            north_blade.setPropertyValue("height", north_height)
            south_blade.setPropertyValue("height", south_height)
            south_blade.setPropertyValue("y", south_y)

            west_blade.setPropertyValue("width", west_width)
            east_blade.setPropertyValue("width", east_width)
            east_blade.setPropertyValue("x", east_x)

            Thread.sleep(200)


thread = Thread(UpdateJaws())
thread.start()
