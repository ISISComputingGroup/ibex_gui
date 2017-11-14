from org.csstudio.opibuilder.scriptUtil import PVUtil
from org.csstudio.opibuilder.scriptUtil import ConsoleUtil

def is_unexpanded_macro(value):
    return len(value)>=3 and value[:2]=="$(" and value[-1]==")"
    
def display_available_attrs(o):
    ConsoleUtil.writeInfo("\n".join(dir(o)))
    
def add_motor_macro(motor_index, target):
    motor_widget = display.getWidget("Motor_" + str(motor_index))
    motor_macros = motor_widget.getPropertyValue("macros")
    motor_macros.put("MOTOR", target)
    motor_widget.setPropertyValue("macros", motor_macros)
    motor_widget.refresh()

motor_names = [PVUtil.getString(pv) for pv in pvs[1:]]
motor_macros = widget.getPropertyValue("macros")



for i, name in enumerate(motor_names):
    if not is_unexpanded_macro(name):
        # Use enumerate for the odd case where named motors are not contiguous
        pvs[0].setValue(i+1)
        add_motor_macro(i+1, name)