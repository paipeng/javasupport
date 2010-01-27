@echo off
rem Copyright ${__RES_TEMPLATE_YEAR} Zemian Deng
rem 
rem Licensed under the Apache License, * Version 2.0 (the "License"); you may not
rem use this file except in compliance with the License. You may obtain a copy of
rem the License at
rem     http://www.apache.org/licenses/LICENSE-2.0 
rem Unless required by applicable law or agreed to in writing, software distributed
rem under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
rem CONDITIONS OF ANY KIND, either express or implied. See the License for the
rem specific language governing permissions and limitations under the License.

rem 
rem A wrapper script to jump start Java program
rem $Id: template.bat 2820 2006-01-18 18:41:40Z zemian.deng $
rem :tabSize=2:indentSize=2:
rem 

if "%OS%"=="Windows_NT" @setlocal EnableDelayedExpansion

rem #===============================================================================
rem # Java main class
rem #=============================================================================== 
set MAIN_CLASS=jragonsoft.javautil.cmdtool.UniqueText

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
if "%LOCALCLASSPATH%"=="" set LOCALCLASSPATH=.;build\classes
rem add jar files from lib. You need to enable "@setlocal EnableDelayedExpansion"
for %%F in ("%PROGRAM_HOME%\lib\*.jar") do set LOCALCLASSPATH=!LOCALCLASSPATH!;%%F

rem #===============================================================================
rem # Run Java command with options settings
rem #===============================================================================
if not "%JAVA_HOME%"=="" set JAVA_CMD=%JAVA_HOME%\bin\java.exe
if "%JAVA_CMD%"=="" set JAVA_CMD=java.exe

rem run IT! -- use echo instead to debug
"%JAVA_CMD%" %JAVA_OPTS% -classpath "%LOCALCLASSPATH%" %MAIN_CLASS% %PROGRAM_ARGS% %ARGS%
