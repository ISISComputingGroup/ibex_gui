importPackage(Packages.org.csstudio.opibuilder.scriptUtil);

var mode = PVUtil.getString(pvs[0]);

if(mode=="Running")
	widget.setVisible(true);
else
	widget.setVisible(false);