@echo off
setlocal
set MYDIR=%~dp0

REM FILESROOT is the directory above the "Client" directory
set FILESROOT=%1

if exist "c:\Program Files (x86)\WiX Toolset v3.10\bin\heat.exe" (
	set WIXBIN=c:\Program Files (x86)\WiX Toolset v3.10\bin
)
if exist "c:\Program Files (x86)\WiX Toolset v3.11\bin\heat.exe" (
		set WIXBIN=c:\Program Files (x86)\WiX Toolset v3.11\bin
)

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

REM change directory to avoid too long path errors, use pushd as it works with UNC paths
pushd %FILESROOT%

REM -sw5150 supresses warning about self registering DLLs
"%WIXBIN%\heat.exe" dir .\Client -gg -scom -sreg -svb6 -sfrag -sw5150 -template feature -var var.MySource -dr INSTALLDIR -cg MyCG -t %MYDIR%wxs2wxi.xsl -out ibex_client.wxi
if %errorlevel% neq 0 goto ERROR

copy %MYDIR%ibex_client_master.wxs ibex_client.wxs
"%WIXBIN%\candle.exe" -dMySource=.\Client -dVersionLong=%IBEXVERSIONLONG% -dVersionShort=%IBEXVERSIONSHORT% ibex_client.wxs
if %errorlevel% neq 0 goto ERROR

REM -sice:ICE60 is to stop font install warnings (from JRE)
"%WIXBIN%\light.exe" -sice:ICE60 -ext WixUIExtension ibex_client.wixobj
if %errorlevel% neq 0 goto ERROR

@echo Successfully created ibex_client.msi

del ibex_client.wxs
del ibex_client.wxi
del ibex_client.wixobj
del ibex_client.wixpdb

popd

goto :EOF

:ERROR

@echo ERROR creating ibex_client.msi

del ibex_client.*

popd

exit /b 1
