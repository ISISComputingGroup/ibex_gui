from org.csstudio.opibuilder.scriptUtil import PVUtil
from org.csstudio.opibuilder.util import BOYPVFactory
import sys

pv_prefix = display.getMacroValue("P")
graph_source_val = pvs[0].getValue().getValue()
graph_source_map = {
    "Sensor 1": "{}CS:SB:DSC_CERNOX_1".format(pv_prefix),
    "Sensor 2": "{}CS:SB:DSC_CERNOX_2".format(pv_prefix),
    "Sensor 3": "{}CS:SB:DSC_CERNOX_3".format(pv_prefix),
    "Sensor 4": "{}CS:SB:DSC_CERNOX_4".format(pv_prefix),
    "Differential 1": "{}DSC:DIFFERENTIAL_1".format(pv_prefix),
    "Differential 2": "{}DSC:DIFFERENTIAL_2".format(pv_prefix),
    "Differential 3": "{}DSC:DIFFERENTIAL_3".format(pv_prefix),
    "Differential 4": "{}DSC:DIFFERENTIAL_4".format(pv_prefix)
}

# Clear graph if selected graph source changes
trace_current = widget.getPropertyValue("trace_0_name")
trace_pv = graph_source_map[graph_source_val]
y_axis_pv = BOYPVFactory.createPV(trace_pv + ".EGU")
y_axis_title = PVUtil.getDouble(y_axis_pv)
if graph_source_val != trace_current:
    widget.clearGraph()
    # Set pv and name correctly
    widget.setPropertyValue("trace_0_y_pv", graph_source_map[graph_source_val])
    widget.setPropertyValue("trace_0_name", graph_source_val)
    widget.setPropertyValue("axis_1_axis_title", y_axis_title)
