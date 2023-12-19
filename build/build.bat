setlocal
cd /d %~dp0

REM We bundle our own JRE with the client, this is where it is
set "JRELOCATION=\\isis.cclrc.ac.uk\inst$\Kits$\CompGroup\ICP\ibex_client_jdk-17.0.6+10"
set "LOCAL_JRE_LOCATION=%~dp0jdk"

set "TARGET_DIR=%3"
if "%TARGET_DIR%" == "" (
    set TARGET_DIR=built_client
)

robocopy "%JRELOCATION%" "%LOCAL_JRE_LOCATION%" /E /PURGE /R:2 /MT /XF "install.log" /NFL /NDL /NC /NS /NP /LOG:"local_copy_jre.log"

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

SET "JAVA_HOME=%~dp0jdk"

REM temporarily disable checks as workaround for JDK CEN header issue
set "JAVA_TOOL_OPTIONS=-Djdk.util.zip.disableZip64ExtraFieldValidation=true"

if "%PYTHON3%" == "" (
	set "PYTHON3=C:\Instrument\Apps\Python3\python.exe"
)

call %~dp0run_python_support_tests.bat
if %errorlevel% neq 0 exit /b %errorlevel%

%PYTHON3% .\check_build.py ..\base\
if %errorlevel% neq 0 exit /b %errorlevel%

if "%BUILD_NUMBER%" == "" SET BUILD_NUMBER=SNAPSHOT

set mvnErr=
call mvn -T 1C --settings=%~dp0..\mvn_user_settings.xml -f %~dp0..\base\uk.ac.stfc.isis.ibex.client.tycho.parent\pom.xml -DforceContextQualifier=%BUILD_NUMBER% -Dmaven.repo.local=%~dp0\.m2 clean verify || set mvnErr=1
if defined mvnErr exit /b 1

REM Copy built client into a sensible directory to run it
@echo Maven build completed, copying built client
if "%~2" == "" (
	set built_client="%~dp0..\base\uk.ac.stfc.isis.ibex.e4.client.product\target\products\ibex.product\win32\win32\x86_64"
) else (
	set built_client="%~dp0..\%~2"
)
set sensible_build_dir="%~dp0..\%TARGET_DIR%"
if exist "%sensible_build_dir%" RMDIR /S /Q %sensible_build_dir%
robocopy "%built_client%" "%sensible_build_dir%" /MT /E /PURGE /R:2 /XF "install.log" /NFL /NDL /NP /NS /NC /LOG:"local_copy_client.log"
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
@echo Copying JRE into client
robocopy "%LOCAL_JRE_LOCATION%" "%sensible_build_dir%\jre" /MT /MIR /R:1 /XF "install.log" /NFL /NDL /NP /NS /NC /LOG:"copy_client_jre.log"
if %errorlevel% geq 4 (
    @echo Failed to copy JRE across
    exit /b 1
)

REM Copy python into the client
@echo Copying python into client
%PYTHON3% get_python_write_dir.py %sensible_build_dir% > Output
set /p PythonWriteDir=<Output
call copy_python.bat %PythonWriteDir%
if %errorlevel% neq 0 exit /b %errorlevel%

@echo Client built in %sensible_build_dir%

exit /b 0
