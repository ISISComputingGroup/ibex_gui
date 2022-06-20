from org.csstudio.opibuilder.scriptUtil import PVUtil

periodPv = pvs[0]
periodPv_str = pvs[1]

string_for_period_0 = "Current"

def main():
    if periodPv_str.isConnected() and widget.getPropertyValue("visible"):
        period_str = PVUtil.getString(periodPv_str)
        if period_str != string_for_period_0:
            try:
                period = int(float(period_str))
                PVUtil.writePV(periodPv.getName(), period)
            except ValueError as e:
                PVUtil.writePV(periodPv_str.getName(), string_for_period_0)

    if periodPv.isConnected():
        period = PVUtil.getLong(periodPv)
        if period == 0:
            widget.setPropertyValue("visible", True)
            if PVUtil.getString(periodPv_str) != string_for_period_0:
                PVUtil.writePV(periodPv_str.getName(), string_for_period_0)
        else:
            widget.setPropertyValue("visible", False)

main()
