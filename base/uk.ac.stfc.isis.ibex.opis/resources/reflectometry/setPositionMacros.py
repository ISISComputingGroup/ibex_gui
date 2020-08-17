from org.csstudio.opibuilder.scriptUtil import PVUtil
from org.csstudio.opibuilder.scriptUtil import ConsoleUtil
import json
import time
import zlib


def _set_opi(widget, param_type):
    """
    Set the opi
    Args:
        widget: wdidget to reload
    """
    widget.setPropertyValue("opi_file", "null.opi")
    if param_type == "correction":
        widget.setPropertyValue("opi_file", "correction.opi")
    elif param_type == "align":
        widget.setPropertyValue("opi_file", "param_align.opi")        
    elif param_type == "in_out":
        widget.setPropertyValue("opi_file", "param_inout.opi")
    elif param_type == "float_value":
        widget.setPropertyValue("opi_file", "value.opi")
    elif param_type == "enum":
        widget.setPropertyValue("opi_file", "param_enum.opi")        
    else:
        widget.setPropertyValue("opi_file", "param_move.opi")


def _add_macros(widget, name, value):
    """
    Add the macro with name to a widget with value. 
    Args:
        widget: widget to add macro to
        name: macro name
        value: macro value
    """
    macros = widget.getPropertyValue("macros")
    macros.put(name, value)
    widget.setPropertyValue("macros", macros)

def sort_out_list(pv, widget_name_prefix, macro_prefix, has_type, max):
    value = PVUtil.getStringArray(pv)
    value = "".join(chr(int(i)) for i in value)
    value = zlib.decompress(value.decode("hex"))
    params = json.loads(value)

    for widget_index in range(1, max+1):
        target_widget = display.getWidget(widget_name_prefix + str(widget_index))
        
        if widget_index <= len(params):
            _add_macros(target_widget, macro_prefix + "_PV", params[widget_index - 1]["prepended_alias"])
            _add_macros(target_widget, macro_prefix + "_NAME", params[widget_index - 1]["name"])
            if has_type:
                _set_opi(target_widget, params[widget_index - 1]["type"])
            else:
                _set_opi(target_widget, "correction")
            target_widget.setPropertyValue("visible", True)
        else:
            target_widget.setPropertyValue("visible", False)

# Widget names need to be set before trying to set properties
time.sleep(0.2)

sort_out_list(pvs[0], "params_slit_", "PARAM", True, 28)
sort_out_list(pvs[1], "params_collim_", "PARAM", True, 28)
sort_out_list(pvs[2], "params_toggle_", "PARAM", True, 28)
sort_out_list(pvs[3], "params_misc_", "PARAM", True, 28)
sort_out_list(pvs[4], "Correction_", "COR", False, 14)     
sort_out_list(pvs[5], "align_", "PARAM", True, 64)    
sort_out_list(pvs[6], "Value_", "VALUE", True, 32) 
