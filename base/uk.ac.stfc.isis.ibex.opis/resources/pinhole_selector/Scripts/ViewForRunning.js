importPackage(Packages.org.csstudio.opibuilder.scriptUtil);

// PVs
// pv[0] = loc://Mode, triggered

var mode = PVUtil.getString(pvs[0]);

if(mode=="Running")
	widget.setVisible(true);
else
	widget.setVisible(false);