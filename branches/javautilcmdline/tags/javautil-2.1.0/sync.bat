@echo off
rem This script will help sync javautil project built dir to your local install dir.
rem USAGE Example: cd %JAVAUTIL_HOME% &&  sync /c/opt
rem Notice the destination ONLY need parent dir name!

rem You need cwRsync install first.
rsync --progress --archive --delete --exclude="**/.source.jam, **/.svn" dist/javautil /cygdrive%1