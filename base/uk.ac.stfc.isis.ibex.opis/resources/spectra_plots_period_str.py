from org.csstudio.opibuilder.scriptUtil import PVUtil

periodPv = pvs[0]
periodPv_str = pvs[1]

def main():

    if periodPv_str.isConnected() and widget.getPropertyValue("visible"):
        period_str = PVUtil.getString(periodPv_str)
        if period_str != "Current":
            try:
                period = int(float(period_str))
                PVUtil.writePV(periodPv.getName(), period)
            except ValueError as e:
                PVUtil.writePV(periodPv_str.getName(), "Current")

    if periodPv.isConnected():
        period = PVUtil.getLong(periodPv)
        if period == 0:
            widget.setPropertyValue("visible", True)
            PVUtil.writePV(periodPv_str.getName(), "Current")
        else:
            widget.setPropertyValue("visible", False)

main()
