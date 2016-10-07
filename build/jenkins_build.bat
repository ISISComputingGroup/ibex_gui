REM %~dp0 expands to directory where this file lives
set BASEDIR=%~dp0

set M2=%MAVEN%bin
set PYTHON=C:\Python27
set PYTHON_HOME=C:\Python27
set LOCINSTALLDIR=c:\Installers\CSStudio_ISIS\
set GENIEPYTHONDIR=c:\Installers\genie_python
set ZIPLOCATION=c:\Installers\client_zip

REM Find latest Java 7 version
for /F %%f in ('dir "C:\Program Files\Java\jdk1.*" /b') do set JAVA_HOME=C:\Program Files\Java\%%f

set PATH=%M2%;%JAVA_HOME%;%PYTHON%;%PATH%

REM call build.bat
REM if %errorlevel% neq 0 exit /b %errorlevel%

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

if "%RELEASE%" == "YES" (
    set RELEASE_DIR=p:\Kits$\CompGroup\ICP\Releases\%GIT_BRANCH:~15%
    set INSTALLBASEDIR=%RELEASE_DIR%\Client
    set INSTALLDIR=%INSTALLBASEDIR%
) else (
    set INSTALLBASEDIR=p:\Kits$\CompGroup\ICP\Client
    set INSTALLDIR=%INSTALLBASEDIR%\BUILD%BUILD_NUMBER%
    REM Set a symlink for folder BUILD_LATEST to point to most recent build
    set INSTALLLINKDIR=%INSTALLBASEDIR%\BUILD_LATEST
)
