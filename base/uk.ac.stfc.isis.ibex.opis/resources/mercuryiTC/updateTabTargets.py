def is_active_int_macro(value):
    if value is None or len(value) == 0:
        return False  # Empty macro
    elif len(value) >= 3 and value[:2] == "$(" and value[-1] == ")":
        return False  # Unexpanded macro
    else:
    	try:
    		int(value)
    		return True
    	except:
    	    return False


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
    
def populate_tabs(count, card_type, type_count, container):
    for i in range(1, 1 + type_count):
        card_number = get_macros().get(card_type.upper() + "_NUM" + str(i))
        if is_active_int_macro(card_number):
            tab = display.getWidget("Tab"+str(count))
            tab.setPropertyValue("opi_file", "mercuryiTC_single_" + card_type.lower() + ".opi")
            add_macro(tab, "CARD_NUM", int(card_number))
            container.setPropertyValue("tab_" + str(count) + "_title", card_type.title() + " card " + str(card_number))
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
