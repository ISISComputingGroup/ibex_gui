importPackage(Packages.org.csstudio.opibuilder.scriptUtil);

// PVs
// pv[0] = loc://CAEN:CHANLIST:UPDATE, triggered
// pv[1] = $(P)CAEN:crates.ZRST
// pv[2] = $(P)CAEN:crates.ONST
// pv[3] = $(P)CAEN:crates.TWST
// pv[4] = $(P)CAEN:crates.THST
// pv[5] = $(P)CAEN:crates.FRST
// pv[6] = $(P)CAEN:crates.FVST
// pv[7] = $(P)CAEN:crates.SXST
// pv[8] = $(P)CAEN:crates.SVST
// pv[9] = $(P)CAEN:crates.EIST
// pv[10] = $(P)CAEN:crates.NIST
// pv[11] = $(P)CAEN:crates.TEST
// pv[12] = $(P)CAEN:crates.ELST
// pv[13] = $(P)CAEN:crates.TVST
// pv[14] = $(P)CAEN:crates.TTST
// pv[15] = $(P)CAEN:crates.FTST
// pv[16] = $(P)CAEN:crates.FFST

// Get the display objects and PVs for interaction
var group = display.getWidget('group');
var test = display.getWidget('test');
var chanlistwidget = display.getWidget('update');
var chanlistpv = chanlistwidget.getPV();

// Clear the list
var newlist = '';

// Values to control the search length for all channels (must match the values in DisplayChannels)
var maxcrate = 2;
var maxslot = 5;
var maxchan = 10;

var actioned = PVUtil.getDouble(pvs[0]);

// Loop through the channels, and if selected add them to the list
if (actioned == 1) {
	for (z = 0; z < maxcrate; z++){
		var cratename = pvs[1+z];
		if (cratename != "") {
			var crate = PVUtil.getString(cratename);
			for (x=0; x < maxslot; x++){
				for (y=0; y < maxchan; y++){
					var avail = crate + ':' + x + ':' + y;
					var container = display.getWidget(avail);
					var inlistwidget = container.getChild('Include');
					var include = inlistwidget.getValue();
					if (include == 1){
						newlist = newlist + ' ' + avail;
					}
				}
			}
		} 
	}
} else {
	newlist = 'Not looping';
}

// Update the PV value
chanlistpv.setValue(newlist);