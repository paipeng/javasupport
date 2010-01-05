# BASH RC Script for Cygwin Shell
# Created by Zemian Deng on 12/24/2009

###############################
# Environment Variables
###############################

# TMP and TEMP are defined in the Windows environment.  Leaving
# them set to the default Windows temporary directory can have
# unexpected consequences.
unset TMP
unset TEMP

# Alternatively, set them to the Cygwin temporary directory
# or to any other tmp directory of your choice
export TMP=/tmp
export TEMP=/tmp
export EDITOR=/apps/jEdit/jedit.bat

# Change propmt string and color
#PS1='\e[0;32m\u@\h:\e[0;33m\w\e[m\n\$ '

###############################
## PATH Setup
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

pathmunge /apps/jdk/bin # Make this path in front so it overwrite default Window's path version.
pathmunge /apps/ant/bin after
pathmunge /apps/maven/bin after
pathmunge /apps/jruby/bin after
pathmunge /apps/groovy/bin after


###############################
## Shell Helper Functions
###############################

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
	cygpath -wl "$@"
}
export -f wpath

# Open any file or dir using system's open or explorer command.
function open {                
	ARGS="$@"
	LAUNCHER=explorer
	ruby -e 'exit(1) if ARGV[0] =~ /^(https{0,1}:|file:)/' "$@"
	if [ "$?" -eq 0 ] ; then
  	ARGS=$(wpath "$@")
  else
    BROWSER=${BROWSER:=explorer}
  	LAUNCHER=${BROWSER}
  fi
  echo $ARGS
  "$LAUNCHER" $ARGS
}
export -f open

# Use this insetad of rm command.
function trash {
  TRASHCAN=~/.trash/`ts`
  if [ -e $TRASHCAN ]; then
		echo "Renaming existing trash can: $TRASHCAN"
    \mv -vf $TRASHCAN ${TRASHCAN}.`date "+%N"`
  fi
	mkdir -p $TRASHCAN
  echo "Deleting to trash can: $TRASHCAN"
  \mv -f "$@" $TRASHCAN
}
export -f trash

function e() {
  EDITOR=${EDITOR:=/cygdrive/c/WINDOWS/notepad.exe}
	$EDITOR $(wpath "$@")
}
export -f e

###############################
## Aliases For Shorter Commands
###############################

# Some example alias instructions
# If these are enabled they will be used instead of any instructions
# they may mask.  For example, alias rm='rm -i' will mask the rm
# application.  To override the alias instruction use a \ before, ie
# \rm will call the real rm not the alias.

# Interactive operation...
#alias rm='rm -i'
alias rm=trash
alias cp='cp -i'
alias mv='mv -i'

# Default to human readable figures
alias df='df -h'
alias du='du -h'

# Misc :)
alias less='less -r'                          # raw control characters
alias whence='type -a'                        # where, of a sort
alias grep='grep --color'                     # show differences in colour

# Some shortcuts for different directory listings
alias ls='ls -hF --color=tty'                 # classify files in colour
alias dir='ls --color=auto --format=vertical'
alias vdir='ls --color=auto --format=long'
alias ll='ls -l'                              # long list
alias la='ls -A'                              # all but . and ..
alias l='ls -CF'                              #


# Very short and frequently used commands
alias t=trash                          # trashing file
alias ts="date '+%m%d%Y-%H%M'"         # timestamp label
alias eb='e ~/.bashrc'                 # edit .bashrc file
alias ebx='e ~/.bashrc-extra'          # edit .bashrc-extra file
alias ej='e /source/journals/`date "+%m%d%Y"`.txt'
alias rb='exec bash'                   # reload bashrc
function f() {
  TEXT=$1
  DIR=.
  if (( $# > 1 )); then DIR=$2; fi
  find $DIR -iname "*${TEXT}*" | grep -v .svn
}
#alias top5bigfiles='find . -type f -exec ls -s {} \; | sort -n -r | head -5'
#alias top5smallfiles='find . -type f -exec ls -s {} \; | sort -n | head -5'
#alias findlasthour='find . -mmin -60'
#alias findlastday='find . -mtime -1'


# Longer Commands
alias link='ln -s'
alias openports='netstat -a | grep LISTENING'
alias printpath='echo $PATH | ruby -pe "gsub(/:/, \"\n\")"'
#alias xterm='xterm -display :0.0 -bd white -bg black -fg white -geometry 120x35 -sb -rightbar -sl 5000 -e bash --login &'
alias xterm='cmd /c `wpath /bin/rxvt` -bg black -fg white -geometry 120x35 -sl 2000 -sr -fn "Courier New-16" -e bash --login &'

# ztool alias (assume you link /js to /source/javasupport/branches)
alias ztool='/js/ztool/bin/ztool'

###############################
## Subversion Helpers
###############################
function svnadd() {
  ruby -e '
    files = []
    `svn status`.each_line do |ln|
      words = ln.split
      files << words[1] if words[0].strip == "?"
    end
    system("svn add #{files.join(" ")}") unless files.size == 0
  ' "$@"
}
export -f svnadd
function svnrm() {
  ruby -e '
    files = []
    `svn status`.each_line do |ln|
      words = ln.split
      files << words[1] if words[0].strip == "!"
    end
    system("svn remove #{files.join(" ")}") unless files.size == 0
  ' "$@"
}
export -f svnrm
function svnmove() {
  ruby -e '
    to_dir=ARGV.pop # last element
    ARGV.each { |n| system("svn move #{n} #{to_dir}") }
  ' "$@"
}
export -f svnmove
alias svnci='svn commit -m ""'
alias svnig='svn ps svn:ignore'
alias svnpig='svn pl . -v'
alias svns='svn status'
alias svnup='svn update .'
function svnigproj() {
svnig 'target
.settings
.classpath
.project' .
}
alias svnall='svnadd && svnrm && svnci'

###############################
## Java Development Helpers
###############################
export JAVA_HOME=${JAVA_HOME:=/apps/jdk}
function setwjhome() {
	JAVA_HOME=`wpath $JAVA_HOME` # set JAVA_HOME to a Windows PATH.
}
export -f setwjhome
function setjhome() {
	JAVA_HOME=`cygpath --unix $JAVA_HOME` # set JAVA_HOME to a unix PATH.
}
export -f setjhome

# Open a javadoc file under java.lang package.
function jdoc {
	CLS=`ruby -e 'c=ARGV[0]; if c.include?("."); puts c.gsub(/\./, "/") else puts "java/lang/#{c}" end' "$@"`
	JDOC=${JDOC:=http://java.sun.com/javase/6/docs/api}
  open "$JDOC/$CLS.html"
}
export -f jdoc

function mkcp() {
  export CP=`cygpath --path --windows $(ruby -e 'puts ARGV.map{ |e| e.gsub(%r(/), %q(\\\)) }.join(";")' "$@")`
  echo "export CP=$CP"
}
export -f mkcp
alias javacp='java -cp $CP'
alias todot='ruby -pe "gsub(/\//, \".\")"'

# Create a java project using a local catalog of Maven's archetype.
function mvngenjava {
  PKG=$(echo $1 | ruby -pe "gsub(/[^[:alnum:]]/, '')")
  mvn archetype:generate -DinteractiveMode=false -DarchetypeCatalog=local \
  -DarchetypeGroupId=deng.archetypes -DarchetypeArtifactId=simple-java-app-archetype -DarchetypeVersion=1.0-SNAPSHOT \
  -DgroupId=deng.$1 -DartifactId=$1 -Dpackage=deng.$PKG
}
export -f mvngenjava

# Short Maven Commands
alias finds='find src | grep'
alias mvnc='mvn compile'
alias mvnp='mvn package'
alias mvnt='mvn test'
alias mvnt1='mvn test -Dtest='
alias mvngen='mvn archetype:generate -DarchetypeCatalog=local'
alias mvnnt='mvn -Dmaven.test.skip' # no test / skip test
alias mvncpdp='mvn dependency:copy-dependencies'
alias mkcptarget='mkcp target/classes "target/dependency/*"'

# Display all the failed tests under maven surefire-reports dir.
function failedtests {
  wc -l target/surefire-reports/* | ruby -ane 'puts $F[1] if $F[1] != "total" && $F[0].to_i > 4'
}
export -f failedtests

###############################
## JBoss Dev Helpers
###############################
#export JBOSS_HOME=`wpath /apps/jboss`
alias rjb='cd /apps/jboss; bin/run.sh'   # run jboss
alias rjbd='rjb -c default'                                          # run jboss with default server config
alias mkcpjbclient='mkcp target/classes "target/dependency/*" "/apps/jboss/client/*"'

###############################
## Allow User Custom Overwrite
###############################
if [ -e ~/.bashrc-extra ] ; then
  source ~/.bashrc-extra
fi
