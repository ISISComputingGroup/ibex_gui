robocopy "\\isis\inst$\Kits$\CompGroup\ICP\JDK_11" "%~dp0\jdk" /E /PURGE /R:2 /MT /XF "install.log" /NFL /NDL /NP

set errcode=%ERRORLEVEL%
if %errcode% GEQ 4 (
    @echo *** Exit Code %errcode% ERROR see %INSTALLDIR%install.log ***
	@echo ************** Exit Code %errcode% ERROR **************** >>%INSTALLDIR%install.log
    if not "%1" == "NOLOG" start %INSTALLDIR%install.log
	exit /b %errcode%
)

for /D %%I in ( %~dp0jdk\* ) do SET "JAVA_HOME=%%I"
 
call python .\check_build.py ..\base\
if %errorlevel% neq 0 exit /b %errorlevel%

if "%BUILD_NUMBER%" == "" (
    set BUILD_NUMBER=SNAPSHOT
)

call mvn --fail-at-end --settings=%~dp0..\mvn_user_settings.xml -f %~dp0..\base\uk.ac.stfc.isis.scriptgenerator.tycho.parent\pom.xml -DforceContextQualifier=%BUILD_NUMBER% clean verify
if %errorlevel% neq 0 exit /b %errorlevel%

@echo Client built in %~dp0..\base\uk.ac.stfc.isis.scriptgenerator.client.product\target\products\scriptgenerator.product\win32\win32\x86_64
pause
