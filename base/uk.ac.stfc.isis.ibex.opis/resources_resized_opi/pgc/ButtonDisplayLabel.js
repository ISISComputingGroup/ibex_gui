importPackage(Packages.org.csstudio.opibuilder.scriptUtil);
for (i = 1; i <= 3; i++)
	if (pvArray[i].isConnected())
		display.getWidget("Running_Button_"+(i-1).toString()).setPropertyValue("text",  PVUtil.getString(pvs[i]));
