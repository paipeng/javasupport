BEGIN {
  FS="[.: ]+"
  cnt = 0
}

{
 script[cnt] = $1
 javaclass[cnt] = $7
 cnt += 1
}

END {
  #
  # Create the unified UNIX Script 'all'
  #
  OFS=""
  out="all"

  print "#!/bin/sh" > out
  print "# $Id$" >> out
  print "# Main shell script for UNIX access to javautils" >> out
  print "# all of the actual scripts are linked to this file" >> out
  print "#" >> out
  print "" >> out
  print "cmd=$(basename $0)" >> out
  print "" >> out
  print "case $cmd in" >> out

  for (i=0; i<cnt; i++) {
    print "  ",script[i],") jclass=",javaclass[i]," ;;" >> out
  }

  print "esac" >> out
  print "" >> out
  print "if [ -n \"$jclass\" ]; then" >> out
  print "  runjava $jclass $*" >> out
  print "else" >> out
  print "  echo \"Error: unknown command: $cmd\"" >> out
  print "fi" >> out

  #
  # Output Link commands (to create the links at the shell prompt)
  #
  print "# These can be piped to sh to create the links"
  for (i=0; i<cnt; i++) {
    print "ln all ",script[i]
  }

}
