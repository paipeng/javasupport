
Summary:
ztool contains many command line utilities that process text and files,
like those basic coreutil commands found in Unix/Linx environment.
Since all ztool programs are written in Java, you may run it on any OS
platform that supports Java Virtual Machine (JRE) 1.5 or higher.

This particular Help program will list all commands available in the
package library.

== About help format and syntaxs:
  All format rule written on help page use the bracket([]) to mean optional
  input, and tag(<>) means required. And they may be nested. An eclipse(...)
  within them means that they can be repeated.

== About Options:
  All programs in this package support a --help option unless stated
  otherwise. And option flag can be enter by long (two dashes) or
  short (one dash) using the first character of the long flag. If there
  are duplicated short flag, then only first one will be used, and the
  rest must use long flag instead.

  If option value are given, it must be follow by equal char(=) then a
  value, and no space is allowed in between.

  You may escape all option parsing from rest of arguments manually with
  a double dash (--).

== Installation:
  If you have the ztool.jar file, then you may invoke a command like so:
    java -cp ztool.jar ztool.Run Sysinfo # Print system info.
    java -cp ztool.jar ztool.Cat *.txt      # Print all text files on screen.
    java -cp ztool.jar ztool.Run Help       # Display all available commands.

  You may also create a shell script named ztool (ztool.bat for Windows) that
  can be added to your system path. Here is how a Windows script looks like:
    @echo off
    set SCRIPT_DIR=%~dp0
    java -cp "%SCRIPT_DIR%\*" ztool.Run %*

  Now you may call programs like this:
    ztool Sysinfo
    ztool Sysinfo -s | ztool Grep name

== Author:
  ztool package is created by Zemian Deng.


Usage:
Help [Options] 

Options: ([--flag[=value]])
  d or debug Show extra debug info.
  h or help  Display this help page.

Examples:
  ztool Help
  ztool Help --help


