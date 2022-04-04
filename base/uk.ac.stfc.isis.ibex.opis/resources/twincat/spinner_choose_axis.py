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


widget = display.getWidget("link1")
spinner = display.getWidget("spinner")
value = spinner.getPropertyValue("text")
add_macro(widget, "AXIS", value)
