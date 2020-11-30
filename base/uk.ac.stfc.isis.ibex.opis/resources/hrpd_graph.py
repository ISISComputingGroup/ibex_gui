from org.csstudio.opibuilder.scriptUtil import PVUtil
import sys

graph_source_val = pvs[0].getValue().getValue()
graph_source_map = {
    "Sensor 1": "TE:NDLT1208:CS:SB:DSC_CERNOX_1",
    "Sensor 2": "TE:NDLT1208:CS:SB:DSC_CERNOX_2",
    "Sensor 3": "TE:NDLT1208:CS:SB:DSC_CERNOX_3",
    "Sensor 4": "TE:NDLT1208:CS:SB:DSC_CERNOX_4",
    "Differential 1": "TE:NDLT1208:DSC:DIFFERENTIAL_1",
    "Differential 2": "TE:NDLT1208:DSC:DIFFERENTIAL_2",
    "Differential 3": "TE:NDLT1208:DSC:DIFFERENTIAL_3",
    "Differential 4": "TE:NDLT1208:DSC:DIFFERENTIAL_4"
}
trace_current = widget.getPropertyValue("trace_0_name")
if graph_source_val != trace_current:
    widget.clearGraph()
widget.setPropertyValue("trace_0_y_pv", graph_source_map[graph_source_val])
widget.setPropertyValue("trace_0_name", graph_source_val)
