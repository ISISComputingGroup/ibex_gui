@echo on
setlocal
set "MYDIR=%~dp0"

@echo %TIME% Building MSI Kit

set "CLIENTDIR=built_client"

REM FILESROOT is the directory above the CLIENTDIR directory
set "FILESROOT=%1"

for /D %%I in ( "c:\Program Files (x86)\WiX Toolset v3.*" ) do SET "WIXBIN=%%I\bin"

set IBEXMAJOR=1
set IBEXMINOR=0
set IBEXPATCH=0
if not "%BUILD_NUMBER%" == "" (
    set IBEXBUILD=%BUILD_NUMBER%
) else (
    set IBEXBUILD=0
)
set IBEXVERSIONLONG=%IBEXMAJOR%.%IBEXMINOR%.%IBEXPATCH%.%IBEXBUILD%
set IBEXVERSIONSHORT=%IBEXMAJOR%.%IBEXMINOR%

REM change directory to avoid too long path errors
subst q: %FILESROOT%
set "OLDDIR=%CD%"
cd /d q:\

del ibex_client.msi

@echo %TIME% Running HEAT
REM -sw5150 supresses warning about self registering DLLs
"%WIXBIN%\heat.exe" dir .\%CLIENTDIR% -gg -scom -sreg -svb6 -sfrag -sw5150 -template feature -var var.MySource -dr INSTALLDIR -cg MyCG -t %MYDIR%wxs2wxi.xsl -out ibex_client.wxi
if %errorlevel% neq 0 goto ERROR

copy /y %MYDIR%ibex_client_master.wxs ibex_client.wxs
@echo %TIME% Running CANDLE
"%WIXBIN%\candle.exe" -dMySource=.\%CLIENTDIR% -dVersionLong=%IBEXVERSIONLONG% -dVersionShort=%IBEXVERSIONSHORT% ibex_client.wxs
if %errorlevel% neq 0 goto ERROR

@echo %TIME% Running LIGHT
REM -sice:ICE60 is to stop font install warnings (from JRE)
"%WIXBIN%\light.exe" -sice:ICE60 -ext WixUIExtension ibex_client.wixobj
if %errorlevel% neq 0 goto ERROR

if exist "ibex_client.msi" (
    @echo %TIME% Successfully created ibex_client.msi
) else (
    goto ERROR
)

del ibex_client.wxs
del ibex_client.wxi
del ibex_client.wixobj
del ibex_client.wixpdb

cd /d %OLDDIR%
subst /d q:

@echo off

goto :EOF

:ERROR

@echo ERROR creating ibex_client.msi

del ibex_client.*

cd /d %OLDDIR%
subst /d q:

@echo off

exit /b 1
