importPackage(Packages.org.csstudio.opibuilder.scriptUtil);

var group = display.getWidget('group');
group.removeAllChildren();
try{
	var fullchanlist = PVUtil.getString(pvs[1]);	
}
catch(err){
	var fullchanlist = "";
}
fullchanlist = fullchanlist.split(' ');
var chanlist = [];
var testval = '';

for (i=0; i < fullchanlist.length; i++){
	var chan =  fullchanlist[i].replace(" ","");
	if (chan.slice(-1)==','){
		chan = chan.replace(",","");
	}
	chanlist[chanlist.length] = chan;
}

var maxcrate = 15;
var maxslot = 15;
var maxchan = 23;
var cratenum = 2;
var slotnum = 2;
var channum = 3;

for (z=0; z < cratenum; z++){
	var cratename = pvs[2+z];
	var crate = PVUtil.getString(cratename);
	for (x=0; x < slotnum; x++){
		for (y=0; y < channum; y++){
			var avail = crate + ':' + x + ':' + y;
			var target = WidgetUtil.createWidgetModel("org.csstudio.opibuilder.widgets.linkingContainer");
			target.setPropertyValue('opi_file', "HVChannelSummaryMaintenance.opi");
			target.setPropertyValue('auto_size','true');
			target.setPropertyValue('zoom_to_fit','false');
			target.setPropertyValue('border_style',0);
			target.setPropertyValue('name',avail);
			target.addMacro('SEL',avail);
			group.addChildToBottom(target);
			container = display.getWidget(avail);
			for(i=0; i<chanlist.length;i++)
			{
				if (avail == chanlist[i]){
					var inlistwidget = container.getChild('Include');
					inlistwidget.setValue(1);
				}
			}
		}
	}
}

var test = display.getWidget('test');
test.setValue(testval);