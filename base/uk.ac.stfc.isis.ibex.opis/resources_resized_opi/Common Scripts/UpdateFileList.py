from org.csstudio.opibuilder.scriptUtil import PVUtil
import zlib

waveform = []

for c in PVUtil.getLongArray(pvs[0]):
	if c == 0:
		break
	waveform.append(chr(c))

in_string = "".join(waveform).strip().replace(" ", "")
in_string = zlib.decompress(in_string.decode("hex"))

#remove square brackets and speech marks
in_string = in_string.replace('"','')[1:-1]

file_list = in_string.split(",")
widget.setPropertyValue("items", file_list)