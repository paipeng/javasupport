@echo off
set SCRIPT_DIR=%~dp0
java -cp "%SCRIPT_DIR%..\target\classes;%SCRIPT_DIR%..\dist\*" ztool.Run %*
