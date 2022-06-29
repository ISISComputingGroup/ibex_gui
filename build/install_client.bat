setlocal
REM This is a redirect for backwards compatibility with install_client.bat references
@echo WARNING: install_client.bat (depreciated) called
@echo calling install_gui_with_builtin_python.bat
@echo and then installing genie python separately

REM %~dp0 expands to directory where this file lives
call %~dp0install_gui_with_builtin_python.bat %*
