from org.csstudio.opibuilder.scriptUtil import PVUtil
import os

def _set_panels(pv):
    """
    Set the panels to be the same as the opi constant
    """

    value = PVUtil.getStringArray(pv)
    value = "".join(chr(int(i)) for i in value)
    value = value.strip("\0")

    for widget_name, opi_name in [("Vertical Gaps", "vgaps.opi"),
                                  ("Horizontal Gaps", "hgaps.opi"),
                                  ("Sample Stack", "sample_stack.opi"),
                                  ("Important Params", "important_params.opi")]:
                                      

        widget = display.getWidget(widget_name)
        widget.setPropertyValue("opi_file", "null.opi")
        widget.setPropertyValue("opi_file", value + os.sep + opi_name)
        
        
_set_panels(pvs[0])
