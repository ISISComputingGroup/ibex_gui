@echo CREATE_ICP_BINARIES: Mirroring "ICP_Binaries" from local ISIS share
robocopy "\\shadow.isis.cclrc.ac.uk\ICP_Binaries$\maven" "%~dp0maven" /MT /NP /NFL /NDL /MIR /R:2 /LOG:"%~dp0ICP_Binaries_maven.log"
set errcode=%ERRORLEVEL%
if %errcode% GEQ 4 (
    type "%~dp0ICP_Binaries_maven.log"
    @echo *** Robocopy Exit Code %errcode% ***
	exit /b 1
)
exit /b 0
