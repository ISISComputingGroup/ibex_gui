from org.csstudio.opibuilder.scriptUtil import PVUtil


yAxisUnitsPv = pvs[0]

def main():

    if yAxisUnitsPv.isConnected():
        modes = [PVUtil.getString(yAxisUnitsPv)]
    else:
        modes = ["counts/us"]

    modes.append("counts")

    widget.setPropertyValue("items", modes)

main()
