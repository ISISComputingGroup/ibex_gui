from org.csstudio.opibuilder.scriptUtil import PVUtil
from org.csstudio.opibuilder.scriptUtil import ConsoleUtil
from time import sleep

MACRO_ROOT = "AXIS_PV_"

def is_unexpanded_macro(value):
    """
    Is the value of the form "$(...)"?
    """
    return len(value)>=3 and value[:2]=="$(" and value[-1]==")"
    
def get_pv_value(pv, max_wait=1.0):
    """
    Gets the value of a PV as a string. Returns None if no value available
    """
    steps = 100
    for i in range(steps):
        try:
            return PVUtil.getString(pv)
        except:
            sleep(max_wait/steps)
    return None
    
def reload_widgets(count):
    """
    Reload all the motor widgets. We have 'count' of them
    """
    for motor_index in range(1, count+1):
        reload_widget(display.getWidget("Motor_" + str(motor_index)) )
    
def reload_widget(widget):
    """
    We have to reload the widget to update the macros. Done by unsetting and resetting the
    target OPI.
    """
    widget.setPropertyValue("opi_file","null.opi");
    widget.setPropertyValue("opi_file","motor_control.opi");
    
def _add_macro(motor_index, name, value):
    """
    Add the macro with name to a motor widget with value. 
    Widgets are identified as "Motor_n" for n in [1,8] in the parent OPI
    """
    motor_widget = display.getWidget("Motor_" + str(motor_index))    
    motor_macros = motor_widget.getPropertyValue("macros")
    motor_macros.put(name, value)
    motor_widget.setPropertyValue("macros", motor_macros)
    
def _get_axis_pv(index):
	return display.getMacroValue(MACRO_ROOT + str(index))
    
def add_motor_macro(motor_index, pv):
    """
    Add the $(MOTOR) macro to a motor widget. Widgets are identified as "Motor_n"
    for n in [1,8] in the parent OPI
    """
    # Prepend "MOT" if it's not already in the address
    motor_address = get_pv_value(pv)
    if not motor_address.startswith("MOT:"):
        motor_address = "MOT:" + motor_address
        
    _add_motor_macro(motor_index, "MOTOR", motor_address)
    
def add_label_macro(motor_index):
    """
    Add the $(LABEL) macro to a motor widget. Widgets are identified as "Motor_n"
    for n in [1,8] in the parent OPI
    """
    _add_macro(motor_index, "LABEL", _get_axis_pv(motor_index).split(":")[-1])

def add_name_macro(motor_index):
    """
    Add the $(NAME) macro to a motor widget. Widgets are identified as "Motor_n"
    for n in [1,8] in the parent OPI
    """
    _add_macro(motor_index, "NAME", _get_axis_pv(motor_index)
    
def get_motor_pvs():
    """
    Get the PVs containing the names of the motors associated with the specified soft motors
    """
    motor_pvs = []
    
    for name in [PVUtil.getString(pv) for pv in pvs[1:]]:  # pvs[0] is for the number of motors
    
        if is_unexpanded_macro(name):
            motor_pvs.append(None)
            
        else:
            def get_motor_target():
                """
                e.g. "TE:NDW1836:THETA:MOTOR"
                """
                return display.getMacroValue("P") + name + ":MOTOR"
                
            # Associate all PVs with the top-level display for ease. Shouldn't be any conflict
            motor_pvs.append(PVUtil.createPV(get_motor_target(), display))
            
    return motor_pvs

def main():
    naxes = 0
    # enumerate( , 1) not supported in CSS Python
    for i, pv in enumerate(get_motor_pvs()): 
        if pv is not None:
            add_motor_macro(i+1, pv)
            add_label_macro(i+1)
            add_name_macro(i+1)
            naxes = i+1
    pvs[0].setValue(naxes)
    reload_widgets(naxes)
            
main()
