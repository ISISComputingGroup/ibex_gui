from org.csstudio.opibuilder.scriptUtil import PVUtil
from org.csstudio.opibuilder.scriptUtil import ConsoleUtil

def is_unexpanded_macro(value):
    return len(value)>=3 and value[:2]=="$(" and value[-1]==")"

motor_names = [PVUtil.getString(pv) for pv in pvs[1:]]

for i, name in enumerate(motor_names):
    if not is_unexpanded_macro(name):
        ConsoleUtil.writeInfo(name)
        # Use enumerate for the odd case where named motors are not contiguous
        naxes = i+1
ConsoleUtil.writeInfo(str(naxes))
pvs[0].setValue(naxes)