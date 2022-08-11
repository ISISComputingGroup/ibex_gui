setlocal enabledelayedexpansion
REM When run will install all the client stuff (gui, genie_python, epics_utils)
REM if %1 == NOINT then non-interactive mode and no pause when done

REM %~dp0 expands to directory where this file lives
set BASEDIR=%~dp0
call %BASEDIR%install_gui_with_builtin_python.bat NOINT
if !errorlevel! neq 0 (
    @echo ERROR !errorlevel! installing GUI
    goto ERROR
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
if !errorlevel! neq 0 (
    @echo ERROR !errorlevel! copying GENIE PYTHON
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
