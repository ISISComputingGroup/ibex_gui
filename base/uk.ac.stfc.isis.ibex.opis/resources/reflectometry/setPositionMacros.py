from org.csstudio.opibuilder.scriptUtil import PVUtil
import json
import zlib


def reload_widgets(count):
    """
    Reload all the position widgets. We have 'count' of them
    """
    for index in range(1, count + 1):
        reload_widget(display.getWidget("pos_" + str(index)))


def reload_widget(widget):
    """
    We have to reload the widget to update the macros. Done by unsetting and resetting the
    target OPI.
    """
    widget.setPropertyValue("opi_file", "null.opi")
    # widget.setPropertyValue("opi_file", "component_pos.opi")
    widget.setPropertyValue("opi_file", "component_pos.opi")


def _add_macros(widget_index, name, value):
    """
    Add the macro with name to a motor widget with value. 
    Widgets are identified as "Motor_n" for n in [1,8] in the parent OPI
    """
    motor_widget = display.getWidget("pos_" + str(widget_index))
    motor_macros = motor_widget.getPropertyValue("macros")
    motor_macros.put(name, value)
    motor_widget.setPropertyValue("macros", motor_macros)
    motor_widget.setPropertyValue("visible", True)

value = PVUtil.getStringArray(pvs[0])
value = "".join(chr(int(i)) for i in value[:-1])
value = zlib.decompress(value.decode("hex"))
params = json.loads(value)

pos = 1
for pv, name in params.iteritems():
    _add_macros(pos, "PARAM_PV", pv)
    _add_macros(pos, "PARAM_NAME", name)
    pos += 1
reload_widgets(pos)

