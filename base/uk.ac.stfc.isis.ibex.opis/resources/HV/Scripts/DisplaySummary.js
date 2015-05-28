importPackage(Packages.org.csstudio.opibuilder.scriptUtil);

var testval = "Testing";

var group = display.getWidget('group');
group.removeAllChildren();
try{
	var chanlist = PVUtil.getString(pvs[0]);	
}
catch(err){
	var chanlist = "";
}
testval = chanlist;
chanlist = chanlist.split(' ');

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

var test = display.getWidget('test');
test.setValue(testval);