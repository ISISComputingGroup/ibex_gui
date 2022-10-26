from org.csstudio.opibuilder.scriptUtil import PVUtil

carrier_freq_val = PVUtil.getDouble(pvs[2])
carrier_freq_unit = PVUtil.getString(pvs[3])
pvs[4].setValue(carrier_freq_unit)
pvs[1].setValue(str(carrier_freq_val) + carrier_freq_unit)
