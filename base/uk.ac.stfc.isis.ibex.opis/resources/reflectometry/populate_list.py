from org.csstudio.opibuilder.scriptUtil import PVUtil
import json
import time
import zlib

from org.csstudio.ui.util.thread import UIBundlingThread
from java.lang import Thread, Runnable


from org.eclipse.swt.widgets import Display
currentDisplay = Display.getCurrent()


def _set_opi(widget, param_type, has_cv):
    """
    Set the opi
    Args:
        widget: widget to reload
        param_type: parameter type to load
        has_cv: has a characteristic value to dispaly
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
    elif param_type == "string_value":
    	widget.setPropertyValue("opi_file", "string_value.opi")
    elif param_type == "enum":
        widget.setPropertyValue("opi_file", "param_enum.opi")        
    else:
        if has_cv:
            widget.setPropertyValue("opi_file", "param_move_with_cv.opi")
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

def populate_list(pv, widget_name_prefix, macro_prefix, has_type, max):
    value = PVUtil.getStringArray(pv)
    value = "".join(chr(int(i)) for i in value)
    value = zlib.decompress(value.decode("hex"))
    params = json.loads(value)

    for widget_index in range(1, max+1):
        target_widget = display.getWidget("{}_{}".format(widget_name_prefix, str(widget_index)))
        
        if widget_index <= len(params):
            _add_macros(target_widget, macro_prefix + "_PV", params[widget_index - 1]["prepended_alias"])
            _add_macros(target_widget, macro_prefix + "_NAME", params[widget_index - 1]["name"])
            _add_macros(target_widget, macro_prefix + "_DESC", params[widget_index - 1].get("description", ""))
            cv_value = params[widget_index - 1].get("characteristic_value", "")
            _add_macros(target_widget, "CHARACTERISTIC_PV", cv_value)
            if has_type:
                _set_opi(target_widget, params[widget_index - 1]["type"], cv_value != "")
            else:
                _set_opi(target_widget, "correction", cv_value != "")
            target_widget.setPropertyValue("visible", True)
        else:
            target_widget.setPropertyValue("visible", False)


class WorkerThread(Runnable):
    def run(self):
        # Widget names need to be set before trying to set properties
        Thread.sleep(200)

        class UITask(Runnable):
            def run(self):
                macros = widget.getPropertyValue("macros").getMacrosMap()
                populate_list(pvs[0], macros["GROUP_KEY"], macros["MACRO_PREFIX"], bool(int(macros["HAS_TYPE"])), int(macros["MAX_NUMBER"]))
        UIBundlingThread.getInstance().addRunnable(currentDisplay, UITask())
        
        
thread = Thread(WorkerThread(), "reflectometry OPI populate_list.py worker")
thread.start()
