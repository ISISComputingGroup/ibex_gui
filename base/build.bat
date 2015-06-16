@echo off

call mvn --settings=%~dp0..\mvn_user_settings.xml -f uk.ac.stfc.isis.ibex.client.tycho.parent\pom.xml clean verify
if %errorlevel% neq 0 exit /b %errorlevel%

@echo Client built in %~dp0\uk.ac.stfc.isis.ibex.client.product\target\products\ibex.product\win32\win32\x86_64 
