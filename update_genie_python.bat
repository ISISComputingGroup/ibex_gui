@echo off
setlocal EnableDelayedExpansion
REM %1 is an alternative directory to install to
REM
REM It only takes two or three minutes to do a full install, so for simplicity we do that.
REM it is installed using xcopy /d, so if it hasn't changed then it doesn't do anything.


set "PYTHON_KITS_PATH=\\isis.cclrc.ac.uk\inst$\Kits$\CompGroup\ICP\genie_python_3"
set "PYTHON_INSTALL_DIR=C:\Instrument\Apps\Python3"

if not "%1" == "" (
    set "PYTHON_INSTALL_DIR=%1"
)
 
@echo %TIME% Updating genie_python

if exist "%PYTHON_KITS_PATH%\LATEST_BUILD.txt" (
	for /f %%i in ( %PYTHON_KITS_PATH%\LATEST_BUILD.txt ) do (
		@echo NEW_BUILD: %%i
		call %PYTHON_KITS_PATH%\BUILD-%%i\genie_python_install.bat %PYTHON_INSTALL_DIR%
        if !errorlevel! neq 0 goto ERROR
	)
) else (
	@echo Could not access LATEST_BUILD.txt
	goto ERROR
)

@echo %TIME% update_genie_python OK
exit /b 0

:ERROR
@echo %TIME% update_genie_python failed
exit /b 1
