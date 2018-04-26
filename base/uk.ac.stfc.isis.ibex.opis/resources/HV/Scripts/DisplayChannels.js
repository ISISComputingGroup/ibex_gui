importPackage(Packages.org.csstudio.opibuilder.scriptUtil);

// PVs
// pv[0] = $(P)CAEN:crates, triggered
// pv[1] = $(P)CAEN:CHANLIST, triggered
// pv[2] = $(P)CAEN:crates.ZRST
// pv[3] = $(P)CAEN:crates.ONST
// pv[4] = $(P)CAEN:crates.TWST
// pv[5] = $(P)CAEN:crates.THST
// pv[6] = $(P)CAEN:crates.FRST
// pv[7] = $(P)CAEN:crates.FVST
// pv[8] = $(P)CAEN:crates.SXST
// pv[9] = $(P)CAEN:crates.SVST
// pv[10] = $(P)CAEN:crates.EIST
// pv[11] = $(P)CAEN:crates.NIST
// pv[12] = $(P)CAEN:crates.TEST
// pv[13] = $(P)CAEN:crates.ELST
// pv[14] = $(P)CAEN:crates.TVST
// pv[15] = $(P)CAEN:crates.TTST
// pv[16] = $(P)CAEN:crates.FTST
// pv[17] = $(P)CAEN:crates.FFST

// Get and clear the group container prior to display
var group = display.getWidget('group');
group.removeAllChildren();

// Get the list of entries in the channel list
try{
	var fullchanlist = PVUtil.getString(pvs[1]);	
}
catch(err){
	var fullchanlist = "";
}
// Split the chanlist into an array
fullchanlist = fullchanlist.split(' ');
var chanlist = [];
var testval = '';

// format the chanlist array for searching
for (i=0; i < fullchanlist.length; i++){
	var chan =  fullchanlist[i].replace(" ","");
	if (chan.slice(-1)==','){
		chan = chan.replace(",","");
	}
	chanlist[chanlist.length] = chan;
}

// Values to control the search length for all channels (must match the values in UpdateChannels)
var maxcrate = 2;
var maxslot = 5;
var maxchan = 10;

// Loop through and generate an OPI linked to each channel displaying the name for inclusion in the chan list
for (z=0; z < maxcrate; z++){
	var cratename = pvs[2+z];
	if (cratename != "") {
		var crate = PVUtil.getString(cratename);
		for (x=0; x < maxslot; x++){
			for (y=0; y < maxchan; y++){
				var avail = crate + ':' + x + ':' + y;
				var target = WidgetUtil.createWidgetModel("org.csstudio.opibuilder.widgets.linkingContainer");
				target.setPropertyValue('opi_file', "HVChannelSummaryMaintenance.opi");
				target.setPropertyValue('auto_size', true);
				target.setPropertyValue('zoom_to_fit', false);
				target.setPropertyValue('border_style',0);
				target.setPropertyValue('name', avail);
				target.addMacro('SEL',avail);
				group.addChildToBottom(target);
				container = display.getWidget(avail);
				// Check if the channel is in in the list, and check the bos if it is
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
}