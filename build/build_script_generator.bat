robocopy "\\isis\inst$\Kits$\CompGroup\ICP\ibex_client_jre" "%~dp0\jdk" /E /PURGE /R:2 /MT /XF "install.log" /NFL /NDL /NP /NC /NS /LOG:NUL
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

SET "JAVA_HOME=%~dp0\jdk"

if "%PYTHON3%" == "" (
	set "PYTHON3=C:\Instrument\Apps\Python3\python.exe"
)
 
%PYTHON3% .\check_build.py ..\base\
if %errorlevel% neq 0 exit /b %errorlevel%


if "%BUILD_NUMBER%" == "" (
    set BUILD_NUMBER=SNAPSHOT
)

REM Create a bundle of the latest script defninitions repo, then delete temporary files
pushd %~dp0
set definitions_temp_directory=%~dp0..\base\uk.ac.stfc.isis.ibex.scriptgenerator\python_support\ScriptDefinitions

git clone https://github.com/ISISComputingGroup/ScriptGeneratorConfigs.git %definitions_temp_directory%
cd %definitions_temp_directory%

git bundle create ScriptDefinitions_repo.bundle --all
if %errorlevel% neq 0 exit /b %errorlevel%
cd ..
robocopy "ScriptDefinitions" "." "ScriptDefinitions_repo.bundle"
popd
RMDIR /S /Q %definitions_temp_directory%

set mvnErr=
call mvn --settings=%~dp0..\mvn_user_settings.xml -f %~dp0..\base\uk.ac.stfc.isis.scriptgenerator.tycho.parent\pom.xml -DforceContextQualifier=%BUILD_NUMBER% clean verify || set mvnErr=1
if defined mvnErr exit /b 1

REM Copy built client into a sensible clean directory to run it
set built_client="%~dp0..\base\uk.ac.stfc.isis.scriptgenerator.client.product\target\products\scriptgenerator.product\win32\win32\x86_64"
set sensible_build_dir="%~dp0..\built_script_gen"
RMDIR /S /Q %sensible_build_dir%
robocopy "%built_client%" "%sensible_build_dir%" /E /PURGE /R:2 /XF "install.log" /MT /NFL /NDL /NP /NS /NC /LOG:NUL
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
%PYTHON3% get_python_write_dir.py %sensible_build_dir% > Output
set /p PythonWriteDir=<Output
call copy_python.bat %PythonWriteDir%
if %errorlevel% neq 0 exit /b %errorlevel%

@echo Script generator built in %sensible_build_dir%

pause
