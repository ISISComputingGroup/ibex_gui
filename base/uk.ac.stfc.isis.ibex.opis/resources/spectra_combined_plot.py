from org.csstudio.opibuilder.scriptUtil import PVUtil
from org.csstudio.opibuilder.scriptUtil import ConsoleUtil

traces = [pvs[0:3], pvs[3:6], pvs[6:9], pvs[9:12]]

modePv =  pvs[12]

xAxisUnitsPv = pvs[13]
yAxisUnitsPv = pvs[14]

def main():
    ConsoleUtil.writeError("*****RELOADED*****")
    
    widget.clearGraph();
    
    # set axis titles
    # axis title logic requires modePv != null
    try:
        PVUtil.getString(modePv).lower()
    except:
        modePv.setValue("counts")
        
    if xAxisUnitsPv.isConnected():
        x_axis_title = "Time of flight (" + PVUtil.getString(xAxisUnitsPv) + ")"
    else:
        x_axis_title = "Time of flight (us)"
    
    if modePv.isConnected() and PVUtil.getString(modePv).lower() == "counts":
        mode = "YC"
        y_axis_title = "counts"
    else:
        mode = "Y"
        if yAxisUnitsPv.isConnected():
            # Remove cnt from start of string and add counts to make it more readable 
            y_axis_title = "counts/" + PVUtil.getString(yAxisUnitsPv).split("/")[1]
        else:
            y_axis_title = "counts/us"
            
            
    widget.setPropertyValue("axis_0_axis_title", x_axis_title)
    widget.setPropertyValue("axis_1_axis_title", y_axis_title)
            
    # setting the 4 traces
    counter = 0
    for trace in traces:
    
        spectrumPv = trace[0]
        periodPv = trace[1]
        checkboxPV = trace[2]
        
        # handle null values 
        if spectrumPv.isConnected():
            try:
                spectrum = PVUtil.getLong(spectrumPv)
            except:
                spectrum = counter + 1
                spectrumPv.setValue(spectrum)

        if periodPv.isConnected():
            try:
                period = PVUtil.getLong(periodPv)
            except:
                period = 1
                periodPv.setValue(period)
            
        try:
            PVUtil.getDouble(checkboxPV)
        except:
            value = 1
            checkboxPV.setValue(value)
        
        # set trace to correct spectrum and period
        # widget.setPropertyValue("trace_" + str(counter) + "_x_pv","$(P)DAE:SPEC:" + "1" + ":" + "0" + ":X")
        # widget.setPropertyValue("trace_" + str(counter) + "_y_pv","$(P)DAE:SPEC:" + "1" + ":" + "0" + ":" + mode)
        
        # widget.setPropertyValue("trace_" + str(counter) + "_update_mode", "Trigger")
        
        
        
        widget.setPropertyValue("trace_" + str(counter) + "_x_pv","$(P)DAE:SPEC:" + str(period) + ":" + str(spectrum) + ":X")
        widget.setPropertyValue("trace_" + str(counter) + "_y_pv","$(P)DAE:SPEC:" + str(period) + ":" + str(spectrum) + ":" + mode)
        
        # widget.setPropertyValue("trace_" + str(counter) + "_update_mode", "X or Y")
        
        # set visibility of trace based on checkbox
        checkbox_val = PVUtil.getDouble(checkboxPV)
        if  checkbox_val == 1.0:
            widget.setPropertyValue("trace_" + str(counter) + "_visible", "true")
        else:
            widget.setPropertyValue("trace_" + str(counter) + "_visible", "false")
            
        # debug
        ConsoleUtil.writeError("Trace " + str(counter) + " | DAE:SPEC:" + str(period) + ":" + str(spectrum) + " | CHECKBOX: " + str(checkbox_val))
        
        counter += 1
        

main()
