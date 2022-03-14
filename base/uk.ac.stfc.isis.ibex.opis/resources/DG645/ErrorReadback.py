from org.csstudio.opibuilder.scriptUtil import PVUtil

# This script is needed because records can only hold strings
# up to 40 characters - too small for error queue

error_queue = []
err_count = 10

for i in range(err_count):
    error_queue.append(PVUtil.getString(pvs[err_count-i-1]))

displayText = ""
for error in error_queue:
    if error != "":
        displayText += error + "\n"
widget.setPropertyValue("text", displayText)
