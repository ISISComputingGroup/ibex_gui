REM %~dp0 expands to directory where this file lives
setlocal

set BASEDIR=%~dp0

set M2=%MAVEN%bin
set PYTHON=C:\Instrument\Apps\Python
set PYTHON_HOME=C:\Instrument\Apps\Python

REM We bundle our own JRE with the client, this is where it is
set JRELOCATION=p:\Kits$\CompGroup\ICP\ibex_client_jre

set PATH=%M2%;%JAVA_HOME%;%PYTHON%;%PATH%

if "%IS_E4_DEPLOY%" == "YES" (
    set BUILT_CLIENT_DIR=base\uk.ac.stfc.isis.ibex.e4.client.product\target\products\ibex.product\win32\win32\x86_64
) else (
    set BUILT_CLIENT_DIR=base\uk.ac.stfc.isis.ibex.client.product\target\products\ibex.product\win32\win32\x86_64
)

call build.bat "LOG" %BUILT_CLIENT_DIR%
if %errorlevel% neq 0 exit /b %errorlevel%

REM set EXIT=YES will change error code to 1 if not set previously so store the current
set build_error_level=%errorlevel%

@echo on

REM Whether to deploy
set EXIT=YES
if "%DEPLOY%" == "YES" set EXIT=NO
if "%RELEASE%" == "YES" set EXIT=NO
if "%IS_E4_DEPLOY%" == "YES" set EXIT=NO
if "%EXIT%" == "YES" exit /b %build_error_level%

REM Copy zip to installs area
REM Delete older versions?
REM the password for isis\IBEXbuilder is contained in the BUILDERPW system environment variable on the build server
net use p: /d /yes
net use p: \\isis\inst$

python.exe purge_archive_client.py

set TARGET_DIR=built_client

REM Don't group these. Bat expands whole if at once, not sequentially
if "%RELEASE%" == "YES" (
    set RELEASE_DIR=p:\Kits$\CompGroup\ICP\Releases\%GIT_BRANCH:~8%
    set RELEASE_VERSION=%GIT_BRANCH:~8%    
) else (
    set RELEASE_VERSION=devel-%GIT_COMMIT:~0,7%
)
if "%RELEASE%" == "YES" set INSTALLBASEDIR=%RELEASE_DIR%\Client
if "%RELEASE%" == "YES" set INSTALLDIR=%INSTALLBASEDIR%

if not "%RELEASE%" == "YES" (
    if "%IS_E4_DEPLOY%" == "YES" (
        set INSTALLBASEDIR=p:\Kits$\CompGroup\ICP\Client_E4
    ) else (
        set INSTALLBASEDIR=p:\Kits$\CompGroup\ICP\Client
    )
) 

if not "%RELEASE%" == "YES" set INSTALLDIR=%INSTALLBASEDIR%\BUILD%BUILD_NUMBER%
REM Set a symlink for folder BUILD_LATEST to point to most recent build
if not "%RELEASE%" == "YES" set INSTALLLINKDIR=%INSTALLBASEDIR%\BUILD_LATEST

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

robocopy %CD%\..\%TARGET_DIR% %INSTALLDIR%\Client /MT /MIR /R:1 /NFL /NDL /NP /NS /NC /LOG:NUL
if %errorlevel% geq 4 (
    if not "%INSTALLDIR%" == "" (
        @echo Removing invalid client directory %INSTALLDIR%\Client
        rd /q /s %INSTALLDIR%\Client
    )
    @echo Client copy failed
    exit /b 1
)

REM Copy the JRE across 
robocopy %JRELOCATION% %INSTALLDIR%\Client\jre /MT /MIR /R:1 /NFL /NDL /NP /NS /NC /LOG:NUL
if %errorlevel% geq 4 (
    @echo Failed to copy JRE across
    exit /b 1
)

if not "%RELEASE%"=="YES" (
    if exist "%INSTALLLINKDIR%" (
        rmdir "%INSTALLLINKDIR%"
    )
    mklink /J "%INSTALLLINKDIR%" "%INSTALLDIR%"
)

REM Copy the install script across
cd %BASEDIR%
copy /Y %BASEDIR%\install_client.bat %INSTALLDIR%
if %errorlevel% neq 0 (
    @echo Installl client copy failed
    exit /b %errorlevel%
)

@echo %BUILD_NUMBER%> %INSTALLDIR%\Client\BUILD_NUMBER.txt
@echo %RELEASE_VERSION%> %INSTALLDIR%\Client\VERSION.txt

@echo Copy complete>%INSTALLDIR%\COPY_COMPLETE.txt

if not "%RELEASE%" == "YES" (
    @echo %BUILD_NUMBER%>%INSTALLDIR%\..\LATEST_BUILD.txt 
)
