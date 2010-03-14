function makescripts(script, javaclass)
{
 # Create an MSDOS/Windows Batch file (one per command)
 batfile = (script ".bat")
 print "Creating", batfile, ("(" javaclass ")")
 print "@echo off"  > batfile
 print "rem $Id$" >> batfile
 print "call runjava.bat",javaclass,"%*" >> batfile

 # Create a UNIX (MSYS / Cygwin) Shell Script (one per command)
 print "Creating", script, ("(" javaclass ")")
 print "#!/bin/sh"  > script
 print "# $Id$" >> script
 print "runjava",javaclass,"$@" >> script

 # NOTE Could also create a single UNIX change that uses
 #      a case on the file name to run the correct class
 #      and then link all the others to it.
}

BEGIN { FS="[.: ]+" }

{
 makescripts($1,$7)
}
