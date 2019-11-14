robocopy "\\isis\inst$\Kits$\CompGroup\ICP\ibex_client_jre" "%~dp0\jdk" /E /PURGE /R:2 /MT /XF "install.log" /NFL /NDL /NP

set errcode=%ERRORLEVEL%
if %errcode% GEQ 4 (
    @echo *** Exit Code %errcode% ERROR see %INSTALLDIR%install.log ***
	@echo ************** Exit Code %errcode% ERROR **************** >>%INSTALLDIR%install.log
    if not "%1" == "NOLOG" start %INSTALLDIR%install.log
	exit /b %errcode%
)

SET "JAVA_HOME=%~dp0\jdk"
 
call python .\check_build.py ..\base\
if %errorlevel% neq 0 exit /b %errorlevel%

REM This batch script is called with copy_python.bat for build machines and dev_python.bat for dev machines
call %1
if %errorlevel% neq 0 exit /b %errorlevel%

if "%BUILD_NUMBER%" == "" SET BUILD_NUMBER=SNAPSHOT

call mvn --settings=%~dp0..\mvn_user_settings.xml -f %~dp0..\base\uk.ac.stfc.isis.ibex.client.tycho.parent\pom.xml -DforceContextQualifier=%BUILD_NUMBER% clean verify
if %errorlevel% neq 0 exit /b %errorlevel%

@echo Client built in %~dp0..\base\uk.ac.stfc.isis.ibex.e4.client.product\target\products\ibex.product\win32\win32\x86_64
pause
