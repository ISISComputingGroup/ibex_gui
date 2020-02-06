from org.csstudio.opibuilder.scriptUtil import PVUtil

yAxisUnitsPv = pvs[0]
modePv = pvs[1]

def main():

    if yAxisUnitsPv.isConnected():
        # Remove cnt from start of string and add counts to make it more readable 
        y_axis_title = "counts/" + PVUtil.getString(yAxisUnitsPv).split("/")[1]
    else:
        y_axis_title = "counts/us"

    # The initial value
    old_value = y_axis_title if "/" in PVUtil.getString(modePv) else PVUtil.getString(modePv)

    # Set the possible items in the combobox
    widget.setPropertyValue("items", [ y_axis_title, "counts" ])

    # Set the initial value
    PVUtil.writePV(modePv.getName(), old_value)

main()
