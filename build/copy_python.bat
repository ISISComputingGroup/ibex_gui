set PYCOPYPATH=%~dp0..\base\uk.ac.stfc.isis.python
if not exist %PYCOPYPATH% (
    xcopy %PYTHON3DIR% %PYCOPYPATH% /isekryj
    if %errorlevel% neq 0 exit /b %errorlevel%
)
