from org.csstudio.opibuilder.scriptUtil import PVUtil

carrier_freq_val = PVUtil.getDouble(pvs[0])
carrier_freq_unit = PVUtil.getString(pvs[1])
    
display.getWidget('Car_freq_readout').setValue(str(carrier_freq_val) + ' ' + carrier_freq_unit)