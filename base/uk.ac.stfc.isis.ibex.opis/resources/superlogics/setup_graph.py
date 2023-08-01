from org.csstudio.opibuilder.scriptUtil import PVUtil

type = PVUtil.getString(pvs[0])

if type == "THERMO":
    channels = []
    for i in range(8):
        if PVUtil.getString(pvs[i + 1]) == "YES":
            channels += str(i)

    widget.setPropertyValue("trace_count", len(channels))
    for i in range(len(channels)):
        widget.setPropertyValue("trace_" + str(i) + "_name", "Value " + channels[i])
        widget.setPropertyValue("trace_" + str(i) + "_y_pv", "$(PV_ROOT):CHANNEL:" + channels[i] + ":VALUE")

elif type == "STRAIN":
    widget.setPropertyValue("trace_count", 1)
    widget.setPropertyValue("trace_0_name", "Strain")
    widget.setPropertyValue("trace_0_y_pv", "$(PV_ROOT):VALUE")
