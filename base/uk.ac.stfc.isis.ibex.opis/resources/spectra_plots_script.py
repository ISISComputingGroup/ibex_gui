from org.csstudio.opibuilder.scriptUtil import PVUtil

spectrumPv = pvs[0]
periodPv = pvs[1]
modePv = pvs[2]
xAxisUnitsPv = pvs[3]
yAxisUnitsPv = pvs[4]

def main():

    if spectrumPv.isConnected():
        spectrum = PVUtil.getLong(spectrumPv)
        if spectrum is None:
            spectrum = 1
    else:
        spectrum = 1

    if periodPv.isConnected():
        period = PVUtil.getLong(periodPv)
        if period is None:
            period = 1
    else:
        period = 1

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

    widget.clearGraph();
    widget.setPropertyValue("trace_0_x_pv","$(P)DAE:SPEC:" + str(period) + ":" + str(spectrum) + ":X")
    widget.setPropertyValue("trace_0_y_pv","$(P)DAE:SPEC:" + str(period) + ":" + str(spectrum) + ":" + mode)
    widget.setPropertyValue("axis_0_axis_title", x_axis_title)
    widget.setPropertyValue("axis_1_axis_title", y_axis_title)

main()
