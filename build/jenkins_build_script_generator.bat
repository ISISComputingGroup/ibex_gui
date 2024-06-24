REM %~dp0 expands to directory where this file lives
setlocal enabledelayedexpansion

set BASEDIR=%~dp0

set M2=%MAVEN%bin
set PYTHON3=C:\Instrument\Apps\Python3\python.exe
set PYTHON_HOME=C:\Instrument\Apps\Python3

set PATH=%M2%;%PATH%

set TARGET_DIR=script_generator
set MSINAME=ibex_script_generator

call build_script_generator.bat "" %TARGET_DIR%
if %errorlevel% neq 0 exit /b %errorlevel%
set build_error_level=%errorlevel%

set PUBLISH=YES
if "%RELEASE%" == "YES" set PUBLISH=YES
if "%DEPLOY%" == "YES" set PUBLISH=YES
if "%PUBLISH%" == "NO" exit /b 0

REM disable for now
REM call build_msi.bat %BASEDIR%.. %TARGET_DIR% %MSINAME%
REM if %errorlevel% neq 0 exit /b %errorlevel%

@echo on

REM Copy zip to installs area
REM Delete older versions?
REM the password for isis\IBEXbuilder is contained in the BUILDERPW system environment variable on the build server
REM net use p: /d /yes
REM net use p: \\isis.cclrc.ac.uk\inst$


REM Don't group these. Bat expands whole if at once, not sequentially
if "%RELEASE%" == "YES" (
    set RELEASE_DIR=\\isis.cclrc.ac.uk\inst$\Kits$\CompGroup\ICP\Releases\script_generator_release\%GIT_BRANCH:~19%
    set RELEASE_VERSION=%GIT_BRANCH:~19%
) else (
    set RELEASE_VERSION=devel-%GIT_COMMIT:~0,7%
)
if "%RELEASE%" == "YES" set INSTALLBASEDIR=%RELEASE_DIR%\script_generator
if "%RELEASE%" == "YES" set INSTALLDIR=%INSTALLBASEDIR%

if not "%RELEASE%" == "YES" (
        set INSTALLBASEDIR=\\isis.cclrc.ac.uk\inst$\Kits$\CompGroup\ICP\script_generator
) 

if not "%RELEASE%" == "YES" set INSTALLDIR=%INSTALLBASEDIR%\BUILD-%BUILD_NUMBER%
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

robocopy %CD%\..\%TARGET_DIR% %INSTALLDIR%\script_generator /MIR /R:1 /NFL /NDL /NP /MT /NC /NS /LOG:NUL
if %errorlevel% geq 4 (
    if not "%INSTALLDIR%" == "" (
        @echo Removing invalid script generator directory %INSTALLDIR%\script_generator
        rd /q /s %INSTALLDIR%\script_generator
    )
    @echo Script generator copy failed
    exit /b 1
)

if not "%RELEASE%"=="YES" (
    if exist "%INSTALLLINKDIR%" (
        rmdir "%INSTALLLINKDIR%"
    )
    mklink /J "%INSTALLLINKDIR%" "%INSTALLDIR%"
)

REM copy MSI
if exist "%MSINAME%.msi" (
    xcopy /y /j %MSINAME%.msi %INSTALLDIR%
    if !errorlevel! neq 0 (
        @echo MSI copy failed
        exit /b !errorlevel!
    )
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
