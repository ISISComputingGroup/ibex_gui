@echo off
setlocal
set "MYDIR=%~dp0"

@echo MSI Kit build disabled
exit / b 0

@echo %TIME% Building MSI Kit

REM FILESROOT is the directory above the CLIENTDIR directory
set "FILESROOT=%1"
set "CLIENTDIR=%2"
set "MSINAME=%3"

for /D %%I in ( "\\isis.cclrc.ac.uk\inst$\Kits$\CompGroup\ICP\Binaries\wixToolset" ) do SET "WIXBIN=%%I"

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

for %%a in (Z Y X W V U T S R Q) do (
   IF NOT EXIST "%%a:\" (set FreeDriveLetter=%%a&&GOTO break)
)
@echo No free drive letter
exit/b 1
:break

REM change directory to avoid too long path errors
subst %FreeDriveLetter%: %FILESROOT%
pushd %FreeDriveLetter%:\

del %MSINAME%.wxi
del %MSINAME%.msi

@echo %TIME% Running HEAT
REM -sw5150 supresses warning about self registering DLLs
"%WIXBIN%\heat.exe" dir .\%CLIENTDIR% -gg -scom -sreg -svb6 -sfrag -sw5150 -template feature -var var.MySource -dr INSTALLDIR -cg MyCG -t %MYDIR%wxs2wxi.xsl -out %MSINAME%.wxi
if %errorlevel% neq 0 goto ERROR
if not exist "%MSINAME%.wxi" (
    @echo %TIME% Unable to create %MSINAME%.wxi
	goto ERROR
)

copy /y %MYDIR%%MSINAME%_master.wxs %MSINAME%.wxs
@echo %TIME% Running CANDLE
"%WIXBIN%\candle.exe" -dMySource=.\%CLIENTDIR% -dVersionLong=%IBEXVERSIONLONG% -dVersionShort=%IBEXVERSIONSHORT% %MSINAME%.wxs
if %errorlevel% neq 0 goto ERROR
if not exist "%MSINAME%.wixobj" (
    @echo %TIME% Unable to create %MSINAME%.wixobj
	goto ERROR
)

@echo %TIME% Running LIGHT
REM -sice:ICE60 is to stop font install warnings (from JRE)
"%WIXBIN%\light.exe" -sice:ICE60 -ext WixUIExtension %MSINAME%.wixobj
if %errorlevel% neq 0 goto ERROR
if exist "%MSINAME%.msi" (
    @echo %TIME% Successfully created %MSINAME%.msi
) else (
    goto ERROR
)

copy /y %MSINAME%.msi %MYDIR%

del %MSINAME%.*

popd
subst /d %FreeDriveLetter%:

goto :EOF

:ERROR

@echo ERROR creating %MSINAME%.msi

del %MSINAME%.*

popd
subst /d %FreeDriveLetter%:

exit /b 1
