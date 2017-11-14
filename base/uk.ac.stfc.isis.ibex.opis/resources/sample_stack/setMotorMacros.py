from org.csstudio.opibuilder.scriptUtil import PVUtil
from org.csstudio.opibuilder.scriptUtil import ConsoleUtil
from time import sleep

def is_unexpanded_macro(value):
    return len(value)>=3 and value[:2]=="$(" and value[-1]==")"
    
def display_available_attrs(o):
    ConsoleUtil.writeInfo("\n".join(dir(o)))
    
def print_info(o):
    ConsoleUtil.writeInfo(str(o))
    
def get_pv_value(pv_name, widget, add_prefix=True, max_wait=1.0):
    if add_prefix:
        pv_name = display.getMacroValue("P") + pv_name
    pv = PVUtil.createPV(pv_name, widget)
    steps = 10000
    for i in range(steps):
        try:
            sleep(max_wait/steps)
            return PVUtil.getString(pv)
        except:
            pass
    return ""
    
def add_motor_macro(motor_index, target):
    motor_widget = display.getWidget("Motor_" + str(motor_index))
    motor_macros = motor_widget.getPropertyValue("macros")
    motor_macros.put("MOTOR", get_pv_value(target + ":MOTOR", motor_widget))
    motor_widget.setPropertyValue("macros", motor_macros)
    motor_widget.refresh()        

motor_names = [PVUtil.getString(pv) for pv in pvs[1:]]
motor_macros = widget.getPropertyValue("macros")

for i, name in enumerate(motor_names):
    if not is_unexpanded_macro(name):
        # Use enumerate for the odd case where named motors are not contiguous
        pvs[0].setValue(i+1)
        add_motor_macro(i+1, name)