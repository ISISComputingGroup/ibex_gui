REM Get the latest genie python build with LATEST_PYTHON and LATEST_PYTHON_DIR as the vars
setlocal

set "KITS_ICP_PATH=\\isis.cclrc.ac.uk\inst$\Kits$\CompGroup\ICP"
set "PYCOPYPATH=%1"

REM Get latest build
if exist "%KITS_ICP_PATH%\genie_python_3\LATEST_BUILD.txt" (
	for /f %%i in ( %KITS_ICP_PATH%\genie_python_3\LATEST_BUILD.txt ) do (
	    set LATEST_PYTHON_DIR=%KITS_ICP_PATH%\genie_python_3\BUILD-%%i\Python
		set LATEST_BUILD=%%i
	)
) else (
	@echo Could not access LATEST_BUILD.txt
	exit /b 1
)

robocopy "%LATEST_PYTHON_DIR%" "%PYCOPYPATH%" /e /purge /r:2 /XF "install.log" /MT /NFL /NDL /NP /NS /NC /LOG:NUL
set errcode=%ERRORLEVEL%
if %errcode% GEQ 4 (
	@echo robocopy error
    @echo *** Exit Code %errcode% ERROR see %INSTALLDIR%install.log ***
	@echo ************** Exit Code %errcode% ERROR **************** >>%INSTALLDIR%install.log
	exit /b %errcode%
) else (
	set ERRORLEVEL=0
)

if not exist %PYCOPYPATH%\python.exe (
	@echo Python not copied correctly>>%INSTALLDIR%install.log
	@echo Python not copied correctly
	exit /b 1
)

exit /b 0
