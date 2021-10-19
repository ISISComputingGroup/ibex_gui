pushd %~dp0..\base\uk.ac.stfc.isis.ibex.scriptgenerator\python_support
call run_tests.bat
popd

if %ERRORLEVEL% NEQ 0 (
    @echo Python support unit tests failed
	exit /b %ERRORLEVEL%
)
