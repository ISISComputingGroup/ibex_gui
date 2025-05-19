REM %~dp0 expands to directory where this file lives
setlocal enabledelayedexpansion

set BASEDIR=%~dp0

set M2=%MAVEN%bin
set PYTHON3=%WORKSPACE%\Python3\python.exe
set PYTHON_HOME=%WORKSPACE%\Python3

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

set PUBLISH=NO
if "%RELEASE%" == "YES" set PUBLISH=YES
if "%DEPLOY%" == "YES" set PUBLISH=YES

if "%PUBLISH%" == "NO" exit /b 0

REM disable msi for now
REM call build_msi.bat %BASEDIR%.. %TARGET_DIR% %MSINAME%
REM if !errorlevel! neq 0 exit /b !errorlevel!

REM we disable method filters (-mf=off) to avoid 7zip pack/unpack version issues
REM with recently introduced ARM64 filter, excluding arm64 named files
REM was not enough
pushd %CD%\..\%TARGET_DIR%
if exist "..\Client-tmp.7z" del "..\Client-tmp.7z"
"c:\Program Files\7-Zip\7z.exe" a "..\Client-tmp.7z" . -mx1 -mf=off -r -xr^^!*-arm.exe -xr^^!*-arm64.exe
set errcode=!errorlevel!
popd
if !errcode! gtr 1 exit /b !errcode!

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
	if "%JOB_NAME%" == "ibex_gui_win11_pipeline" (
        set INSTALLBASEDIR=\\isis.cclrc.ac.uk\inst$\Kits$\CompGroup\ICP\Client_E4_win11
    )
    if not "%GIT_BRANCH%" == "" (
        set "INSTALLBASEDIR=!INSTALLBASEDIR!\branches\%GIT_BRANCH%"
    )
) 

if not "%RELEASE%" == "YES" (
    set INSTALLDIR=%INSTALLBASEDIR%\BUILD-%BUILD_NUMBER%
)

if not exist "%INSTALLBASEDIR%" mkdir "%INSTALLBASEDIR%"
if not exist "%INSTALLDIR%" mkdir "%INSTALLDIR%"

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
    if not exist "%INSTALLDIR%\Client" (
        @echo Creating client directory %INSTALLDIR%\Client
        mkdir %INSTALLDIR%\Client
    )
)

if "%RELEASE%" == "YES" (
    @echo Copying full tree as RELEASE
    robocopy %CD%\..\%TARGET_DIR% %INSTALLDIR%\Client /MT /MIR /R:1 /NFL /NDL /NP /NS /NC /LOG:"copy_client.log"
    set errcode=!errorlevel!
) else (
    @echo Not copying tree as zip only install
    @echo YES> %INSTALLDIR%\Client\ZIP_ONLY_INSTALL.txt
    set errcode=0
)
if %errcode% geq 4 (
    if not "%INSTALLDIR%" == "" (
        @echo Removing invalid client directory %INSTALLDIR%\Client
        rd /q /s %INSTALLDIR%\Client
    )
    @echo Client copy failed
    exit /b 1
)

REM copy MSI
if exist "%MSINAME%.msi" (
    xcopy /i /y /j %MSINAME%.msi %INSTALLDIR%
    if !errorlevel! neq 0 (
        @echo MSI copy failed
        exit /b !errorlevel!
    )
)

REM 7zip archive
if not exist "%INSTALLDIR%\zips" mkdir %INSTALLDIR%\zips
if exist "%INSTALLDIR%\zips\Client-tmp.7z" del "%INSTALLDIR%\zips\Client-tmp.7z"
if exist "%INSTALLDIR%\zips\Client.7z" del "%INSTALLDIR%\zips\Client.7z"
xcopy /i /y /j "%CD%\..\Client-tmp.7z" %INSTALLDIR%\zips
if %errorlevel% neq 0 (
    @echo 7z copy failed
    exit /b %errorlevel%
)
ren "%INSTALLDIR%\zips\Client-tmp.7z" "Client.7z"
if %errorlevel% neq 0 (
    waitfor /t 30 WillNeverHappen >NUL 2>&1
    ren "%INSTALLDIR%\zips\Client-tmp.7z" "Client.7z"
)
if %errorlevel% neq 0 (
    @echo 7z rename failed
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
