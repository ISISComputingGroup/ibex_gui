import sys
import os

plugins_dir = sys.argv[1]+"\\plugins"

for filename in os.listdir(plugins_dir):
    if filename.startswith("uk.ac.stfc.isis.ibex.preferences"):
        print(plugins_dir+"\\"+filename+"\\resources\\Python3")
        exit
