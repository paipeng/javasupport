@echo off
if "%OS%"=="Windows_NT" setlocal
set JAVA_APP_HOME=%~dp0..
scalac -d out -cp "%JAVA_APP_HOME%\target\classes;%JAVA_APP_HOME%\lib\*" %*
if "%OS%"=="Windows_NT" endlocal

