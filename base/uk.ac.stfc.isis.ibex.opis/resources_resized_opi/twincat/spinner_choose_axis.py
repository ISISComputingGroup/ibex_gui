from org.csstudio.opibuilder.scriptUtil import PVUtil

def get_macros():
    return display.getPropertyValue("macros").getMacrosMap()
        

def reload_widget(widget):
    original_opi = widget.getPropertyValue("opi_file")
    widget.setPropertyValue("opi_file", "null.opi")
    widget.setPropertyValue("opi_file", original_opi)
    
    
def add_macro(widget, name, value):    
    macros = widget.getPropertyValue("macros")
    macros.put(name, str(value))
    widget.setPropertyValue("macros", macros)
    reload_widget(widget)

NUM_AXES_TO_SHOW = 8
spinner = display.getWidget("spinner")
FIRST_AXIS_TO_SHOW = spinner.getPropertyValue("text")

for i in range(1, NUM_AXES_TO_SHOW+1):
	widget = display.getWidget("link" + str(i))
	value = int(FIRST_AXIS_TO_SHOW) + i - 1
	add_macro(widget, "AXIS", value)
