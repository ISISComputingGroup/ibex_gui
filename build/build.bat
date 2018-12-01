@echo off
 
call python .\check_build.py ..\base\
if %errorlevel% neq 0 exit /b %errorlevel%

if "%BUILD_NUMBER%" == "" SET BUILD_NUMBER=0.0.0-SNAPSHOT

call mvn -e -T16 --fail-at-end --settings=%~dp0..\mvn_user_settings.xml -f %~dp0..\base\uk.ac.stfc.isis.ibex.client.tycho.parent\pom.xml -DforceContextQualifier=%BUILD_NUMBER% clean verify 
if %errorlevel% neq 0 exit /b %errorlevel%

@echo Client built in %~dp0..\base\uk.ac.stfc.isis.ibex.e4.client.product\target\products\ibex.product\win32\win32\x86_64
pause
