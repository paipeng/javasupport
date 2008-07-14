@echo off
set SCRIPT_HOME=%~dp0..
java %JAVA_OPTS% -classpath "%JAVA_CLASSPATH%;%SCRIPT_HOME%..\lib\*" %*
