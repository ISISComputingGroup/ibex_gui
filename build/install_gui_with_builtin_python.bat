setlocal enabledelayedexpansion
REM When run will install only the gui and epics_utils

REM if %1 == NOINT then non-interactive mode and no pause when done

REM %~dp0 expands to directory where this file lives
set BASEDIR=%~dp0

if not exist "%BASEDIR%COPY_COMPLETE.txt" (
    @echo ERROR Client copy in %BASEDIR% is not complete
	goto ERROR
)

REM Copy the Client files across
@echo %TIME% main ibex client install started
set APPSDIR=C:\Instrument\Apps
set CLIENTDIR=%APPSDIR%\Client_E4

REM Copy the pydev command history file to temp so it can be copied back in after deploy (otherwise it's overwritten) 
set GENIECMDLOGFILE=history.py
set GENIECMDLOGDIR=%CLIENTDIR%\workspace\.metadata\.plugins\org.python.pydev.shared_interactive_console\
if exist %GENIECMDLOGDIR%\%GENIECMDLOGFILE% (
	@echo Copying pydev history file before copying client
	robocopy %GENIECMDLOGDIR% %TEMP% %GENIECMDLOGFILE% /IS /NFL /NDL /NP /NC /NS /LOG:NUL
)

mkdir %CLIENTDIR%
robocopy "%BASEDIR%Client" "%CLIENTDIR%" /MIR /R:2 /MT /NFL /NDL /NP /NC /NS /LOG:NUL
set errcode=%errorlevel%
if %errcode% GEQ 4 (
    @echo ERROR %errcode% in robocopy copying ibex client
	goto ERROR
)

REM re-copy the pydev command history file back if it exists
if exist %TEMP%\%GENIECMDLOGFILE% (
	@echo Moving pydev history file to client
	robocopy %TEMP% %GENIECMDLOGDIR% %GENIECMDLOGFILE% /MOV /NFL /NDL /NP /NC /NS /LOG:NUL
)

REM Copy EPICS_UTILS across
@echo %TIME% epics utils install started
set UTILSDIR=%APPSDIR%\EPICS_UTILS
mkdir %UTILSDIR%
robocopy "\\isis.cclrc.ac.uk\inst$\Kits$\CompGroup\ICP\EPICS_UTILS\EPICS_UTILS" "%UTILSDIR%" /MIR /R:2 /MT /NFL /NDL /NP /NC /NS /LOG:NUL
set errcode=%errorlevel%
if %errcode% GEQ 4 (
    @echo ERROR %errcode% copying EPICS UTILS
	goto ERROR
)

@echo %TIME% client install finished OK
if /i not "%1" == "NOINT" (
    pause
)
exit /b 0

:ERROR
@echo %TIME% ERROR in client install
if /i not "%1" == "NOINT" (
    pause
)
exit /b 1
