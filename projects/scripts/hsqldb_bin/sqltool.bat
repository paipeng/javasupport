@echo off
rem A shortcut wrapper script to start commandline SQLTOOL for hsqldb.
rem This requires a sqltool.rc file, which one setup under data directory
rem for testdb connection.
rem
rem @author Zemian Deng (dengz1) 01/19/2009
rem
set APP_HOME=%~dp0..
%APP_HOME%\bin\run.bat org.hsqldb.util.SqlTool --rcfile %APP_HOME%/data/sqltool.rc %*
