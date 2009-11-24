@echo off
rem A OS dependent wrapper script that will build a classpath to load up all jars
rem under lib directory, and then it invoke java executable. User only need to specify
rem the full class name that contains a main method.
rem
rem You may specify where to find java excutable by setting JAVA_HOME in system env
rem variable.
rem
rem This script may also be called by another bat file, in that case you may customize
rem the java invocation by two variable CLASSPATH and JAVA_OPTS. Note that these two
rem should only be set by script per user level, not system wide!
rem 
rem @author Zemian Deng 12/17/2008
rem

rem These classpath style requires JRE6!
set APP_HOME=%~dp0..
set APP_CLASSPATH=%CLASSPATH%
set APP_CLASSPATH=%APP_CLASSPATH%;%APP_HOME%\target\classes
set APP_CLASSPATH=%APP_CLASSPATH%;%APP_HOME%\lib\*
set APP_CLASSPATH=%APP_CLASSPATH%;%APP_HOME%\lib\optional\*
set APP_CLASSPATH=%APP_CLASSPATH%;%APP_HOME%\lib\vendor\*

set APP_JAVA_OPTS=%JAVA_OPTS%
set APP_JAVA_OPTS=-classpath "%APP_CLASSPATH%"

if "%JAVA_HOME%"=="" goto noJavaHome

:withJavaHome
rem Use the specific java set by user env.
set JAVA_CMD=%JAVA_HOME%\bin\java
goto endJavaHome

:noJavaHome
rem Assume java is already in path.
set JAVA_CMD=java
goto endendJavaHome

:endJavaHome

%JAVA_CMD% %APP_JAVA_OPTS% %*
