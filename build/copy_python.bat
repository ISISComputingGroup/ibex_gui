set PYTHON3DIR=c:\Instrument\Apps\Python3
if not exist "%PYTHON3DIR%" (
    git clone https://github.com/ISISComputingGroup/genie_python.git "%PYTHON3DIR%"
    %PYTHON3DIR%\package_builder\dev_build_python.bat 3
    if %errorlevel% neq 0 exit /b %errorlevel%
)

set PYCOPYPATH=%~dp0..\base\uk.ac.stfc.isis.ibex.scriptgenerator\python
if not exist %PYCOPYPATH% (
    mklink /D %PYCOPYPATH% %PYTHON3DIR%
    if %errorlevel% neq 0 exit /b %errorlevel%
)