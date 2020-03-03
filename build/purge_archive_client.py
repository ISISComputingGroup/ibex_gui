import os, time, sys, datetime, glob, shutil

base_path = r"\\isis\inst$\kits$\CompGroup\ICP"
paths = [
    r"{}\script_generator\BUILD*".format(base_path),
    r"{}\Client_E4\BUILD*".format(base_path)]
    

dir_list = []
for path in paths:
    for d in glob.glob(path):
        if os.path.isdir(d):
            file_modified = datetime.datetime.fromtimestamp(os.path.getmtime(d))
            if datetime.datetime.now() - file_modified > datetime.timedelta(hours=24*60):
                dir_list.append(d)

for d in dir_list:
    print("Deleting {}".format(d))
    shutil.rmtree(d)
