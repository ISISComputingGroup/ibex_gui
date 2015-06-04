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
REM python.exe check_jenkins_build.py
REM if %errorlevel% neq 0 exit /b %errorlevel%

REM Copy zip to installs area
REM Delete older versions?
REM the password for isis\builder is contained in the BUILDERPW system environment variable on the build server 
REM net use p: /d
REM net use p: \\isis\inst$ /user:isis\builder %BUILDERPW%

REM python.exe ..\purge_archive_client.py

REM set INSTALLDIR=%LOCINSTALLDIR%\SVN%SVN_REVISION%
REM set INSTALLBASEDIR=p:\Kits$\CompGroup\ICP\Client
REM set INSTALLDIR=%INSTALLBASEDIR%\SVN%SVN_REVISION%\BUILD%BUILD_NUMBER%
REM if not exist "%INSTALLDIR%\Client" (
    REM @echo Creating client directory %INSTALLDIR%\Client
    REM mkdir %INSTALLDIR%\Client
REM )

REM robocopy %CD%\org.csstudio.isis.ibex.product\target\products\ibex.product\win32\win32\x86_64 %INSTALLDIR%\Client /MIR /R:1 /NFL /NDL /NP
REM if %errorlevel% geq 4 (
    REM if not "%INSTALLDIR%" == "" (
        REM @echo Removing invalid client directory %INSTALLDIR%\Client
        REM rd /q /s %INSTALLDIR%\Client
    REM )
    REM @echo Client copy failed
    REM exit /b 1
REM )

REM Copy the genie_python too
REM mkdir %INSTALLDIR%\genie_python
REM copy /Y %GENIEPYTHONDIR%\*.* %INSTALLDIR%\genie_python\.
REM if %errorlevel% neq 0 (
    REM @echo Genie python copy failed
    REM exit /b %errorlevel%
REM )

REM Copy EPICS_UTILS
REM mkdir %INSTALLDIR%\EPICS_UTILS\
REM copy /Y %INSTALLBASEDIR%\EPICS_UTILS\*.* %INSTALLDIR%\EPICS_UTILS\.
REM if %errorlevel% neq 0 (
    REM @echo Epics utils copy failed
    REM exit /b %errorlevel%
REM )
