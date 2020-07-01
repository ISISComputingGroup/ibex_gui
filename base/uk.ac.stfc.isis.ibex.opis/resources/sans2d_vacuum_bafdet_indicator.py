from org.csstudio.opibuilder.scriptUtil import PVUtil

bafdet_location_z_pv = pvs[0]
bafdet_location_z = PVUtil.getDouble(pvs[0])
min_x = 150.0
max_x = 800.0 + 150.0
diff_x = max_x - min_x
min_z = 0.0
max_z = 12500.0
diff_z = max_z - min_z
sizing_value = diff_x / diff_z
size_offset = 40.0 / 2.0
widget_new_x = (bafdet_location_z * sizing_value) + min_x - size_offset

widget.setPropertyValue("x", widget_new_x)
widget.setPropertyValue("tooltip", str(bafdet_location_z_pv) + "\n" + str(widget_new_x))
