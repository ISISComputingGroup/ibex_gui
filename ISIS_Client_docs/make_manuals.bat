@ echo off

set DOCDIR=%~dp0

if not exist %DOCDIR%manuals mkdir %DOCDIR%manuals

echo Generating HTML Developer manual
%PYTHON_HOME%\python.exe %PYTHON_HOME%\Scripts\rst2html.py --stylesheet-path=%DOCDIR%manual.css %DOCDIR%manual_dev.rst %DOCDIR%manuals\manual_dev.html

echo Generating HTML User manual
%PYTHON_HOME%\python.exe %PYTHON_HOME%\Scripts\rst2html.py --stylesheet-path=%DOCDIR%manual.css %DOCDIR%manual_user.rst %DOCDIR%manuals\manual_user.html

echo Generating PDF Developer manual
%PYTHON_HOME%\python.exe %PYTHON_HOME%\Scripts\rst2pdf-script.py %DOCDIR%manual_dev.rst %DOCDIR%manuals\manual_dev.pdf

echo Generating PDF User manual
%PYTHON_HOME%\python.exe %PYTHON_HOME%\Scripts\rst2pdf-script.py %DOCDIR%manual_user.rst %DOCDIR%manuals\manual_user.pdf

xcopy /s /E /i /y %DOCDIR%image %DOCDIR%manuals\image 