@echo off
rem A shortcut wrapper script to start the GUI sql tool.
rem
rem usage eg: bin\manager.bat --urlid testdb 
rem
rem @author Zemian Deng (dengz1) 01/19/2009
rem
set APP_HOME=%~dp0..
%APP_HOME%\bin\run.bat org.hsqldb.util.DatabaseManagerSwing --rcfile %APP_HOME%/data/sqltool.rc %*
