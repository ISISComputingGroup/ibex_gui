from org.csstudio.opibuilder.scriptUtil import PVUtil
from org.csstudio.opibuilder.scriptUtil import ConsoleUtil
from time import sleep

def is_unexpanded_macro(value):
    return len(value)>=3 and value[:2]=="$(" and value[-1]==")"
    
def get_pv_value(pv_name, widget, add_prefix=True, max_wait=1.0):
    if add_prefix:
        pv_name = display.getMacroValue("P") + pv_name
    pv = PVUtil.createPV(pv_name, widget)
    steps = 100
    for i in range(steps):
        try:
            sleep(max_wait/steps)
            return PVUtil.getString(pv)
        except:
            pass
    return ""
    
def reload_widget(widget):
    # Reloading is achieved by unsetting and resetting the opi
    widget.setPropertyValue("opi_file","null.opi");
    widget.setPropertyValue("opi_file","motor_control.opi");
    
def add_motor_macro(motor_index, name):
    motor_widget = display.getWidget("Motor_" + str(motor_index))
    
    def get_motor_target():
        return name + ":MOTOR"
    
    motor_macros = motor_widget.getPropertyValue("macros")
    motor_macros.put("MOTOR", get_pv_value(get_motor_target(), motor_widget))
    motor_widget.setPropertyValue("macros", motor_macros)
    
    reload_widget(motor_widget)

def main():
    motor_names = [PVUtil.getString(pv) for pv in pvs[1:]]
    for i, name in enumerate(motor_names):
        if not is_unexpanded_macro(name):
            add_motor_macro(i+1, name)
            naxes = i+1
    pvs[0].setValue(naxes)
            
main()