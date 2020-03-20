from org.csstudio.opibuilder.scriptUtil import ConsoleUtil
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
            
            #Without the following code, if the horizontal and vertical limits of travel differ
            #but the vertical and horizontal gaps are set equal, the gap on the GUI will not appear
            #square as expected. The following applies a scaling factor to the jaw heights/widths to correct this
            #and calculates x and y positions for the limit of travel indicators.
            if self.max_x > self.max_y:
                scaling_factor = self.max_y/self.max_x
                self.north *= scaling_factor
                self.south *= scaling_factor
                north_bound_val = background_height*scaling_factor/2.0
                south_bound_val = background_height - background_height*scaling_factor/2.0
            elif self.max_y > self.max_x:
                scaling_factor = self.max_x/self.max_y
                self.east *= scaling_factor
                self.west *= scaling_factor
                west_bound_val = background_width*scaling_factor/2.0
                east_bound_val = background_width - background_width*scaling_factor/2.0
            else:
                scaling_factor = 1.0             
                        
            north_height, south_height = self.calc_pair_height((self.north, self.south), self.max_y, background_height)
            east_width, west_width = self.calc_pair_height((self.east, self.west), self.max_x, background_width)
            
            south_y = background_y + background_height - south_height
            east_x = background_x + background_width - east_width
            
            #Make GUI changes on the GUI thread only
            max_x = self.max_x
            max_y = self.max_y
            class UITask(Runnable):
                def run(self):
                    #Set limit of travel indicators
                    if max_x > max_y:
                        north_bound.setPropertyValue("visible", True)
                        south_bound.setPropertyValue("visible", True) 
                        east_bound.setPropertyValue("visible", False)
                        west_bound.setPropertyValue("visible", False)                       
                        north_bound.setPropertyValue("y", north_bound_val)
                        south_bound.setPropertyValue("y", south_bound_val)
                    elif max_y > max_x:
                        east_bound.setPropertyValue("visible", True)
                        west_bound.setPropertyValue("visible", True)
                        north_bound.setPropertyValue("visible", False)
                        south_bound.setPropertyValue("visible", False)
                        east_bound.setPropertyValue("x", east_bound_val)
                        west_bound.setPropertyValue("x", west_bound_val)
                    else:
                        north_bound.setPropertyValue("visible", False)
                        south_bound.setPropertyValue("visible", False)
                        east_bound.setPropertyValue("visible", False)
                        west_bound.setPropertyValue("visible", False)   
                
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
