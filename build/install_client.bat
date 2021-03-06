setlocal enabledelayedexpansion
REM When run will install all the client stuff (gui, genie_python, epics_utils)

REM %~dp0 expands to directory where this file lives
set BASEDIR=%~dp0

if not exist "%BASEDIR%COPY_COMPLETE.txt" (
    @echo ERROR Client copy in %BASEDIR% is not complete
	goto FINISH
)

REM Copy the Client files across
@echo %TIME% main ibex client install started
set APPSDIR=C:\Instrument\Apps
set CLIENTDIR=%APPSDIR%\Client_E4
mkdir %CLIENTDIR%
robocopy "%BASEDIR%Client" "%CLIENTDIR%" /E /R:2 /MT /NFL /NDL /NP /NC /NS /LOG:NUL
set errcode=%errorlevel%
if %errcode% GEQ 4 (
    @echo ERROR %errcode% in robocopy copying ibex client
	goto FINISH
)

REM Copy EPICS_UTILS across
@echo %TIME% epics utils install started
set UTILSDIR=%APPSDIR%\EPICS_UTILS
mkdir %UTILSDIR%
robocopy "\\isis.cclrc.ac.uk\inst$\Kits$\CompGroup\ICP\EPICS_UTILS\EPICS_UTILS" "%UTILSDIR%" /E /R:2 /MT /NFL /NDL /NP /NC /NS /LOG:NUL
set errcode=%errorlevel%
if %errcode% GEQ 4 (
    @echo ERROR copying EPICS UTILS
	goto FINISH
)

REM genie_python already has its own script
REM the hierarchy is slightly different for release and development builds
if exist "%BASEDIR%..\genie_python_3" (
    set GENIE_PYTHON_TOP=%BASEDIR%..\genie_python_3
    call !GENIE_PYTHON_TOP!\genie_python_install.bat
) else (
    set GENIE_PYTHON_TOP=%BASEDIR%..\..\genie_python_3
    for /F %%I in ( !GENIE_PYTHON_TOP!\LATEST_BUILD.txt ) DO SET LATEST_BUILD=%%I
    call !GENIE_PYTHON_TOP!\BUILD-!LATEST_BUILD!\genie_python_install.bat
)
if %errorlevel% neq 0 (
    @echo ERROR copying GENIE PYTHON
    goto FINISH
)

@echo %TIME% client install finished

:FINISH
pause
