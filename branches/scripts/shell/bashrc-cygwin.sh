# BASH RC Script for Cygwin Shell
# Created by Zemian Deng on 12/24/2009


###############################
## Default Cygwin Bash RC file
###############################
# To pick up the latest recommended .bashrc content,
if [ -e "/etc/defaults/etc/skel/.bashrc" ] ; then
  source "/etc/defaults/etc/skel/.bashrc"
fi

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

pathmunge /apps/jdk/bin
pathmunge /apps/jruby/bin
pathmunge /apps/maven/bin
pathmunge /apps/ant/bin

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
  cygpath -wlp "$@"
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
    \mv -vf $TRASHCAN ${TRASHCAN}.`date "+%N"`
  fi
	mkdir -p $TRASHCAN
  echo "Deleting to trash can: $TRASHCAN"
  \mv -f $* $TRASHCAN
}
export -f trash

function e() {
	/apps/jEdit/jedit.bat $(wpath "$@") &
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
alias eb='e ~/.bashrc'                 # edit bashrc file
alias ej='e /source/journals/`date "+%m%d%Y"`.txt'
alias rb='exec bash'                   # reload bashrc
alias f='find . -name'

# Longer Commands
alias openports='netstat -a | grep LISTENING'
alias printpath='echo $PATH | ruby -pe "gsub(/:/, \"\n\")"'
#alias xterm='xterm -display :0.0 -bd white -bg black -fg white -geometry 120x35 -sb -rightbar -sl 5000 -e bash --login &'
alias xterm='cmd /c `wpath /bin/rxvt` -bg black -fg white -geometry 120x35 -sl 2000 -sr -fn "Courier New-16" -e bash --login &'


###############################
## Subversion Helpers
###############################
function svnadd() {
  svn st | ruby -ane 'puts $F[1].gsub(/\\/, "/") if $F[0].strip=="?"' | xargs svn add
}
export -f svnadd
function svnrm() {
  svn st | ruby -ane 'puts $F[1].gsub(/\\/, "/") if $F[0].strip=="!"' | xargs svn rm
}
export -f svnrm
alias svnall='svnadd && svnrm'
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

###############################
## Java Development Helpers
###############################
alias lnto='ln -s $(pwd)'

# Open a javadoc file under java.lang package.
function jdoc {
  open "http://java.sun.com/javase/6/docs/api/java/lang/$1.html"
}
export jdoc

function mkcp() {
  export CP=`ruby -e 'puts ARGV.join(";")' $(wpath "$@")`
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
alias rjb='/jb/bin/run.bat -c'   # run jboss
alias rjbd='rjb default'         # run jboss with default server config
alias mkcpjbclient='mkcp target/classes "target/dependency/*" "/apps/jboss/client/*"'

