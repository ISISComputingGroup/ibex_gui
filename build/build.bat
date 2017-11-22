@echo off

REM call python .\check_build.py ..\base\
if %errorlevel% neq 0 exit /b %errorlevel%

call mvn --settings=%~dp0..\mvn_user_settings.xml -f %~dp0..\base\uk.ac.stfc.isis.ibex.client.tycho.parent\pom.xml -DforceContextQualifier=%BUILD_NUMBER% clean verify
if %errorlevel% neq 0 exit /b %errorlevel%

@echo Client built in %~dp0..\base\uk.ac.stfc.isis.ibex.client.product\target\products\ibex.product\win32\win32\x86_64
pause
