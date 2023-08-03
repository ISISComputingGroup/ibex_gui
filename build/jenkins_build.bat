REM %~dp0 expands to directory where this file lives
setlocal enabledelayedexpansion

set BASEDIR=%~dp0

set M2=%MAVEN%bin
set PYTHON3=C:\Instrument\Apps\Python3\python.exe
set PYTHON_HOME=C:\Instrument\Apps\Python3

set PATH=%M2%;%JAVA_HOME%;%PATH%

if "%IS_E4%" == "YES" (
    set BUILT_CLIENT_DIR=base\uk.ac.stfc.isis.ibex.e4.client.product\target\products\ibex.product\win32\win32\x86_64
) else (
    set BUILT_CLIENT_DIR=base\uk.ac.stfc.isis.ibex.client.product\target\products\ibex.product\win32\win32\x86_64
)

set TARGET_DIR=Client_E4
set MSINAME=ibex_client

call build.bat "LOG" %BUILT_CLIENT_DIR% %TARGET_DIR%
if %errorlevel% neq 0 exit /b %errorlevel%

call build_msi.bat %BASEDIR%.. %TARGET_DIR% %MSINAME%
if %errorlevel% neq 0 exit /b %errorlevel%

REM set EXIT=YES will change error code to 1 if not set previously so store the current
set build_error_level=%errorlevel%

@echo on

REM Copy zip to installs area
REM Delete older versions?
REM the password for isis\IBEXbuilder is contained in the BUILDERPW system environment variable on the build server
REM net use p: /d /yes
REM net use p: \\isis.cclrc.ac.uk\inst$

REM Don't group these. Bat expands whole if at once, not sequentially
if "%RELEASE%" == "YES" (
    set RELEASE_DIR=\\isis.cclrc.ac.uk\inst$\Kits$\CompGroup\ICP\Releases\%GIT_BRANCH:~8%
    set RELEASE_VERSION=%GIT_BRANCH:~8%    
) else (
    set RELEASE_VERSION=devel-%GIT_COMMIT:~0,7%
)
if "%RELEASE%" == "YES" set INSTALLBASEDIR=%RELEASE_DIR%\Client
if "%RELEASE%" == "YES" set INSTALLDIR=%INSTALLBASEDIR%

if not "%RELEASE%" == "YES" (
    if "%IS_E4%" == "YES" (
        set INSTALLBASEDIR=\\isis.cclrc.ac.uk\inst$\Kits$\CompGroup\ICP\Client_E4
    ) else (
        set INSTALLBASEDIR=\\isis.cclrc.ac.uk\inst$\Kits$\CompGroup\ICP\Client
    )
    if not "%DEPLOY%" == "YES" (
        set "INSTALLBASEDIR=!INSTALLBASEDIR!\branches\%GIT_BRANCH%"
    )
) 

if not "%RELEASE%" == "YES" (
    set INSTALLDIR=%INSTALLBASEDIR%\BUILD%BUILD_NUMBER%
)

if "%RELEASE%" == "YES" (
    if not exist "%RELEASE_DIR%" (
        mkdir %RELEASE_DIR%
    )
    RMDIR /S /Q %INSTALLDIR%
    @echo Creating client directory %INSTALLDIR%
    if not exist "%INSTALLDIR%" (
        mkdir %INSTALLDIR%
    )
) else (
    if exist "%INSTALLDIR%\Client" (
        @echo Creating client directory %INSTALLDIR%\Client
        mkdir %INSTALLDIR%\Client
    )
)

robocopy %CD%\..\%TARGET_DIR% %INSTALLDIR%\Client /MT /MIR /R:1 /NFL /NDL /NP /NS /NC /LOG:"copy_client.log"
if %errorlevel% geq 4 (
    if not "%INSTALLDIR%" == "" (
        @echo Removing invalid client directory %INSTALLDIR%\Client
        rd /q /s %INSTALLDIR%\Client
    )
    @echo Client copy failed
    exit /b 1
)

REM copy MSI
copy /Y %MSINAME%.msi %INSTALLDIR%
if %errorlevel% neq 0 (
    @echo MSI copy failed
    exit /b %errorlevel%
)

REM Copy the install script across
cd /d %BASEDIR%
robocopy "." "%INSTALLDIR%" install_client.bat install_gui_with_builtin_python.bat install_gui_and_external_python.bat README_INSTALL.txt /R:1
if %errorlevel% geq 4 (
    @echo Install client batch file copy failed  %errorlevel%
    exit /b 1
)

@echo %BUILD_NUMBER%> %INSTALLDIR%\Client\BUILD_NUMBER.txt
@echo %RELEASE_VERSION%> %INSTALLDIR%\Client\VERSION.txt

@echo Copy complete>%INSTALLDIR%\COPY_COMPLETE.txt

if not "%RELEASE%" == "YES" (
    @echo %BUILD_NUMBER%>%INSTALLDIR%\..\LATEST_BUILD.txt 
)
exit /b 0

