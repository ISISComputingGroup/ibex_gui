from org.csstudio.opibuilder.scriptUtil import PVUtil


def copy_pvs(this_display, this_pvs):
    """
    Copies PVs

    Args:
        this_display: The display that has called this script. Used to control modification of global CSS variables
        this_pvs: PVs passed to the script by CSS. Used to control modification of global CSS variables
                    List of PVs, first one is the trigger, then in pairs which copy to their partner.
                    E.g: ["trigger", "PVA-source", "PVA-target", "PVB-source", "PVB-target", "PVC-source", "PVC-target", ]

    Returns:
        None
    """
    # Make sure this has been triggered, then reset the trigger
    actioned = PVUtil.getDouble(this_pvs[0]) == 1
    this_pvs[0].setValue(0)
    if actioned:
        # Skip the trigger PV
        # Copying every other PV to the next one
        this_pvs[2].setValue(PVUtil.getDouble(this_pvs[1]))
        this_pvs[4].setValue(PVUtil.getDouble(this_pvs[3]))
        this_pvs[6].setValue(PVUtil.getDouble(this_pvs[5]))
        this_pvs[8].setValue(PVUtil.getDouble(this_pvs[7]))


# PVs
# pv[0] = loc://GEM:JAWS:FWD:TO:BEAMSCRAPER, triggered
# pv[1-16] = $(P)GEMJAWSET:SAMPLE:HGAP:SP,$(P)MOT:JAWS6:HGAP:SP
copy_pvs(display, pvs)
