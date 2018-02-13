from org.csstudio.opibuilder.scriptUtil import PVUtil
from org.csstudio.opibuilder.scriptUtil import ConsoleUtil

def main():
    # Figure out channel number
    channel = int(widget.getPropertyValue("name").replace("Channel ", ""))
    
    # Set up PVs
    gas_type_pv = pvs[0]
    helium_current_pv = pvs[1]
    
    # Check connection
    for _ in range(10):
        if gas_type_pv.isConnected() and helium_current_pv.isConnected():
            break
        else:
            sleep(0.1)
    else:
        ConsoleUtil.writeError("Error: Unable to connect to channel " + str(channel) + " PVs")
        return    
    
    # Hide some controls for Nitrogen channels
    is_nitrogen = gas_type_pv.getValue().getValue() == "Nitrogen"
    has_helium_current = helium_current_pv.getValue().getValue() == "On"
        
    hide_widget_names = []
    if is_nitrogen or has_helium_current:
        hide_widget_names += [
            "Scan rate " + str(channel) + " label",
            "Scan rate " + str(channel) + " RBV", 
            "Scan rate " + str(channel) + " button",
        ]
    if is_nitrogen:
       hide_widget_names += [
           "Helium current " + str(channel) + " RBV",
           "Helium current " + str(channel) + " label",
       ]
    
    for widget_name in hide_widget_names:
        display.getWidget(widget_name).setVisible(False)
        
main()