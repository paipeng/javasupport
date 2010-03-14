@echo off
rem A shortcut wrapper script to start HyperSonic DB server.
rem
rem @author Zemian Deng (dengz1) 01/19/2009
rem
rem Use -? option to see help page!
rem
rem NOTE: Due to hsqldb server starup limitation, this script MUST cd
rem into hsqldb\data to startup the server.
rem

set APP_HOME=%~dp0..
rem CD into dir where server.properties is.
cd %APP_HOME%\data
%APP_HOME%\bin\run.bat org.hsqldb.Server %*

