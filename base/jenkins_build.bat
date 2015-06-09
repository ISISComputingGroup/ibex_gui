set M2=C:\Program Files\Apache Software Foundation\apache-maven-3.2.2\bin\
set PYTHON=C:\Python27
set PYTHON_HOME=C:\Python27
set LOCINSTALLDIR=c:\Installers\CSStudio_ISIS\
set GENIEPYTHONDIR=c:\Installers\genie_python\

REM Find latest Java 7 version
for /F %%f in ('dir "C:\Program Files\Java\jdk1.7*" /b') do set JAVA_HOME=C:\Program Files\Java\%%f
	
set PATH=%M2%;%JAVA_HOME%;%PYTHON%;%PATH%

call mvn --settings=%CD%\..\mvn_user_settings.xml -f uk.ac.stfc.isis.ibex.client.tycho.parent\pom.xml clean verify
if %errorlevel% neq 0 exit /b %errorlevel%

REM Even if the build is successful this does not mean the executable works!
python.exe check_jenkins_build.py
if %errorlevel% neq 0 exit /b %errorlevel%

REM Copy zip to installs area
REM Delete older versions?
REM the password for isis\builder is contained in the BUILDERPW system environment variable on the build server 
net use p: /d
net use p: \\isis\inst$ /user:isis\builder %BUILDERPW%

python.exe purge_archive_client.py

set INSTALLBASEDIR=p:\Kits$\CompGroup\ICP\Client
set INSTALLDIR=%INSTALLBASEDIR%\BUILD%BUILD_NUMBER%
if not exist "%INSTALLDIR%\Client" (
    @echo Creating client directory %INSTALLDIR%\Client
    mkdir %INSTALLDIR%\Client
)

robocopy %CD%\uk.ac.stfc.isis.ibex.client.product\target\products\ibex.product\win32\win32\x86_64 %INSTALLDIR%\Client /MIR /R:1 /NFL /NDL /NP
if %errorlevel% geq 4 (
    if not "%INSTALLDIR%" == "" (
        @echo Removing invalid client directory %INSTALLDIR%\Client
        rd /q /s %INSTALLDIR%\Client
    )
    @echo Client copy failed
    exit /b 1
)

REM Copy the genie_python too
mkdir %INSTALLDIR%\genie_python
copy /Y %GENIEPYTHONDIR%\*.* %INSTALLDIR%\genie_python\.
if %errorlevel% neq 0 (
    @echo Genie python copy failed
    exit /b %errorlevel%
)

REM Copy EPICS_UTILS
mkdir %INSTALLDIR%\EPICS_UTILS\
copy /Y %INSTALLBASEDIR%\EPICS_UTILS\*.* %INSTALLDIR%\EPICS_UTILS\.
if %errorlevel% neq 0 (
    @echo Epics utils copy failed
    exit /b %errorlevel%
)
