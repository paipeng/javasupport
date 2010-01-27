@echo off
rem 
rem A wrapper script to jump start Java program
rem $Id: filefind.bat 4 2006-03-16 15:27:19Z zemian $
rem :tabSize=2:indentSize=2:
rem 

if "%OS%"=="Windows_NT" @setlocal EnableDelayedExpansion

rem #===============================================================================
rem # Java main class
rem #=============================================================================== 
set MAIN_CLASS=jragonsoft.javautil.cmdtool.FileFind

rem #===============================================================================
rem # Resolve application HOME directory
rem #===============================================================================

rem %~dp0 is expanded pathname of the current script under NT
set SCRIPT_HOME=%~dp0
set PROGRAM_HOME=%~dp0..

rem Get all arguments from XP cmd line
set ARGS=%*

rem #===============================================================================
rem # Source custom env script
rem #===============================================================================
if exist "%SCRIPT_HOME%\setenv.bat" call "%SCRIPT_HOME%\setenv.bat"

rem #===============================================================================
rem # Build java classpath
rem #===============================================================================
if "%LOCALCLASSPATH%"=="" set LOCALCLASSPATH=.
rem add jar files from lib. You need to enable "@setlocal EnableDelayedExpansion"
for %%F in ("%PROGRAM_HOME%\lib\*.jar") do set LOCALCLASSPATH=!LOCALCLASSPATH!;%%F

rem #===============================================================================
rem # Run Java command with options settings
rem #===============================================================================
if not "%JAVA_HOME%"=="" set JAVA_CMD=%JAVA_HOME%\bin\java.exe
if "%JAVA_CMD%"=="" set JAVA_CMD=java.exe
	
rem set PROGRAM_ARGS=
rem set JAVA_OPTS=
rem set JAVA_OPTS=%JAVA_OPTS% "-Dlog4j.configuration=file:///%PROGRAM_HOME%/conf/log4j.properties"

rem run IT! -- use echo instead to debug
"%JAVA_CMD%" %JAVA_OPTS% -classpath "%LOCALCLASSPATH%" %MAIN_CLASS% %PROGRAM_ARGS% %ARGS%
