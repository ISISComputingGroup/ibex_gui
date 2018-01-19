from isActiveMacro import is_active_macro


def get_macros():
    return display.getPropertyValue("macros").getMacrosMap()
    

def levels_requested():
    return any([is_active_macro(get_macros().get("LEVEL_NUM"+str(i))) for i in range(1,3)])
    

def reload_widget(widget):
    original_opi = widget.getPropertyValue("opi_file")
    widget.setPropertyValue("opi_file", "null.opi")
    widget.setPropertyValue("opi_file", original_opi)
    
    
def add_macro(widget, name, value):    
    macros = widget.getPropertyValue("macros")
    macros.put(name, str(value))
    widget.setPropertyValue("macros", macros)
    reload_widget(widget)
    
def populate_tabs(count, card_type, type_count, container):
    for i in range(1, 1 + type_count):
        if is_active_macro(get_macros().get(card_type.upper() + "_NUM" + str(i))):
            tab = display.getWidget("Tab"+str(count))
            tab.setPropertyValue("opi_file", "mercuryiTC_single_" + card_type.lower() + ".opi")
            add_macro(tab, "CARD_NUM", i)
            container.setPropertyValue("tab_" + str(count) + "_title", card_type.title() + " card " + str(i))
            count += 1
    return count

def main():
    count = 0
    container = display.getWidget("TabContainer")
    count = populate_tabs(count, "temp", 4, container)
    count = populate_tabs(count, "level", 2, container)
    if count > 0:
    	container.setPropertyValue("tab_count", count)
    else:
    	container.setPropertyValue("visible", False)
    	display.getWidget("Warning").setPropertyValue("visible", True)

main()
