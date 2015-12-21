importPackage(Packages.org.csstudio.opibuilder.scriptUtil);

// PVs
// pv[0] = loc://Mode, triggered
// pv[1] = $(P)LKUP:$(PH):IPOSN, triggered

var mode = PVUtil.getString(pvs[0]);
var loc = PVUtil.getDouble(pvs[1]);

var name = widget.getPropertyValue("name");
var runnDispVerifier = "RAct";
var runner = name.indexOf(runnDispVerifier) > -1;
var posString = name.split("-")[1];
var pos = parseInt(name.split("-")[1]);
var displayUnknown = false;

if(mode=="Running" && runner)
	widget.setVisible(true);
else if (mode=="Loading" && !runner)
	widget.setVisible(true);
else
	widget.setVisible(false);

if ((loc%2)==0) {
	loc = loc/2;
	if (!runner)
		displayUnknown = true; }
else {
	loc = (loc - 1)/2;
	if (runner)
		displayUnknown = true; }

var sum = loc + pos;
var check = sum-6;
var use = 100;

if (check < 0)
	use = sum;
else
	if (check < sum)
		use = check;
	else
		use = sum;

if (use == 0)
	widget.setPropertyValue("image_file","label_A.png");
else if (use == 1)
	widget.setPropertyValue("image_file","label_B.png");
else if (use == 2)
	widget.setPropertyValue("image_file","label_C.png");
else if (use == 3)
	widget.setPropertyValue("image_file","label_D.png");
else if (use == 4)
	widget.setPropertyValue("image_file","label_E.png");
else if (use == 5)
	widget.setPropertyValue("image_file","label_F.png");
else
	widget.setPropertyValue("image_file","inactive.png");

if (displayUnknown)
	widget.setPropertyValue("image_file","label_Unknown.png");	