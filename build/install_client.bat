REM When run will install all the client stuff (gui, genie_python, epics_utils)

REM %~dp0 expands to directory where this file lives
set BASEDIR=%~dp0

if not exist "%BASEDIR%COPY_COMPLETE.txt" (
    @echo ERROR Client copy in %BASEDIR% is not complete
	goto FINISH
)
REM Copy the Client files across
set APPSDIR=C:\Instrument\Apps
set CLIENTDIR=%APPSDIR%\Client
mkdir %CLIENTDIR%
xcopy /q /s /e /h %BASEDIR%\Client %CLIENTDIR%
if %errorlevel% neq 0 (
    @echo ERROR copying IBEX CLIENT
	goto FINISH
)

REM Copy EPICS_UTILS across
set UTILSDIR=%APPSDIR%\EPICS_UTILS
mkdir %UTILSDIR%
xcopy /q /s /e /h \\isis.cclrc.ac.uk\inst$\Kits$\CompGroup\ICP\Client\EPICS_UTILS %UTILSDIR%
if %errorlevel% neq 0 (
    @echo ERROR copying EPICS UTILS
	goto FINISH
)

REM genie_python already has its own script
call %BASEDIR%\..\genie_python\genie_python_install.bat
if %errorlevel% neq 0 (
    @echo ERROR copying GENIE PYTHON
	goto FINISH
)

:FINISH
pause
