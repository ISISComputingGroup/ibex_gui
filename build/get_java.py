import os
from six.moves import urllib_request
import zipfile
import sys

OPENJDK_URL = "https://download.java.net/java/GA/jdk11/13/GPL/openjdk-11.0.1_windows-x64_bin.zip"
JDK_DIR = os.path.join(os.getcwd(), "jdk")
ARCHIVE_NAME = "{}.zip".format(JDK_DIR)

if os.path.exists(JDK_DIR):
    print("Java already exists, nothing to do")
    sys.exit(0)
	
print("Downloading OpenJDK from {} to {}".format(OPENJDK_URL, ARCHIVE_NAME))

urllib_request.urlretrieve(OPENJDK_URL, ARCHIVE_NAME)

print("Extracting OpenJDK from {} to {}".format(ARCHIVE_NAME, JDK_DIR))

with zipfile.ZipFile(ARCHIVE_NAME, "r") as zip_ref:
    zip_ref.extractall(JDK_DIR)
	
print("Removing temporary archive from {}".format(ARCHIVE_NAME))

os.remove(ARCHIVE_NAME)

print("Done")
