from org.csstudio.opibuilder.scriptUtil import PVUtil

forward_component_pv = pvs[0]
rear_component_pv = pvs[1]
forward_component_z = PVUtil.getDouble(forward_component_pv)
rear_component_z = PVUtil.getDouble(rear_component_pv)
interval = rear_component_z - forward_component_z

widget.setPropertyValue("text", str(interval))
widget.setPropertyValue("tooltip", str(forward_component_pv) + " - " + str(rear_component_pv) + "\n" + str(interval))
