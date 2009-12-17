
###############################
## Typical Cygwin/Linux helpers
###############################

# append to path without repeating.
function pathmunge {
        if ! echo $PATH | egrep -q "(^|:)$1($|:)" ; then
           if [ "$2" = "after" ] ; then
              PATH=$PATH:$1
           else
              PATH=$1:$PATH
           fi
        fi
}
export -f pathmunge

# Move/Remove files into a trash can dir.
function bak {
  mv -vf $1 ${1}.`ts`.bak
}
export -f bak

# Same as bak, but use copy instead of move.
function bakc {
  cp -rf $1 ${1}.`ts`.bak
}
export -f bakc

# Convert a cygwin unix path into Windows path.
function wpath {
  cygpath -wl $(ruby -e "puts File.expand_path(ARGV[0])" $1)
}
export -f wpath

# Open any file or dir using system's open or explorer command.
function open {
  explorer $(wpath $1)
}
export -f open

# Use this insetad of rm command.
function trash {
  TRASHCAN=~/.trash/`ts`
  if [ -e $TRASHCAN ]; then
		echo "Renaming existing trash can: $TRASHCAN"
    mv -vf $TRASHCAN ${TRASHCAN}.`date "+%N"`
  fi
	mkdir -p $TRASHCAN
  echo "Deleting to trash can: $TRASHCAN"
  mv -f $* $TRASHCAN
}
export -f trash

# Quicky command aliases
alias e=/apps/jEdit/jedit.bat
alias eb='e $(wpath ~/.bashrc)'
alias ebc='e $(wpath /source/javasupport/branches/scripts/shell/bashrc-cygwin.sh)'
alias ej='e $(wpath /source/journals/tech-`date "+%m%d%Y"`.txt)'
alias rb='exec bash'
alias ts="date '+%m%d%Y-%H%M'"
alias ll='ls -lA'
alias findx='find . -name'
alias openports='netstat -a | grep LISTENING'
alias cdjs='cd /source/javasupport/branches'


###############################
## Java Development helpers
###############################

# Open a javadoc file under java.lang package.
function jdoc {
  open "http://java.sun.com/javase/6/docs/api/java/lang/$1.html"
}
export jdoc

# Display all the failed tests under maven surefire-reports dir.
function failedtests {
  wc -l target/surefire-reports/* | ruby -ane 'puts $F[1] if $F[1] != "total" && $F[0].to_i > 4'
}
export -f failedtests

# Create a java project using a local catalog of Maven's archetype.
function mvngenjava {
  PKG=$(echo $1 | ruby -pe "gsub(/[^[:alnum:]]/, '')")
  mvn archetype:generate -DinteractiveMode=false -DarchetypeCatalog=local \
  -DarchetypeGroupId=deng.archetypes -DarchetypeArtifactId=simple-java-app-archetype -DarchetypeVersion=1.0-SNAPSHOT \
  -DgroupId=deng.$1 -DartifactId=$1 -Dpackage=deng.$PKG
}
export -f mvngenjava

# Some quicky command aliases.
alias mvngen='mvn archetype:generate -DarchetypeCatalog=local'
alias mvnpkg='mvn -Dmaven.test.skip package'
alias mvnins='mvn -Dmaven.test.skip install'
alias finds='find src | grep'


###############################
## For creating cygwin xterm terminal
###############################

#alias xterm='xterm -display :0.0 -bd white -bg black -fg white -geometry 120x35 -sb -rightbar -sl 5000 -e bash &'
alias xterm='cmd /c `wpath /bin/rxvt` -bg black -fg white -geometry 120x35 -sl 2000 -sr -fn "Courier New-16" -e bash &'

