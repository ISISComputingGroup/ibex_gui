importPackage(Packages.org.csstudio.opibuilder.scriptUtil);

var group = display.getWidget('group');
var test = display.getWidget('test');
var chanlistwidget = display.getWidget('update');
var chanlistpv = chanlistwidget.getPV();

var newlist = '';

var maxcrate = 15;
var maxslot = 15;
var maxchan = 23;
var strlen = 39;
var cratenum = 1;
var slotnum = 1;
var channum = 6;

var actioned = PVUtil.getDouble(pvs[0]);

if (actioned == 1) {
	for (z = 0; z < cratenum; z++){
		var cratename = pvs[1+z];
		var crate = PVUtil.getString(cratename);
		for (x=0; x < slotnum; x++){
			for (y=0; y < channum; y++){
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
} else {
	newlist = 'Not looping';
}

test.setValue(chanlistpv);
chanlistpv.setValue(newlist);