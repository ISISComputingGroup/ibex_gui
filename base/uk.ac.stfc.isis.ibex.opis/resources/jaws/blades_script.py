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

        self.max_n = PVUtil.getDouble(pvs[4])
        self.max_e = PVUtil.getDouble(pvs[5])
        self.max_s = abs(PVUtil.getDouble(pvs[6]))
        self.max_w = abs(PVUtil.getDouble(pvs[7]))

        
    def calc_bound_values(self):
        """
        Calculate x and y positions for the limit of travel indicators (bounds)
        """
        north_bound_val = (background_height/2.0)*(1 - self.scaling_factor_n)
        south_bound_val = background_height - ((background_height/2.0)*(1 - self.scaling_factor_s))                    
        west_bound_val = (background_width/2.0)*(1 - self.scaling_factor_w)
        east_bound_val = background_width - ((background_width/2.0)*(1 - self.scaling_factor_e))
        return north_bound_val, south_bound_val, west_bound_val, east_bound_val

    def calc_gui_heights(self, gui_max_x, gui_max_y):
        """
        Calculates the height or width of blade gui elements
        :param gui_max_x: the maximum width allowed for the jaws on the GUI
        :param gui_max_y: the maximum height allowed for the jaws on the GUI
        :return: the heights/widths in order north, east, south, west
        """
        nominal_x = gui_max_x / 2
        nominal_y = gui_max_y / 2
        
        north_height = nominal_y - nominal_y * (self.north / self.max_n)
        east_width = nominal_x - nominal_x * (self.east / self.max_e)
        south_height = nominal_y + nominal_y * (self.south / self.max_s)
        west_width = nominal_x + nominal_x * (self.west / self.max_w)
        
        north_height = bound_value(north_height, gui_max_y)
        east_width = bound_value(east_width, gui_max_x)
        south_height = bound_value(south_height, gui_max_y)
        west_width = bound_value(west_width, gui_max_x)

        return north_height, east_width, south_height, west_width
        
    def calc_scaling_factor(self, highest_jaw_limit):
        """
        Calculate scaling factors that will be used to scale the blade GUI elements
        down relative to the jaw with the largest maximum height. This ensures that
        e.g. if a square gap is set in the PVs then the gap appears square instead
        of rectangular.
        """
        self.scaling_factor_n = self.max_n/highest_jaw_limit
        self.scaling_factor_e = self.max_e/highest_jaw_limit
        self.scaling_factor_s = self.max_s/highest_jaw_limit
        self.scaling_factor_w = self.max_w/highest_jaw_limit
    
    def get_highest_limit(self):
        return max([self.max_n, self.max_e, self.max_w, self.max_s])
        
            
    def run(self):
        while True:
            if not display.isActive():
                return

            self.get_pvs()
            
            highest_jaw_limit = self.get_highest_limit()
            self.calc_scaling_factor(highest_jaw_limit)
            self.north *= self.scaling_factor_n
            self.east *= self.scaling_factor_e
            self.south *= self.scaling_factor_s
            self.west *= self.scaling_factor_w

            north_bound_val, south_bound_val, west_bound_val, east_bound_val = self.calc_bound_values()
                
            north_height, east_width, south_height, west_width = self.calc_gui_heights(background_width, background_height)
            
            south_y = background_y + background_height - south_height
            east_x = background_x + background_width - east_width
            
            #Make GUI changes on the GUI thread only
            max_n = self.max_n
            max_e = self.max_e
            max_s = self.max_s
            max_w = self.max_w                       
            class UITask(Runnable):
                def run(self):
                    #Set limit of travel indicators to be visible only for the shorter axis
                    north_bound.setPropertyValue("visible", max_n/highest_jaw_limit < 1.0)
                    south_bound.setPropertyValue("visible", max_s/highest_jaw_limit < 1.0) 
                    east_bound.setPropertyValue("visible", max_e/highest_jaw_limit < 1.0)
                    west_bound.setPropertyValue("visible", max_w/highest_jaw_limit < 1.0)                       
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
