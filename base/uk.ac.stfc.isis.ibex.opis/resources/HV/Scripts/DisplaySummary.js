importPackage(Packages.org.csstudio.opibuilder.scriptUtil);

// PVs
// pv[] = $(P)CAEN:CHANLIST

// Get and clear the group container prior to display
var group = display.getWidget('group');
group.removeAllChildren();

// Get the list of entries in the channel list
try{
	var chanlist = PVUtil.getString(pvs[0]);	
}
catch(err){
	var chanlist = "";
}
chanlist = chanlist.split(' ');

// Loop through the chan list and display channel information as appropriate
for (i = 0; i < chanlist.length; i++){
	var chan = chanlist[i];
	if (chan.slice(-1)==','){
		chan = chan.replace(",","");
	}
	if ((chan != '')&&(chan != ',')){
		target = WidgetUtil.createWidgetModel("org.csstudio.opibuilder.widgets.linkingContainer")
		target.setPropertyValue('opi_file', "HVChannelMonitorSummary.opi")
		target.setPropertyValue('auto_size','true')
		target.setPropertyValue('zoom_to_fit','false')
		target.setPropertyValue('border_style',0)
		target.addMacro('SEL',chan)
		group.addChildToBottom(target)
	}
}
