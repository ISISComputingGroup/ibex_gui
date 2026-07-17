import os
import xml.etree.ElementTree as ET
import sys

fail=False
filetype=".tests"

pathsxml = r"C:\Instrument\dev\ibex_gui\base\uk.ac.stfc.isis.ibex.client.tycho.parent\pom.xml"
pathsfolder = r"C:\Instrument\dev\ibex_gui\base"

folderFiles = os.listdir(pathsfolder)
testFiles = []
for file in folderFiles:
    if file[-(len(filetype)):] == filetype:
        testFiles.append(file)
  
tree = ET.parse(pathsxml)
root = tree.getroot()
xmlmodules=[]
for module in root.iter("{http://maven.apache.org/POM/4.0.0}module"):
    xmlmodules.append(module.text)

xmlmodulenames = []
for name in xmlmodules:
    if name[-(len(filetype)):] == filetype:
        xmlmodulenames.append(name[3:])
        
missingtests=[]
for testfile in testFiles:
    if testfile not in xmlmodulenames:
        fail = True
        missingtests.append(testfile)

if fail == True:
    print(f".test files in base/ not found in pom.xml")
    print(f"Missing files: {missingtests}")
    print(f"To fix this: add missing files to the list in client.tycho.parent/pom.xml")
    sys.exit(1)
else:
    sys.exit(0)