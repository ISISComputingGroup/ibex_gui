from org.csstudio.opibuilder.scriptUtil import PVUtil
from org.csstudio.ui.util.thread import UIBundlingThread
from org.eclipse.swt.widgets import Display

from java.lang import Thread, Runnable

currentDisplay = Display.getCurrent()

motNumber = widget.getMacroValue("NUMBER")

jawBackground = display.getWidget("JawBackground"+motNumber)
background_x = jawBackground.getPropertyValue("x")
background_y = jawBackground.getPropertyValue("y")

background_width = jawBackground.getPropertyValue("width")
background_height = jawBackground.getPropertyValue("height")

north_blade = display.getWidget("North_Blade"+motNumber)
south_blade = display.getWidget("South_Blade"+motNumber)
east_blade = display.getWidget("East_Blade"+motNumber)
west_blade = display.getWidget("West_Blade"+motNumber)

north_bound = display.getWidget("North_Bound"+motNumber)
south_bound = display.getWidget("South_Bound"+motNumber)
east_bound = display.getWidget("East_Bound"+motNumber)
west_bound = display.getWidget("West_Bound"+motNumber)


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
        
    def calc_bound_values(self):
        """
        Calculate x and y positions for the limit of travel indicators (bounds)
        """
        north_bound_val = (background_height/2.0)*(1 - self.scaling_factor)
        south_bound_val = background_height - ((background_height/2.0)*(1 - self.scaling_factor))                    
        west_bound_val = (background_width/2.0)*(1 - self.scaling_factor)
        east_bound_val = background_width - ((background_width/2.0)*(1 - self.scaling_factor))
        return north_bound_val, south_bound_val, west_bound_val, east_bound_val

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
        
    def calc_scaling_factor(self):
        """
        Calculate a scaling factor that will be used to shorten the GUI blade elements of
        the shorter axis (i.e. the axis with the smaller limit of travel) such that when the
        vertical and horizontal gap are set equal, the gap between blades will appear square
        and not rectangular.
        """
        if self.max_x > self.max_y:
            self.scaling_factor = self.max_y/self.max_x
        elif self.max_y > self.max_x:
            self.scaling_factor = self.max_x/self.max_y
        else:
            self.scaling_factor = 1.0
            
    def run(self):
        while True:
            if not display.isActive():
                return

            self.get_pvs()
            
            self.calc_scaling_factor()
            if self.max_x > self.max_y:
                self.north *= self.scaling_factor
                self.south *= self.scaling_factor
            elif self.max_y > self.max_x:
                self.east *= self.scaling_factor
                self.west *= self.scaling_factor

            north_bound_val, south_bound_val, west_bound_val, east_bound_val = self.calc_bound_values()
                
            north_height, south_height = self.calc_pair_height((self.north, self.south), self.max_y, background_height)
            east_width, west_width = self.calc_pair_height((self.east, self.west), self.max_x, background_width)
            
            south_y = background_y + background_height - south_height
            east_x = background_x + background_width - east_width
            
            #Make GUI changes on the GUI thread only
            max_x = self.max_x
            max_y = self.max_y
            class UITask(Runnable):
                def run(self):
                    #Set limit of travel indicators to be visible only for the shorter axis
                    north_bound.setPropertyValue("visible", max_x > max_y)
                    south_bound.setPropertyValue("visible", max_x > max_y) 
                    east_bound.setPropertyValue("visible", max_y > max_x)
                    west_bound.setPropertyValue("visible", max_y > max_x)                       
                    north_bound.setPropertyValue("y", north_bound_val)
                    south_bound.setPropertyValue("y", south_bound_val)
                    east_bound.setPropertyValue("x", east_bound_val)
                    west_bound.setPropertyValue("x", west_bound_val)
                                    
                    north_blade.setPropertyValue("height", north_height)
                    south_blade.setPropertyValue("height", south_height)
                    south_blade.setPropertyValue("y", south_y)
                    
                    west_blade.setPropertyValue("width", west_width)
                    east_blade.setPropertyValue("width", east_width)
                    east_blade.setPropertyValue("x", east_x)
            UIBundlingThread.getInstance().addRunnable(currentDisplay, UITask())
            
            Thread.sleep(200)
            
            
thread = Thread(UpdateJaws())
thread.start()
