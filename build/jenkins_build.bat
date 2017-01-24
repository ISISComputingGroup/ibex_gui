REM %~dp0 expands to directory where this file lives
set BASEDIR=%~dp0

set M2=%MAVEN%bin
set PYTHON=C:\Python27
set PYTHON_HOME=C:\Python27
set LOCINSTALLDIR=c:\Installers\CSStudio_ISIS\
set GENIEPYTHONDIR=c:\Installers\genie_python
set ZIPLOCATION=c:\Installers\client_zip

REM We bundle our own JRE with the client, this is where it is
set JRELOCATION=p:\Kits$\CompGroup\ICP\ibex_client_jre

REM Find latest Java 7 version
for /F %%f in ('dir "C:\Program Files\Java\jdk1.*" /b') do set JAVA_HOME=C:\Program Files\Java\%%f

set PATH=%M2%;%JAVA_HOME%;%PYTHON%;%PATH%

call build.bat
if %errorlevel% neq 0 exit /b %errorlevel%

@echo on

REM Whether to deploy
set EXIT=YES
if "%DEPLOY%" == "YES" set EXIT=NO
if "%RELEASE%" == "YES" set EXIT=NO
if "%EXIT%" == "YES" exit

REM Copy zip to installs area
REM Delete older versions?
REM the password for isis\builder is contained in the BUILDERPW system environment variable on the build server
net use p: /d
net use p: \\isis\inst$ /user:isis\builder %BUILDERPW%

python.exe purge_archive_client.py

REM Don't group these. Bat expands whole if at once, not sequentially
if "%RELEASE%" == "YES" set RELEASE_DIR=p:\Kits$\CompGroup\ICP\Releases\%GIT_BRANCH:~15%
if "%RELEASE%" == "YES" set INSTALLBASEDIR=%RELEASE_DIR%\Client
if "%RELEASE%" == "YES" set INSTALLDIR=%INSTALLBASEDIR%

if not "%RELEASE%" == "YES" set INSTALLBASEDIR=p:\Kits$\CompGroup\ICP\Client
if not "%RELEASE%" == "YES" set INSTALLDIR=%INSTALLBASEDIR%\BUILD%BUILD_NUMBER%
REM Set a symlink for folder BUILD_LATEST to point to most recent build
if not "%RELEASE%" == "YES" set INSTALLLINKDIR=%INSTALLBASEDIR%\BUILD_LATEST

if "%RELEASE%" == "YES" (
    if not exist "%RELEASE_DIR%" (
        mkdir %RELEASE_DIR%
    )
    RMDIR /S /Q %INSTALLDIR%
    @echo Creating client directory %INSTALLDIR%
    if not exist "%INSTALLDIR%" (
        mkdir %INSTALLDIR%
    )
) else (
    if exist "%INSTALLDIR%\Client" (
        @echo Creating client directory %INSTALLDIR%\Client
        mkdir %INSTALLDIR%\Client
    )
)

robocopy %CD%\..\base\uk.ac.stfc.isis.ibex.client.product\target\products\ibex.product\win32\win32\x86_64 %INSTALLDIR%\Client /MIR /R:1 /NFL /NDL /NP
if %errorlevel% geq 4 (
    if not "%INSTALLDIR%" == "" (
        @echo Removing invalid client directory %INSTALLDIR%\Client
        rd /q /s %INSTALLDIR%\Client
    )
    @echo Client copy failed
    exit /b 1
)

REM Copy the JRE across 
robocopy %JRELOCATION% %INSTALLDIR%\Client\jre /MIR /R:1 /NFL /NDL /NP
if %errorlevel% geq 4 (
    @echo Failed to copy JRE across
    exit /b 1
)

if not "%RELEASE%"=="YES" (
    if exist "%INSTALLLINKDIR%" (
        rmdir "%INSTALLLINKDIR%"
    )
    mklink /J "%INSTALLLINKDIR%" "%INSTALLDIR%"
)

REM Copy the install script across
cd %BASEDIR%
copy /Y %BASEDIR%\install_client.bat %INSTALLDIR%
if %errorlevel% neq 0 (
    @echo Installl client copy failed
    exit /b %errorlevel%
)



REM Create the installer
if exist C:\"Program Files (x86)"\7-Zip\7z.exe set ZIPEXE=C:\"Program Files (x86)"\7-Zip\7z.exe
if exist C:\"Program Files"\7-Zip\7z.exe set ZIPEXE=C:\"Program Files"\7-Zip\7z.exe

REM First copy the zip with genie_python already in it
copy %ZIPLOCATION%\installer.7z .
if %errorlevel% neq 0 (
    @echo Could not find genie_python zip
    exit /b %errorlevel%
)

REM Add EPICS_UTILS and the Client
%ZIPEXE% a installer.7z P:\Kits$\CompGroup\ICP\Client\EPICS_UTILS
if %errorlevel% neq 0 (
    @echo Could not add EPICS_UTILS to zip
    exit /b %errorlevel%
)
%ZIPEXE% a installer.7z %INSTALLDIR%\Client
if %errorlevel% neq 0 (
    @echo Could not add Client to zip
    exit /b %errorlevel%
)

REM Add the install_client.bat to the archive
%ZIPEXE% a installer.7z install_client_zip.bat
if %errorlevel% neq 0 (
    @echo Could not add install_client_zip.bat to zip
    exit /b %errorlevel%
)

REM Build the installer
copy /b 7zsd_All.sfx + installer_config.txt + installer.7z ClientInstaller.exe
if %errorlevel% neq 0 (
    @echo Installer build failed
    exit /b %errorlevel%
)

REM Copy to Kits
xcopy ClientInstaller.exe %INSTALLDIR%
if %errorlevel% neq 0 (
    @echo Installer copy failed
    exit /b %errorlevel%
)

@echo Copy complete>%INSTALLDIR%\COPY_COMPLETE.txt

REM Delete local copies
del installer.7z
del ClientInstaller.exe
