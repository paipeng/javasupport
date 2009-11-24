@echo off
set SCRIPT_DIR=%~dp0
%SCRIPT_DIR%\run-java -Dhttp.proxyHost=proxy2.lmco.com ztool.Run %*
