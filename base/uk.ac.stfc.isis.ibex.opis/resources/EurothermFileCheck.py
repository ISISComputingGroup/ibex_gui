from org.csstudio.opibuilder.scriptUtil import ConsoleUtil
from org.epics.vtype import AlarmSeverity
from org.csstudio.opibuilder.util import OPIColor

def check(file_type, widget_name, pv):
	in_error_message = "Error reading " + file_type + " file. Check the path and format. See log for details"
	no_error_message = "None"

	# Can't get colors working by name. Define as RBG
	in_error_color = (255,0,0)
	no_error_color = (0,0,0)

	in_error = pv.getValue().getAlarmSeverity() is not AlarmSeverity.NONE
	new_rgb = in_error_color if in_error else no_error_color
	message = in_error_message if in_error else no_error_message

	#ConsoleUtil.writeInfo("Error: " + str(in_error) + ". Colour: " + str(new_rgb) + ". Message: " + message)

	widget = display.getWidget(widget_name)
	widget.setPropertyValue('foreground_color',OPIColor(new_rgb[0],new_rgb[1],new_rgb[2]))
	widget.setPropertyValue('text',message)

check(pvs[0].getValue().getValue(),pvs[1].getValue().getValue(),pvs[2])