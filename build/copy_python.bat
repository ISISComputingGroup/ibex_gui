REM Get the latest genie python build with LATEST_PYTHON and LATEST_PYTHON_DIR as the vars

pushd \\isis.cclrc.ac.uk\inst$\Kits$\CompGroup\ICP

set KITS_ICP_PATH=%cd%
set PYCOPYPATH=%1

REM Get latest build
if exist "%KITS_ICP_PATH%\genie_python_3\LATEST_BUILD.txt" (
	for /f %%i in ( %KITS_ICP_PATH%\genie_python_3\LATEST_BUILD.txt ) do (
	    set LATEST_PYTHON_DIR=%KITS_ICP_PATH%\genie_python_3\BUILD-%%i\Python
		set LATEST_BUILD=%%i
	)
) else (
	@echo Could not access LATEST_BUILD.txt
    popd
	exit /b 1
)

REM Do not copy if the current build is the same as the latest build
set current_build_file=%~dp0current_python_build.txt
if exist "%current_build_file%" (
	for /f %%i in ("%current_build_file%") do set CURRENT_BUILD="%%i"
	if !CURRENT_BUILD! neq %LATEST_BUILD% (
		call :copy_python
	)
) else (
	call :copy_python
)

GOTO :clean_up

:copy_python
    robocopy %LATEST_PYTHON_DIR% %PYCOPYPATH% /e /purge /r:2 /mt /XF "install.log" /NFL /NDL /NP
	@echo %LATEST_BUILD%> %current_build_file%
exit /b

:clean_up
	popd

	if not exist %PYCOPYPATH%\python.exe (
		exit /b 1
	)
	if %errorlevel% neq 0 exit /b %errorlevel%
exit /b
