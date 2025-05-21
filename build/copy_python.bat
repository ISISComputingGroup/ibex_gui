REM Get the latest genie python build with LATEST_PYTHON and LATEST_PYTHON_DIR as the vars
setlocal

set "KITS_ICP_PATH=\\isis.cclrc.ac.uk\inst$\Kits$\CompGroup\ICP"
set "PYCOPYPATH=%1"

REM Get latest build
if exist "%KITS_ICP_PATH%\genie_python_3\LATEST_BUILD.txt" (
	for /f %%i in ( %KITS_ICP_PATH%\genie_python_3\LATEST_BUILD.txt ) do (
        set PYTHON_KITS_PATH=%KITS_ICP_PATH%\genie_python_3\BUILD-%%i
	)
) else (
	@echo Could not access LATEST_BUILD.txt
	exit /b 1
)

call %PYTHON_KITS_PATH%\genie_python_install.bat %PYCOPYPATH%
if %errorlevel% neq 0 (
    @echo *** Exit Code %errorlevel% genie_python_install ***
    @echo *** Exit Code %errorlevel% genie_python_install ***>>%INSTALLDIR%install.log
	exit /b %errorlevel%
)

if not exist %PYCOPYPATH%\python.exe (
	@echo Python not copied correctly>>%INSTALLDIR%install.log
	@echo Python not copied correctly
	exit /b 1
)

exit /b 0
