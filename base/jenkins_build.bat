set M2=C:\Program Files\Apache Software Foundation\apache-maven-3.2.2\bin\
set JAVA_HOME=C:\Program Files\Java\jdk1.7.0_13\
set PYTHON=C:\Python27
set LOCINSTALLDIR=c:\Installers\CSStudio_ISIS\

set PATH=%M2%;%JAVA_HOME%;%PYTHON%;%PATH%

call mvn --settings=%CD%\..\mvn_user_settings.xml -f uk.ac.stfc.isis.ibex.tycho.parent\pom.xml clean verify
if %errorlevel% neq 0 exit /b %errorlevel%

REM Even if the build is successful this does not mean the executable works!
REM python.exe check_jenkins_build.py
REM if %errorlevel% neq 0 exit /b %errorlevel%

REM Copy zip to installs area
REM Delete older versions?
REM set INSTALLDIR=%LOCINSTALLDIR%\SVN%SVN_REVISION%
REM mkdir %INSTALLDIR%

REM add svn revision number
REM echo %SVN_REVISION% > SVN_REVISION.txt
REM C:\"Program Files"\7-Zip\7z.exe a uk.ac.stfc.isis.ibex.tycho.repository\target\products\repository.product-win32.win32.x86_64.zip SVN_REVISION.txt
REM if %errorlevel% neq 0 exit /b %errorlevel%

REM copy zips to installs
REM copy uk.ac.stfc.isis.ibex.tycho.repository\target\products\*.zip %INSTALLDIR%\.
REM if %errorlevel% neq 0 exit /b %errorlevel%