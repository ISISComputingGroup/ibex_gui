def is_active_macro(value):
    if value is None or len(value) == 0:
        return False  # Empty macro
    elif len(value) >= 3 and value[:2] == "$(" and value[-1] == ")":
        return False  # Unexpanded macro
    else:
        return True


def main():
    count = 0
    graph = display.getWidget("GasFlowGraph")
    macros = display.getPropertyValue("macros").getMacrosMap()
    for i in range(1, 5):
        data_row = display.getWidget("CCD_"+str(i))
        if is_active_macro(macros.get("CCD_"+str(i))):
            graph.setPropertyValue("trace_"+str(count)+"_y_pv", "$(P)$(CCD_"+str(i)+"):READING")
            graph.setPropertyValue("trace_"+str(count)+"_name", "Gas "+str(i))
            
            y_position = 30*count
            visible = True
            
            count += 1
        else:
            y_position = 0
            visible = False
        data_row.setPropertyValue("visible", visible)
        data_row.setPropertyValue("y", y_position)
        
    display.getWidget("Readings").setPropertyValue("height", 30*count+35)
    graph.setPropertyValue("trace_count", count)


main()
