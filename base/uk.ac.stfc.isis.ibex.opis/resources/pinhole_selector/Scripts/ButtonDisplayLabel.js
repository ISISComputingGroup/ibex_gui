importPackage(Packages.org.csstudio.opibuilder.scriptUtil);

// PVs
// PV[0] = loc://useToInit, triggered
// pv[1] = $(P)LKUP:$(PH):_SPLITPOSNS.VALA, triggered
// pv[2] = $(P)LKUP:$(PH):_SPLITPOSNS.VALB, triggered
// pv[3] = $(P)LKUP:$(PH):_SPLITPOSNS.VALC, triggered
// pv[4] = $(P)LKUP:$(PH):_SPLITPOSNS.VALD, triggered
// pv[5] = $(P)LKUP:$(PH):_SPLITPOSNS.VALE, triggered
// pv[6] = $(P)LKUP:$(PH):_SPLITPOSNS.VALF, triggered
// pv[7] = $(P)LKUP:$(PH):_SPLITPOSNS.VALG, triggered
// pv[8] = $(P)LKUP:$(PH):_SPLITPOSNS.VALH, triggered
// pv[9] = $(P)LKUP:$(PH):_SPLITPOSNS.VALI, triggered
// pv[10] = $(P)LKUP:$(PH):_SPLITPOSNS.VALJ, triggered
// pv[11] = $(P)LKUP:$(PH):_SPLITPOSNS.VALK, triggered
// pv[12] = $(P)LKUP:$(PH):_SPLITPOSNS.VALL, triggered


var Run0 = "A";
var Load0 = "A";
var Run1 = "B";
var Load1 = "B";
var Run2 = "C";
var Load2 = "C";
var Run3 = "D";
var Load3 = "D";
var Run4 = "E";
var Load4 = "E";
var Run5 = "F";
var Load5 = "F";

if (pvArray[1].isConnected())
	Run0 = Run0 + " = " + PVUtil.getString(pvs[1]);
if (pvArray[2].isConnected())
	Load0 = Load0 + " = " +  PVUtil.getString(pvs[2]);
if (pvArray[3].isConnected())
	Run1 = Run1 + " = " +  PVUtil.getString(pvs[3]);
if (pvArray[4].isConnected())
	Load1 = Load1 + " = " +  PVUtil.getString(pvs[4]);
if (pvArray[5].isConnected())
	Run2 = Run2 + " = " +  PVUtil.getString(pvs[5]);
if (pvArray[6].isConnected())
	Load2 = Load2 + " = " +  PVUtil.getString(pvs[6]);
if (pvArray[7].isConnected())
	Run3 = Run3 + " = " +  PVUtil.getString(pvs[7]);
if (pvArray[8].isConnected())
	Load3 = Load3 + " = " +  PVUtil.getString(pvs[8]);
if (pvArray[9].isConnected())
	Run4 = Run4 + " = " +  PVUtil.getString(pvs[9]);
if (pvArray[10].isConnected())
	Load4 = Load4 + " = " +  PVUtil.getString(pvs[10]);
if (pvArray[11].isConnected())
	Run5 = Run5 + " = " +  PVUtil.getString(pvs[11]);
if (pvArray[12].isConnected())
	Load5 = Load5 + " = " +  PVUtil.getString(pvs[12]);

display.getWidget("Running_Button_0").setPropertyValue("text",  Run0);
display.getWidget("Loading_Button_0").setPropertyValue("text",  Load0);
display.getWidget("Running_Button_1").setPropertyValue("text",  Run1);
display.getWidget("Loading_Button_1").setPropertyValue("text",  Load1);
display.getWidget("Running_Button_2").setPropertyValue("text",  Run2);
display.getWidget("Loading_Button_2").setPropertyValue("text",  Load2);
display.getWidget("Running_Button_3").setPropertyValue("text",  Run3);
display.getWidget("Loading_Button_3").setPropertyValue("text",  Load3);
display.getWidget("Running_Button_4").setPropertyValue("text",  Run4);
display.getWidget("Loading_Button_4").setPropertyValue("text",  Load4);
display.getWidget("Running_Button_5").setPropertyValue("text",  Run5);
display.getWidget("Loading_Button_5").setPropertyValue("text",  Load5);