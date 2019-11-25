REM Get the latest genie python build with LATEST_PYTHON and LATEST_PYTHON_DIR as the vars

pushd \\isis.cclrc.ac.uk\inst$\Kits$\CompGroup\ICP

set KITS_ICP_PATH=%cd%

if exist "%KITS_ICP_PATH%\genie_python_3\LATEST_BUILD.txt" (
	for /f %%i in ( %KITS_ICP_PATH%\genie_python_3\LATEST_BUILD.txt ) do (
	    set LATEST_PYTHON_DIR=%KITS_ICP_PATH%\genie_python_3\BUILD-%%i\Python
	)
) else (
	@echo Could not access LATEST_BUILD.txt
    popd
	exit /b 1
)

set PYCOPYPATH=%~dp0..\base\uk.ac.stfc.isis.python\resources\Python3
rmdir %PYCOPYPATH% /s /q
mkdir %PYCOPYPATH%
xcopy %LATEST_PYTHON_DIR% %PYCOPYPATH% /i /e /k /y /j

popd

if not exist %PYCOPYPATH%\python.exe (
    exit /b 1
)
if %errorlevel% neq 0 exit /b %errorlevel%
