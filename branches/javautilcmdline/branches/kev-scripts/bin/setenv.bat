rem #
rem #	Environment settings to be sourced for custom install.
rem #	$Id: setenv.bat 4 2006-03-16 15:27:19Z zemian $
rem # :tabSize=2:indentSize=2:
rem #

rem # preset localclasspath first
if "%LOCALCLASSPATH%"=="" set	LOCALCLASSPATH=.

rem # add classpath if exists
if not "%CLASSPATH%"=="" set	LOCALCLASSPATH=%LOCALCLASSPATH%;%CLASSPATH%

rem # add development class path if exists
if exist "%PROGRAM_HOME%/build/classes" set LOCALCLASSPATH=%LOCALCLASSPATH%;%PROGRAM_HOME%/build/classes

