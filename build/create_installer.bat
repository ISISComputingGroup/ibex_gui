if exist C:\"Program Files (x86)"\7-Zip\7z.exe set ZIPEXE=C:\"Program Files (x86)"\7-Zip\7z.exe
if exist C:\"Program Files"\7-Zip\7z.exe set ZIPEXE=C:\"Program Files"\7-Zip\7z.exe

REM the password for isis\builder is contained in the BUILDERPW system environment variable on the build server 
net use p: /d
net use p: \\isis\inst$ /user:isis\builder %BUILDERPW%

REM Everything is already grouped up on Kits
set INSTALLBASEDIR=p:\Kits$\CompGroup\ICP\Client
set INSTALLDIR=%INSTALLBASEDIR%\BUILD%BUILD_NUMBER%

REM First zip everything up
%ZIPEXE% a installer.7z %INSTALLDIR%\*

REM Add the install_client.bat to the archive
%ZIPEXE% a installer.7z install_client_zip.bat

REM Build the installer
copy /b 7zSD.sfx + installer_config.txt + installer.7z ClientInstaller.exe
if %errorlevel% neq 0 (
    @echo Installer build failed
    exit /b %errorlevel%
)

REM Copy to Kits
xcopy /s /e /h %GENIEPYTHONDIR% %INSTALLDIR%
if %errorlevel% neq 0 (
    @echo Installer copy failed
    exit /b %errorlevel%
)

REM Delete local copies
del installer.7z
del ClientInstaller.exe