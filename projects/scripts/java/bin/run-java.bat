@echo off
@rem
@rem Start and run a generic java/JVM process with given main class as 
@rem argument. Caller may customize JAVA_HOME, JAVA_OPTS and JAVA_CLASSPATH
@rem variables that would drive this script, else it default to some simple
@rem settings.
@rem
@rem Author: Zemian Deng 05/6/2009
@rem

@rem Set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" setlocal

@rem Set the APP_HOME var (not where wrapper script is, but the app home)
set SCRIPT_DIR=%~dp0
set APP_HOME=%SCRIPT_DIR%\..

@rem Set java options
if "%JAVA_OPTS%" == "" set JAVA_OPTS=-Xmx128m ^
-Djava.app.home=%APP_HOME%

@rem Set java classpath
if "%JAVA_CLASSPATH%" == "" set JAVA_CLASSPATH=.;^
%APP_HOME%\lib\*;^
%APP_HOME%\target\classes

@rem set Java command executable
set JAVA_CMD=java.exe
if not "%JAVA_HOME%" == "" set JAVA_CMD=%JAVA_HOME%\bin\java.exe

@rem Invoke JVM process with a Main class
"%JAVA_CMD%" %JAVA_OPTS% -classpath "%JAVA_CLASSPATH%" %*

@rem End local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" endlocal

