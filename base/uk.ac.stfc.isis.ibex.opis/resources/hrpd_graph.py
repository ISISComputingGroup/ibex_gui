from org.csstudio.opibuilder.scriptUtil import PVUtil
from java.lang import Thread, Runnable
from org.csstudio.ui.util.thread import UIBundlingThread
from org.csstudio.opibuilder.scriptUtil import ConsoleUtil
from org.eclipse.swt.widgets import Display


class GraphListener(Runnable):

    def __init__(self):

        self.graph_source_to_pv_map = {
            "Sensor 1": {"VAL": 1, "EGU": 2, "PV": "$(P)CS:SB:DSC_CERNOX_1"},
            "Sensor 2": {"VAL": 3, "EGU": 4, "PV": "$(P)CS:SB:DSC_CERNOX_2"},
            "Sensor 3": {"VAL": 5, "EGU": 6, "PV": "$(P)CS:SB:DSC_CERNOX_3"},
            "Sensor 4": {"VAL": 7, "EGU": 8, "PV": "$(P)CS:SB:DSC_CERNOX_4"},
            "Differential 1": {"VAL": 9, "EGU": 10, "PV": "$(P)DSC:DIFFERENTIAL_1"},
            "Differential 2": {"VAL": 11, "EGU": 12, "PV": "$(P)DSC:DIFFERENTIAL_2"},
            "Differential 3": {"VAL": 13, "EGU": 14, "PV": "$(P)DSC:DIFFERENTIAL_3"},
            "Differential 4": {"VAL": 15, "EGU": 16, "PV": "$(P)DSC:DIFFERENTIAL_4"}
        }

        super(GraphListener, self).__init__()

    def get_graph_source(self):
        return pvs[0].getValue().getValue()
    
    def get_graph_source_pv_underlying_pv(self):
        return pvs[self.graph_source_to_pv_map[self.get_graph_source()]["VAL"]].getName()
    
    def get_graph_source_underlying_pv_egu(self):
        return pvs[self.graph_source_to_pv_map[self.get_graph_source()]["EGU"]].getValue().getValue()  
    
    def run(self):
        while True:
            if not display.isActive():
                return
            # Clear graph if selected graph source changes
            trace_current = widget.getPropertyValue("trace_0_name")
            trace_pv = self.get_graph_source_pv_underlying_pv()
            trace_pv_egu = self.get_graph_source_underlying_pv_egu()
            graph_source = self.get_graph_source()
            if graph_source != trace_current:
                class UITask(Runnable):
                    def run(self):
                        widget.clearGraph()
                        # Set pv and name correctly
                        widget.setPropertyValue("trace_0_name", graph_source)
                        widget.setPropertyValue("axis_1_axis_title", trace_pv_egu)
                        widget.setPropertyValue("trace_0_y_pv", trace_pv)
                UIBundlingThread.getInstance().addRunnable(Display.getCurrent(), UITask())
            
            Thread.sleep(200)

thread = Thread(GraphListener())
thread.start()
    