In order to build the manuals, you require a few python packages.

* Make sure python 2.7 is installed on your computer
* Set the environment variable %PYTHON_HOME%, to point at the python install directory 
    (In windows, do this by going to My Computer > System Properties > Advanced System Settings > Environment Variables)
* Install pip (https://pip.pypa.io/en/latest/installing.html)
* From the command line install sphinx, rst2pdf and xhtml2pdf. You can do this by running the following commands:

set http_proxy=http://wwwcache.rl.ac.uk:8080
set https_proxy=http://wwwcache.rl.ac.uk:8080
%PYTHON_HOME%\Scripts\pip.exe install sphinx
%PYTHON_HOME%\Scripts\pip.exe install rst2pdf
%PYTHON_HOME%\Scripts\pip.exe install xhtml2pdf


You should now be able to build the html and pdf versions of the manuals by running 'make_manuals.bat'