import sys
import os

plugins_dir = os.path.join(sys.argv[1], "plugins")

for filename in os.listdir(plugins_dir):
    if filename.startswith("uk.ac.stfc.isis.ibex.preferences"):
        print(os.path.join(plugins_dir, filename, "resources", "Python3"))
