@echo off
set TOOL_HOME=%~dp0..
scala -classpath "%TOOL_HOME%\target\classes" deng.toolbox.%*
