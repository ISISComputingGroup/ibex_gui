setlocal enabledelayedexpansion
REM This is a redirect for backwards compatibility with install_client.bat references
@echo WARNING: install_client.bat (depreciated) called, calling install_gui_and_python.bat instead.

REM %~dp0 expands to directory where this file lives
set BASEDIR=%~dp0

call %BASEDIR%install_gui_and_python.bat
