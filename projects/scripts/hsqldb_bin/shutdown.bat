@echo off
rem A shortcut wrapper script to stop HyperSonic DB server.
rem
rem
rem @author Zemian Deng (dengz1) 01/19/2009
rem
set APP_HOME=%~dp0..
%APP_HOME%\bin\sqltool --sql shutdown testdb
