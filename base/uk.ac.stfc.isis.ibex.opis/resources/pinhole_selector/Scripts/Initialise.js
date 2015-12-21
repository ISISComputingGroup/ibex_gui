importPackage(Packages.org.csstudio.opibuilder.scriptUtil);
importPackage(Packages.org.csstudio.platform.data);

// PVs
// pv[0] = loc://useToInit, triggered
// pv[1] = loc://Mode, not triggered

display.getWidget("Mode Selector").setValue("Running");
pvs[1].setValue("Running");