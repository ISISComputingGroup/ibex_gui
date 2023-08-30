from org.csstudio.opibuilder.scriptUtil import PVUtil
from org.csstudio.opibuilder.scriptUtil import ConsoleUtil

traces = [pvs[0:3], pvs[3:6], pvs[6:9], pvs[9:12]]

modePv =  pvs[12]

xAxisUnitsPv = pvs[13]
yAxisUnitsPv = pvs[14]

def main():
    ConsoleUtil.writeError("***************************************")
    
    widget.clearGraph();
    
    counter = 0
    for trace in traces:
    
        spectrumPv = trace[0]
        periodPv = trace[1]
        checkboxPV = trace[2]
        
        # handle null values at initiation of opi
        if spectrumPv.isConnected():
            try:
                spectrum = PVUtil.getLong(spectrumPv)
            except:
                spectrum = 1
                spectrumPv.setValue(spectrum)
           
    
        if periodPv.isConnected():
            try:
                period = PVUtil.getLong(periodPv)
            except:
                period = 1
                periodPv.setValue(period)
            
        try:
            PVUtil.getString(modePv).lower()
        except:
            modePv.setValue("counts")
        
        # set axis title
        
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
        
        if xAxisUnitsPv.isConnected():
            x_axis_title = "Time of flight (" + PVUtil.getString(xAxisUnitsPv) + ")"
        else:
            x_axis_title = "Time of flight (us)"
       
        # set trace to correct spec and period
        widget.setPropertyValue("trace_" + str(counter) + "_x_pv","$(P)DAE:SPEC:" + str(period) + ":" + str(spectrum) + ":X")
        widget.setPropertyValue("trace_" + str(counter) + "_y_pv","$(P)DAE:SPEC:" + str(period) + ":" + str(spectrum) + ":" + mode)
       
        # handle checkbox
        try:
            PVUtil.getLong(checkboxPV)
        except:
        	value = 1
            checkboxPV.setValue(value)
            
                       
        ConsoleUtil.writeError("checkbox_" + str(counter) + ": " +  str(PVUtil.getDouble(checkboxPV)))
        
        # show or hide trace
        if PVUtil.getLong(checkboxPV) == 1.0:
            widget.setPropertyValue("trace_" + str(counter) + "_visible", "true")
        else:
            widget.setPropertyValue("trace_" + str(counter) + "_visible", "false")
        
        # debug
        # ConsoleUtil.writeError("trace_" + str(counter) + "_x_pv" + ", " + "$(P)DAE:SPEC:" + str(period) + ":" + str(spectrum) + ":X")
        # ConsoleUtil.writeError("trace_" + str(counter) + "_y_pv" + ", " + "$(P)DAE:SPEC:" + str(period) + ":" + str(spectrum) + ":" + mode)
        
        
        counter += 1
        
    widget.setPropertyValue("axis_0_axis_title", x_axis_title)
    widget.setPropertyValue("axis_1_axis_title", y_axis_title)
        

main()
