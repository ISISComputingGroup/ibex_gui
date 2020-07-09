from org.csstudio.opibuilder.scriptUtil import PVUtil

bafdet_location_z_pv = pvs[0]
bafdet_location_z = PVUtil.getDouble(bafdet_location_z_pv)
min_x = 78.0
max_x = 620.0 + 78.0
diff_x = max_x - min_x
min_z = 0.0
max_z = 12500.0
diff_z = max_z - min_z
sizing_value = diff_x / diff_z
size_offset = 40.0 / 2
widget_new_x = (bafdet_location_z * sizing_value) + min_x - size_offset

widget.setPropertyValue("x", round(widget_new_x, 1))
