* install_gui_with_builtin_python.bat        

Install just the ibex GUI to `c:\instrument\apps\Client_E4`
Genie python is not installed/updated in `c:\instrument\apps\python3`
The ibex gui will have a builtin python distribution but will prefer any previous
installation in `c:\instrument\apps\python3` when started

* install_gui_and_external_python.bat

Installs the ibex GUI to `c:\instrument\apps\Client_E4` and also installs/replaces
Genie python in `c:\instrument\apps\python3`
Though the ibex gui will have a builtin python distribution, it will prefer
the installation in `c:\instrument\apps\python3`
        
* install_client.bat

Old name for script, calls `install_gui_and_external_python.bat`
