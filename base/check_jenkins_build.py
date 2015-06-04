import os
import time
import subprocess
import psutil

full_path = os.path.abspath("uk.ac.stfc.isis.ibex.client.product\\target\\products\\ibex.product\\win32\\win32\\x86_64\\ibex-client.exe")

if not os.path.isfile(full_path):
	raise Exception("Executable could not be found")
	
#Get a list of existing javaw.exe processes
PROCNAME = u"javaw.exe"
javaw_procs = []
for proc in psutil.process_iter():
	try:
		if proc.name() == PROCNAME:
			javaw_procs.append(proc.pid)
	except:
		#Throws if proc does not have a name
		pass

#Start the executable
exe = subprocess.Popen(full_path)
time.sleep(10)

#See if the subprocess is running
running = False
for proc in psutil.process_iter():
	try:
		if proc.pid == exe.pid:
			print "It is running!"
			running = True
			break
	except:
		pass

if not running:
	raise Exception("Process was not running - this probably means the executable crashed!")

try:
	exe.kill()
	#Have to kill the undelying javaw process too :o(
	for proc in psutil.process_iter():
		try:
			if proc.name() == PROCNAME and proc.pid not in javaw_procs:
				proc.kill()
				break
		except:
			#Throws if proc does not have a name
			pass
except:
	pass