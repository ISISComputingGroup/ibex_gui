robocopy "\\isis\inst$\Kits$\CompGroup\ICP\ibex_client_jre" "%~dp0\jdk" /E /PURGE /R:2 /MT /XF "install.log" /NFL /NDL /NC /NS /NP /LOG:NULset errcode=%ERRORLEVEL%
if %errcode% GEQ 4 (
    @echo *** Exit Code %errcode% ERROR see %INSTALLDIR%install.log ***
	@echo ************** Exit Code %errcode% ERROR **************** >>%INSTALLDIR%install.log
    if not "%1" == "NOLOG" start %INSTALLDIR%install.log
	exit /b %errcode%
)

call copy_in_maven.bat
if %errorlevel% neq 0 exit /b %errorlevel%
set "PATH=%PATH%;%~dp0maven\bin"

SET "JAVA_HOME=%~dp0jdk"

if "%PYTHON3%" == "" (
	set "PYTHON3=C:\Instrument\Apps\Python3\python.exe"
)
 
%PYTHON3% .\check_build.py ..\base\
if %errorlevel% neq 0 exit /b %errorlevel%

if "%BUILD_NUMBER%" == "" SET BUILD_NUMBER=SNAPSHOT

set mvnErr=
call mvn --settings=%~dp0..\mvn_user_settings.xml -f %~dp0..\base\uk.ac.stfc.isis.ibex.client.tycho.parent\pom.xml -DforceContextQualifier=%BUILD_NUMBER% clean verify || set mvnErr=1
if defined mvnErr exit /b 1

REM Copy built client into a sensible directory to run it
if "%~2" == "" (
	set built_client="%~dp0..\base\uk.ac.stfc.isis.ibex.e4.client.product\target\products\ibex.product\win32\win32\x86_64"
) else (
	set built_client="%~dp0..\%~2"
)
set sensible_build_dir="%~dp0..\built_client"
RMDIR /S /Q %sensible_build_dir%
robocopy "%built_client%" "%sensible_build_dir%" /MT /E /PURGE /R:2 /XF "install.log" /NFL /NDL /NP /NS /NC /LOG:NUL

REM Copy the JRE across 
robocopy "%LOCAL_JRE_LOCATION%" "%sensible_build_dir%\jre" /MT /MIR /R:1 /XF "install.log" /NFL /NDL /NP /NS /NC /LOG:NUL
if %errorlevel% geq 4 (
    @echo Failed to copy JRE across
    exit /b 1
)

REM Copy python into the client
%PYTHON3% get_python_write_dir.py %sensible_build_dir% > Output
set /p PythonWriteDir=<Output
call copy_python.bat %PythonWriteDir%
if %errorlevel% neq 0 exit /b %errorlevel%

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

REM Copy python into the client
python get_python_write_dir.py %sensible_build_dir% > Output
set /p PythonWriteDir=<Output
call copy_python.bat %PythonWriteDir%
if %errorlevel% neq 0 exit /b %errorlevel%

@echo Client built in %sensible_build_dir%
pause
