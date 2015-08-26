REM This is run by the self-extracting zip
REM It will install all the client stuff (gui, genie_python, epics_utils)

REM %~dp0 expands to directory where this file lives
set BASEDIR=%~dp0

REM Copy the Client files across
set APPSDIR=C:\Instrument\Apps
set CLIENTDIR=%APPSDIR%\Client
mkdir %CLIENTDIR%
xcopy /s /e /h %BASEDIR%\Client %CLIENTDIR%

REM Copy EPICS_UTILS across
set UTILSDIR=%APPSDIR%\EPICS_UTILS
mkdir %UTILSDIR%
xcopy /s /e /h %BASEDIR%\EPICS_UTILS %UTILSDIR%

REM genie_python already has its own script
call %BASEDIR%\genie_python\genie_python_install.bat

echo Install finished - if Windows pops up a window asking whether it installed correctly just click 'cancel'
pause