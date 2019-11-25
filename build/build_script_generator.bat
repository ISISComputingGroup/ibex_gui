robocopy "\\isis\inst$\Kits$\CompGroup\ICP\ibex_client_jre" "%~dp0\jdk" /E /PURGE /R:2 /MT /XF "install.log" /NFL /NDL /NP

set errcode=%ERRORLEVEL%
if %errcode% GEQ 4 (
    @echo *** Exit Code %errcode% ERROR see %INSTALLDIR%install.log ***
	@echo ************** Exit Code %errcode% ERROR **************** >>%INSTALLDIR%install.log
    if not "%1" == "NOLOG" start %INSTALLDIR%install.log
	exit /b %errcode%
)

SET "JAVA_HOME=%~dp0\jdk"
SET CLIENT = %~dp0..\base\uk.ac.stfc.isis.ibex.e4.client.product\target\products\ibex.product\win32\win32\x86_64

REM If COPY_PYTHON is an arg copy python into the client
if "%1" == "COPY_PYTHON" ( call %1 %CLIENT% ) 
if "%2" == "COPY_PYTHON" ( call %2 %CLIENT% ) 
if %errorlevel% neq 0 exit /b %errorlevel%
 
call python .\check_build.py ..\base\
if %errorlevel% neq 0 exit /b %errorlevel%

if "%BUILD_NUMBER%" == "" (
    set BUILD_NUMBER=SNAPSHOT
)

call mvn --settings=%~dp0..\mvn_user_settings.xml -f %~dp0..\base\uk.ac.stfc.isis.scriptgenerator.tycho.parent\pom.xml -DforceContextQualifier=%BUILD_NUMBER% clean verify
if %errorlevel% neq 0 exit /b %errorlevel%

@echo Client built in %CLIENT%
pause
