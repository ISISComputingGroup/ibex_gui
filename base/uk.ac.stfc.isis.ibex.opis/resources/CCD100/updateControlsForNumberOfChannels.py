from org.csstudio.opibuilder.scriptUtil import PVUtil
from org.csstudio.opibuilder.scriptUtil import ConsoleUtil
from time import sleep

def is_active_macro(value):
    if value is None or len(value)==0:
        return False  # Empty macro
    elif len(value)>=3 and value[:2]=="$(" and value[-1]==")":
        return False  # Unexpanded macro
    else:
        return True
    
def get_count():
    count = 0
    macros = display.getPropertyValue("macros").getMacrosMap()
    for i in range(1, 5):
        if is_active_macro(macros.get("CCD_"+str(i))):
            count = i
    return count
    
def set_number_of_graph_traces(count):
    graph = display.getWidget("GasFlowGraph")
    graph.setPropertyValue("trace_count", count)
    for index in range(count):
        graph.setPropertyValue("trace_"+str(index)+"_y_pv", "$(P)$(CCD_"+str(index+1)+"):READING")
        graph.setPropertyValue("trace_"+str(index)+"_name", "Gas "+str(index+1))

def main():
    count = get_count()
    pvs[0].setValue(count)
    set_number_of_graph_traces(count)
            
main()
