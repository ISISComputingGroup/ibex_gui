import os, time, sys, datetime, glob, shutil

path = r"\\isis\inst$\kits$\CompGroup\ICP\Client\BUILD*"

dir_list = []
for d in glob.glob(path):
            if os.path.isdir(d):
                file_modified = datetime.datetime.fromtimestamp(os.path.getmtime(d))
                if datetime.datetime.now() - file_modified > datetime.timedelta(hours=24*60):
                    dir_list.append(d)

for d in dir_list:
    print "Deleting ",d
    shutil.rmtree(d)


#    for dirpath, dirnames, filenames in os.walk(d) :
#        for file in filenames :
#            curpath = os.path.join(dirpath, file)
#            if os.path.isfile(curpath) :
#                print curpath
#				os.remove(curpath)
