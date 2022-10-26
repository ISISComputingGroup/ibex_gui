from org.csstudio.opibuilder.scriptUtil import PVUtil

rf_level_value = PVUtil.getDouble(pvs[0])

display.getWidget("RF_lvl_readout").setValue(str(rf_level_value) + "DBM")