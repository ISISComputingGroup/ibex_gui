from org.csstudio.opibuilder.scriptUtil import PVUtil, ConsoleUtil

forward_component_pv = pvs[0]
rear_component_pv = pvs[1]
forward_component_z = PVUtil.getDouble(forward_component_pv)
rear_component_z = PVUtil.getDouble(rear_component_pv)
interval = rear_component_z - forward_component_z

widget.setPropertyValue("text", str(round(interval, 1)) + "mm")
widget.setPropertyValue("tooltip", str(forward_component_pv) + " - " + str(rear_component_pv) + "\n" + str(interval))
ConsoleUtil.writeInfo(display.getMacroValue("VACUUM_TANK_INTERVAL_MINOR_WARNING"))
minor_interval_warning = round(float(display.getMacroValue("VACUUM_TANK_INTERVAL_MINOR_WARNING")), 1)
major_interval_warning = round(float(display.getMacroValue("VACUUM_TANK_INTERVAL_MAJOR_WARNING")), 1)

if interval <= minor_interval_warning:
    widget.setPropertyValue("background_color", "Major")
elif interval <= major_interval_warning:
    widget.setPropertyValue("background_color", "Minor")
else:
    widget.setPropertyValue("background_color", "White")
