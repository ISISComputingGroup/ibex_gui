set PYCOPYPATH=%~dp0..\base\uk.ac.stfc.isis.ibex.scriptgenerator\python
if not exist %PYCOPYPATH% (
    mklink /D %PYCOPYPATH% %PYTHON3DIR%
    if %errorlevel% neq 0 exit /b %errorlevel%
)