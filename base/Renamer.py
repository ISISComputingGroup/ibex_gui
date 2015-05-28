# A small utility to rename the prefix of an eclipse project

import os
import shutil

for root, dirs, files in os.walk("."):
    for name in dirs:
        if name == "target" or name == "bin":
            full_path = os.path.join(root, name)
            print "Removing: " + full_path
            if os.path.exists(full_path):
                shutil.rmtree(full_path)