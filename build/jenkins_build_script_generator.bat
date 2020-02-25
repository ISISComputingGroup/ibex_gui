REM %~dp0 expands to directory where this file lives
setlocal

set BASEDIR=%~dp0

set M2=%MAVEN%bin
set PYTHON3=C:\Instrument\Apps\Python3\python.exe
set PYTHON_HOME=C:\Instrument\Apps\Python3

REM We bundle our own JRE with the script generator, this is where it is
set JRELOCATION=p:\Kits$\CompGroup\ICP\ibex_client_jre

set PATH=%M2%;%PATH%

call build_script_generator.bat
if %errorlevel% neq 0 exit /b %errorlevel%

@echo on

REM set EXIT=YES will change error code to 1 if not set previously so store the current
set build_error_level=%errorlevel%

REM Whether to deploy
set EXIT=YES
if "%DEPLOY%" == "YES" set EXIT=NO
if "%RELEASE%" == "YES" set EXIT=NO
if "%EXIT%" == "YES" exit /b %build_error_level%

REM Copy zip to installs area
REM Delete older versions?
REM the password for isis\IBEXbuilder is contained in the BUILDERPW system environment variable on the build server
net use p: /d /yes
net use p: \\isis\inst$

%PYTHON3% purge_archive_client.py

set TARGET_DIR=built_script_gen

REM Don't group these. Bat expands whole if at once, not sequentially
if "%RELEASE%" == "YES" (
    set RELEASE_DIR=p:\Kits$\CompGroup\ICP\Releases\%GIT_BRANCH:~8%
    set RELEASE_VERSION=%GIT_BRANCH:~8%    
) else (
    set RELEASE_VERSION=devel-%GIT_COMMIT:~0,7%
)
if "%RELEASE%" == "YES" set INSTALLBASEDIR=%RELEASE_DIR%\script_generator
if "%RELEASE%" == "YES" set INSTALLDIR=%INSTALLBASEDIR%

if not "%RELEASE%" == "YES" (
        set INSTALLBASEDIR=p:\Kits$\CompGroup\ICP\script_generator
) 

if not "%RELEASE%" == "YES" set INSTALLDIR=%INSTALLBASEDIR%\BUILD%BUILD_NUMBER%
REM Set a symlink for folder BUILD_LATEST to point to most recent build
if not "%RELEASE%" == "YES" set INSTALLLINKDIR=%INSTALLBASEDIR%\BUILD_LATEST

if "%RELEASE%" == "YES" (
    if not exist "%RELEASE_DIR%" (
        mkdir %RELEASE_DIR%
    )
    RMDIR /S /Q %INSTALLDIR%
    @echo Creating script generator directory %INSTALLDIR%
    if not exist "%INSTALLDIR%" (
        mkdir %INSTALLDIR%
    )
) else (
    if exist "%INSTALLDIR%\script_generator" (
        @echo Creating script generator directory %INSTALLDIR%\script_generator
        mkdir %INSTALLDIR%\script_generator
    )
)

robocopy %CD%\..\%TARGET_DIR% %INSTALLDIR%\script_generator /MIR /R:1 /NFL /NDL /NP
if %errorlevel% geq 4 (
    if not "%INSTALLDIR%" == "" (
        @echo Removing invalid script generator directory %INSTALLDIR%\script_generator
        rd /q /s %INSTALLDIR%\script_generator
    )
    @echo Script generator copy failed
    exit /b 1
)

REM Copy the JRE across 
robocopy %JRELOCATION% %INSTALLDIR%\script_generator\jre /MIR /R:1 /NFL /NDL /NP
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
copy /Y %BASEDIR%\install_script_generator.bat %INSTALLDIR%
if %errorlevel% neq 0 (
    @echo Installl script generator copy failed
    exit /b %errorlevel%
)

@echo %BUILD_NUMBER%> %INSTALLDIR%\script_generator\BUILD_NUMBER.txt
@echo %RELEASE_VERSION%> %INSTALLDIR%\script_generator\VERSION.txt

@echo Copy complete>%INSTALLDIR%\COPY_COMPLETE.txt

if not "%RELEASE%" == "YES" (
    @echo %BUILD_NUMBER%>%INSTALLDIR%\..\LATEST_BUILD.txt 
)

REM build MSI kit
REM call build_msi.bat %INSTALLDIR%
