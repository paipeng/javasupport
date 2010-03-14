@echo off
set SCRIPT_HOME=%~dp0
set JAVA_OPTS=-Dtoolhome=%SCRIPT_HOME%..
%SCRIPT_HOME%run.bat javasupport.toolbox.TemplateGenerator %*
