from org.csstudio.opibuilder.scriptUtil import PVUtil

forward_component_z = PVUtil.getDouble(pvs[0])
rear_component_z = PVUtil.getDouble(pvs[1])

widget.setPropertyValue("text", str(rear_component_z - forward_component_z))
