from org.csstudio.opibuilder.scriptUtil import PVUtil
from org.csstudio.opibuilder.scriptUtil import ConsoleUtil
from time import sleep

remote_pv = widget.getPV();
setpoint_pv = PVUtil.createPV(widget.getPropertyValue("pv_name")+":SP", widget)
current_value = int(PVUtil.getDouble(remote_pv))
for _ in range(10):
    if remote_pv.isConnected() and setpoint_pv.isConnected():
        setpoint_pv.setValue(1 if current_value==0 else 0)
        break
    else:
        sleep(0.1)
else:
    ConsoleUtil.writeError("Error: Unable to connect to rate PVs")
