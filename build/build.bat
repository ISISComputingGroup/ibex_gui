@echo off
 
SET "JAVA_HOME=C:\Program Files\Java\jdk-11.0.1"
 
call python .\check_build.py ..\base\
if %errorlevel% neq 0 exit /b %errorlevel%

if "%BUILD_NUMBER%" == "" SET BUILD_NUMBER=SNAPSHOT

call mvn -e --fail-at-end --settings=%~dp0..\mvn_user_settings.xml -f %~dp0..\base\uk.ac.stfc.isis.ibex.client.tycho.parent\pom.xml clean verify 
if %errorlevel% neq 0 exit /b %errorlevel%

@echo Client built in %~dp0..\base\uk.ac.stfc.isis.ibex.e4.client.product\target\products\ibex.product\win32\win32\x86_64
pause
