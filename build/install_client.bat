setlocal enabledelayedexpansion
REM When run will install all the client stuff (gui, genie_python, epics_utils)
REM if %1 == NOINT then non-interactive mode and no pause when done

REM This is a redirect for backwards compatibility with install_client.bat references
@echo WARNING: install_client.bat (depreciated) called
@echo calling install_gui_with_external_python.bat

REM %~dp0 expands to directory where this file lives
set BASEDIR=%~dp0
call %BASEDIR%install_gui_and_external_python.bat %*
exit /b %errorlevel%
