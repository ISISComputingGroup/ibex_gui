def is_active_int_macro(value):
    print(value)
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
    
def populate_tab(count, card_type, container, card_number, title_suffix):
    if is_active_int_macro(card_number):
        tab = display.getWidget("Tab"+str(count))
        tab.setPropertyValue("opi_file", "mercuryiTC_single_" + card_type.lower() + ".opi")
        add_macro(tab, "CARD_NUM", int(card_number))
        container.setPropertyValue("tab_" + str(count) + "_title", card_type.title() + title_suffix)
        return count + 1
    else:
        return count

def populate_card_tabs(count, card_type, type_count, container):
    for i in range(1, 1 + type_count):
        card_number = get_macros().get(card_type.upper() + "_NUM" + str(i))
        title_suffix = " card " + str(card_number)
        count = populate_tab(count, card_type, container, card_number, title_suffix)
    return count

def populate_system_tab(count, container):
    return populate_tab(count, "system", container, "1", "")

def populate_auto_pressure_control_tab(count, container):
    return populate_tab(count, "auto_pressure_control", container, "1", "")


def main():
    count = 0
    container = display.getWidget("TabContainer")
    count = populate_card_tabs(count, "temp", 4, container)
    count = populate_card_tabs(count, "level", 2, container)
    count = populate_card_tabs(count, "pressure", 2, container)
    count = populate_system_tab(count, container)
    count = populate_auto_pressure_control_tab(count, container)
    if count > 0:
    	container.setPropertyValue("tab_count", count)
    else:
    	container.setPropertyValue("visible", False)
    	display.getWidget("Warning").setPropertyValue("visible", True)

main()
