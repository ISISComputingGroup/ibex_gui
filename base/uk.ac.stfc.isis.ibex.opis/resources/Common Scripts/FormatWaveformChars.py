from org.csstudio.opibuilder.scriptUtil import PVUtil

waveform = [" "]

for c in PVUtil.getLongArray(pvs[0]):
	if c == 0:
		break
	waveform.append(chr(c))

in_string = "".join(waveform)
widget.setPropertyValue("text", in_string)
