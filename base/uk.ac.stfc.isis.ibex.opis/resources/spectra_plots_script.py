from org.csstudio.opibuilder.scriptUtil import PVUtil

pvInt0 = PVUtil.getLong(pvs[0])
pvInt1 = PVUtil.getLong(pvs[1])

spectrumPv = pvs[0]
periodPv = pvs[1]
modePv = pvs[2]

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
    else:
        mode = "Y"
        
    widget.setPropertyValue("trace_0_x_pv","$(P)DAE:SPEC:" + str(period) + ":" + str(spectrum) + ":X")
    widget.setPropertyValue("trace_0_y_pv","$(P)DAE:SPEC:" + str(period) + ":" + str(spectrum) + ":" + mode)

main()
