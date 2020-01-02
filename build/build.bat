robocopy "\\isis\inst$\Kits$\CompGroup\ICP\ibex_client_jre" "%~dp0\jdk" /E /PURGE /R:2 /MT /XF "install.log" /NFL /NDL /NP

set errcode=%ERRORLEVEL%
if %errcode% GEQ 4 (
    @echo *** Exit Code %errcode% ERROR see %INSTALLDIR%install.log ***
	@echo ************** Exit Code %errcode% ERROR **************** >>%INSTALLDIR%install.log
    if not "%1" == "NOLOG" start %INSTALLDIR%install.log
	exit /b %errcode%
)

SET "JAVA_HOME=%~dp0\jdk"

REM Copy python into the client
call copy_python.bat %~dp0..\base\uk.ac.stfc.isis.ibex.preferences\resources\Python3
if %errorlevel% neq 0 exit /b %errorlevel%
 
python .\check_build.py ..\base\
if %errorlevel% neq 0 exit /b %errorlevel%

if "%BUILD_NUMBER%" == "" SET BUILD_NUMBER=SNAPSHOT

call mvn --settings=%~dp0..\mvn_user_settings.xml -f %~dp0..\base\uk.ac.stfc.isis.ibex.client.tycho.parent\pom.xml -DforceContextQualifier=%BUILD_NUMBER% clean verify || set mvnErr=1
if defined mvnErr exit /b 1

REM Copy built client into a sensible directory to run it
set built_client="%~dp0..\base\uk.ac.stfc.isis.ibex.e4.client.product\target\products\ibex.product\win32\win32\x86_64"
set sensible_build_dir="%~dp0..\built_client"
robocopy %built_client% %sensible_build_dir% /e /purge /r:2 /mt /XF "install.log" /NFL /NDL /NP

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

@echo Client built in %sensible_build_dir%
pause
