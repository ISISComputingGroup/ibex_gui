from org.csstudio.opibuilder.scriptUtil import PVUtil
from org.csstudio.opibuilder.scriptUtil import ConsoleUtil
from time import sleep

def is_unexpanded_macro(value):
    return len(value)>=3 and value[:2]=="$(" and value[-1]==")"
    
def get_pv_value(pv, max_wait=1.0):
    steps = 100
    for i in range(steps):
        try:
            return PVUtil.getString(pv)
        except:
            sleep(max_wait/steps)
    return None
    
def reload_widget(widget):
    # Reloading is achieved by unsetting and resetting the opi
    widget.setPropertyValue("opi_file","null.opi");
    widget.setPropertyValue("opi_file","motor_control.opi");
    
def add_motor_macro(motor_index, pv):
    motor_widget = display.getWidget("Motor_" + str(motor_index))    
    
    motor_macros = motor_widget.getPropertyValue("macros")
    motor_macros.put("MOTOR", get_pv_value(pv))
    motor_widget.setPropertyValue("macros", motor_macros)
    
    reload_widget(motor_widget)
    
def get_motor_pvs():
    motor_pvs = []
    for name in [PVUtil.getString(pv) for pv in pvs[1:]]:
        if is_unexpanded_macro(name):
            motor_pvs.append(None)
        else:
            def get_motor_target():
                return display.getMacroValue("P") + name + ":MOTOR"
            motor_pvs.append(PVUtil.createPV(get_motor_target(), display))
    return motor_pvs

def main():
    naxes = 0
    for i, pv in enumerate(get_motor_pvs()):
        if pv is not None:
            add_motor_macro(i+1, pv)
            naxes = i+1
    pvs[0].setValue(naxes)
            
main()