from org.csstudio.opibuilder.scriptUtil import PVUtil

low = PVUtil.getDouble(pvs[0])
high = PVUtil.getDouble(pvs[1])
avg = (low + high) / 2
avg_low = (low + avg) / 2
avg_high = (high + avg) / 2

widget.setPropertyValue("minimum", low)
widget.setPropertyValue("maximum", high)

widget.setPropertyValue("level_lolo", low)
widget.setPropertyValue("level_lo", avg_low)
widget.setPropertyValue("level_hi", avg_high)
widget.setPropertyValue("level_hihi", high)
