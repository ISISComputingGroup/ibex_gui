setlocal enabledelayedexpansion
REM When run will install all the script generator stuff (gui, genie_python, epics_utils)

REM %~dp0 expands to directory where this file lives
set BASEDIR=%~dp0

if not exist "%BASEDIR%COPY_COMPLETE.txt" (
    @echo ERROR script generator copy in %BASEDIR% is not complete
	goto FINISH
)
REM Copy the script generator files across
set APPSDIR=C:\Instrument\Apps
set SCRIPTGENDIR=%APPSDIR%\script_generator
mkdir %SCRIPTGENDIR%
xcopy /q /s /e /h %BASEDIR%script_generator %SCRIPTGENDIR%
if %errorlevel% neq 0 (
    @echo ERROR copying SCRIPT GENERATOR
	goto FINISH
)

REM Copy EPICS_UTILS across
set UTILSDIR=%APPSDIR%\EPICS_UTILS
mkdir %UTILSDIR%
xcopy /q /s /e /h \\isis.cclrc.ac.uk\inst$\Kits$\CompGroup\ICP\EPICS_UTILS\EPICS_UTILS %UTILSDIR%
if %errorlevel% neq 0 (
    @echo ERROR copying EPICS UTILS
	goto FINISH
)


REM genie_python already has its own script
REM the hierarchy is slightly different for release and development builds
if exist "%BASEDIR%..\genie_python" (
    set GENIE_PYTHON_TOP=%BASEDIR%..\genie_python
    call !GENIE_PYTHON_TOP!\genie_python_install.bat
) else (
    set GENIE_PYTHON_TOP=%BASEDIR%..\..\genie_python
    for /F %%I in ( !GENIE_PYTHON_TOP!\LATEST_BUILD.txt ) DO SET LATEST_BUILD=%%I
    call !GENIE_PYTHON_TOP!\BUILD-!LATEST_BUILD!\genie_python_install.bat
)
if %errorlevel% neq 0 (
    @echo ERROR copying GENIE PYTHON
    goto FINISH
)

:FINISH
pause
