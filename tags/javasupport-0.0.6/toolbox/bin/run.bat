@echo off
set SCRIPT_HOME=%~dp0
set SCRIPT_PARENT_HOME=%SCRIPT_HOME%..
java %JAVA_OPTS% -classpath "%JAVA_CLASSPATH%;%SCRIPT_PARENT_HOME%\lib\*" %*
