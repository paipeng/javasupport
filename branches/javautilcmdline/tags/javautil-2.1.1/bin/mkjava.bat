@echo off
rem A wrapper script to jump start Java program

set MAIN_CLASS=jragonsoft.javautil.cmdtool.MakeClassFile
set PROGRAM_HOME=%~dp0..
set ARGS=%*
set LOCALCLASSPATH=.;build\classes;%PROGRAM_HOME%\lib\javautil.jar
set JAVA_CMD=java.exe
"%JAVA_CMD%" -classpath "%LOCALCLASSPATH%" %MAIN_CLASS% %ARGS%
