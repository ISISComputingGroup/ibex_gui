setlocal enabledelayedexpansion
REM When run will install all the script generator stuff (gui, genie_python, epics_utils)

REM %~dp0 expands to directory where this file lives
set "BASEDIR=%~dp0"

if not exist "%BASEDIR%COPY_COMPLETE.txt" (
    @echo ERROR script generator copy in %BASEDIR% is not complete
	goto FINISH
)
REM Copy the script generator files across
set APPSDIR=C:\Instrument\Apps
set SCRIPTGENDIR=%APPSDIR%\script_generator
mkdir %SCRIPTGENDIR%
robocopy "%BASEDIR%script_generator" "%SCRIPTGENDIR%" /MIR /R:2 /MT /NFL /NDL /NP /NC /NS /LOG:NUL
if %errorlevel% geq 4 (
    @echo ERROR copying SCRIPT GENERATOR
)

:FINISH
pause
