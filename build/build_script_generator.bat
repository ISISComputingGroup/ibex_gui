set "JRELOCATION=\\isis\inst$\Kits$\CompGroup\ICP\ibex_client_jre"

set "TARGET_DIR=%2"
if "%TARGET_DIR%" == "" (
    set TARGET_DIR=built_script_gen
)

robocopy "%JRELOCATION%" "%~dp0\jdk" /E /PURGE /R:2 /MT /XF "install.log" /NFL /NDL /NP /NC /NS /LOG:NUL
set errcode=%ERRORLEVEL%
if %errcode% GEQ 4 (
    @echo *** Exit Code %errcode% ERROR see %INSTALLDIR%install.log ***
	@echo ************** Exit Code %errcode% ERROR **************** >>%INSTALLDIR%install.log
    if not "%1" == "NOLOG" start %INSTALLDIR%install.log
	exit /b %errcode%
)

call copy_in_maven.bat
if %errorlevel% neq 0 exit /b %errorlevel%
set "PATH=%PATH%;%~dp0maven\bin"

SET "JAVA_HOME=%~dp0\jdk"

if "%PYTHON3%" == "" (
	set "PYTHON3=C:\Instrument\Apps\Python3\python.exe"
)
 
%PYTHON3% .\check_build.py ..\base\
if %errorlevel% neq 0 exit /b %errorlevel%

if "%BUILD_NUMBER%" == "" (
    set BUILD_NUMBER=SNAPSHOT
)

set mvnErr=
call mvn --settings=%~dp0..\mvn_user_settings.xml -f %~dp0..\base\uk.ac.stfc.isis.scriptgenerator.tycho.parent\pom.xml -DforceContextQualifier=%BUILD_NUMBER% clean verify || set mvnErr=1
if defined mvnErr exit /b 1

REM Copy built client into a sensible clean directory to run it
set built_client="%~dp0..\base\uk.ac.stfc.isis.scriptgenerator.client.product\target\products\scriptgenerator.product\win32\win32\x86_64"
set sensible_build_dir="%~dp0..\%TARGET_DIR%"
RMDIR /S /Q %sensible_build_dir%
robocopy "%built_client%" "%sensible_build_dir%" /E /PURGE /R:2 /XF "install.log" /MT /NFL /NDL /NP /NS /NC /LOG:NUL
set errcode=%ERRORLEVEL%
if %errcode% GEQ 4 (
	@echo robocopy error
    @echo *** Exit Code %errcode% ERROR see %INSTALLDIR%install.log ***
	@echo ************** Exit Code %errcode% ERROR **************** >>%INSTALLDIR%install.log
    if not "%1" == "NOLOG" start %INSTALLDIR%install.log
	exit /b %errcode%
) else (
	set ERRORLEVEL=0
)

REM Copy the JRE across 
robocopy "%JRELOCATION%" "%sensible_build_dir%\jre" /MT /MIR /R:1 /XF "install.log" /NFL /NDL /NP /NS /NC /LOG:NUL
if %errorlevel% geq 4 (
    @echo Failed to copy JRE across
    exit /b 1
)

REM Copy python into the client
%PYTHON3% get_python_write_dir.py %sensible_build_dir% > Output
set /p PythonWriteDir=<Output
call copy_python.bat %PythonWriteDir%
if %errorlevel% neq 0 exit /b %errorlevel%

@echo Client built in %sensible_build_dir%

pause
