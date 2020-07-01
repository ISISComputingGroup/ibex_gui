from org.csstudio.opibuilder.scriptUtil import PVUtil

forward_component_pv = pvs[0]
rear_component_pv = pvs[1]
forward_component_z = PVUtil.getDouble(forward_component_pv)
rear_component_z = PVUtil.getDouble(rear_component_pv)
interval = rear_component_z - forward_component_z

widget.setPropertyValue("text", str(interval))
widget.setPropertyValue("tooltip", str(forward_component_pv) + " - " + str(rear_component_pv) + "\n" + str(interval))
if interval <= 50: # 5cm
    widget.setPropertyValue("background_color", "Major")
elif interval <= 100: # 10cm
    widget.setPropertyValue("background_color", "Minor")
else:
    widget.setPropertyValue("background_color", "White")
