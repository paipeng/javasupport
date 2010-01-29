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
export GUI_EDITOR=/apps/jEdit/jedit.bat

# Change propmt string and color
#PS1='\e[0;32m\u@\h:\e[0;33m\w\e[m\n\$ '

###############################
## PATH Setup
###############################

# Append path to before or after PATH without repeating.
function pathmunge {
	export PATH=`ruby -W0 -e '
	  p,b=ARGV
	  s=File::PATH_SEPARATOR
	  a=ENV["PATH"].split(s).uniq.delete_if{|x| x=="" || x==p}
	  if(b=="after")
	    a.push(p)
	  else 
	    a=[p] + a 
	  end
	  puts a.join(s)' "$@"`

}
export -f pathmunge

pathmunge /apps/jdk/bin # Make this path in front so it overwrite default Window's path version.
pathmunge /apps/ant/bin after
pathmunge /apps/maven/bin after
pathmunge /apps/groovy/bin after

###############################
## Shell Helper Functions
###############################

# Display information
#alias printopenports='netstat -a | grep LISTENING'
alias printpath='echo $PATH | ruby -pe "gsub(/:/, \"\n\")"'

# Create a timestamp label.
alias ts="date '+%m%d%Y-%H%M'"    

# Join each line on STDIN into a one long line.
alias joinlines='ruby -e "a=[]; sep=ARGV.shift||\" \"; while(gets); a<<\$_.chomp;end; puts a.join(sep)"'

#alias xterm='xterm -display :0.0 -bd white -bg black -fg white -geometry 120x35 -sb -rightbar -sl 5000 -e bash --login &'
alias xterm='cmd /c `wpath /bin/rxvt` -bg black -fg white -geometry 120x35 -sl 2000 -sr -fn "Courier New-16" -e bash --login &'

# ztool java utilities command
alias ztool='/source/javasupport/branches/ztool/bin/ztool'

# Create file link FROM TO
function link {
  ln -vfs $1 $2
}

# Trash and remove files into a trash directory in user home directory.
# Use this instead of rm command so you have history of deleted files.
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

# A graphical editor
function guiedit() {
  GUI_EDITOR=${GUI_EDITOR:=/cygdrive/c/WINDOWS/notepad.exe}
	$GUI_EDITOR $(wpath "$@")
}
export -f guiedit

# zip a directory recursively with the same name
function zipdir {
  \zip -qr $1.zip $1
}

# Backup a directory with timestamp.
function bak {
  for x in "$@"; do
    BAK=${x}.`ts`.bak
    \cp -rf $x $BAK
    echo "Backup $x to $BAK"
  done
}
export -f bak

# Backup a directory with timestamp and zip it.
function bakzip {
  for x in "$@"; do
    BAK=${x}.`ts`.bak
    cp -rf $x $BAK
    zipdir $BAK
    \rm -rf $BAK
    echo "Backup $x to $BAK.zip"
  done
}
export -f bakzip

# Backup a directory with timestamp and delete the original directory.
function bakd {
  for x in "$@"; do
    BAK=${x}.`ts`.bak
    \mv -vf $x $BAK
    echo "Backup and deleted $x to $BAK."
  done
}
export -f bakd

# Convert a cygwin unix path into Windows path.
function wpath {
	cygpath -wl -- "$@"
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

###############################
## Aliases For Shorter Commands
###############################

# Gui Editor
alias e=guiedit

# Some example alias instructions
# If these are enabled they will be used instead of any instructions
# they may mask.  For example, alias rm='rm -i' will mask the rm
# application.  To override the alias instruction use a \ before, ie
# \rm will call the real rm not the alias.

# Interactive operation...
#alias rm='rm -i'
#alias cp='cp -i'
#alias mv='mv -i'

# Overwrite rm to use trash function instead so we have a safe delete.
alias rm=trash

# Default to human readable figures
alias df='df -h'
alias du='du -h'

# Misc :)
alias less='less -r'                          # raw control characters
#alias whence='type -a'                        # where, of a sort
alias grep='grep --color'                     # show differences in colour

# Some shortcuts for different directory listings
#alias ls='ls -hF --color=tty'                 # classify files in colour
#alias dir='ls --color=auto --format=vertical'
#alias vdir='ls --color=auto --format=long'
#alias la='ls -A'                              # all but . and ..
alias ll='ls -l'                              # long list
alias l='ls -CF'                              #


# Very short and frequently used commands
alias eb='guiedit ~/.bashrc'                             # edit .bashrc file
alias ebx='guiedit ~/.bashrc-extra'                      # edit .bashrc-extra file
alias ej='guiedit /source/journals/`date "+%m%d%Y"`.txt' # edit today's journal file
alias rb='exec bash'                                     # reload bashrc
# a quick find
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
    system("svn add \"#{files.join(" ")}\"") unless files.size == 0
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
    system("svn remove \"#{files.join(" ")}\"") unless files.size == 0
  ' "$@"
}
export -f svnrm
function svnmove() {
  ruby -e '
    to_dir=ARGV.pop # last element
    ARGV.each { |n| system("svn move \"#{n}\" \"#{to_dir}\"") }
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

# Set java home if it's not already set. (eg: echo $CLASSPATH | unixpath )
export JAVA_HOME=${JAVA_HOME:=/apps/jdk}

# Convert Windows Classpath string into unix.
#alias unixpath='ruby -pe "gsub(/;/, \":\")" | ruby -pe "gsub(/\\\\/, \"/\")" | xargs cygpath -p'
function mkcpunix {
  CP=`echo $CLASSPATH | ruby -pe 'gsub(/;/, ":")' | ruby -pe 'gsub(/\\\\/, "/")' | xargs cygpath -p`
  echo "export CLASSPATH=$CP"
  export CLASSPATH=$CP
}

# Expand a wild card classpath to explicit full paths. (eg: mkcpjb; expandcp)
# this is good for tool such as groovy doesn't support wild card classpath yet!
function expandcp {
  CP=${CLASSPATH:$1}
  CP=$(ruby -e '
    puts ARGV[0].split(";").map { |e|
      files = `ls #{e.gsub(/\\/, "/")}`
      parts = files.split("\n")
      if (parts.grep(/.*\.jar$/).size > 0) 
        e = parts.join(";")
      end
      e
    }.join(";")
  ' $CP)
  echo "export CLASSPATH=$CP"
  export CLASSPATH=$CP
}

# Set Window's java home path
#function setwjhome() {
#	JAVA_HOME=`wpath $JAVA_HOME` # set JAVA_HOME to a Windows PATH.
#}
#export -f setwjhome

# Open a javadoc file under java.lang package.
function jdoc {
	CLS=`ruby -e 'c=ARGV[0]; if c.include?("."); puts c.gsub(/\./, "/") else puts "java/lang/#{c}" end' "$@"`
	JDOC=${JDOC:=http://java.sun.com/javase/6/docs/api}
  open "$JDOC/$CLS.html"
}
export -f jdoc

# Create a CLASSPATH string and export by it's variable in shell.
function mkcp() {
	#export CLASSPATH=`cygpath --path --windows "$@" | joinlines ';'`
  #export CLASSPATH=`ruby -e 'puts ARGV.join(";")' $(wpath "$@")`
  export CLASSPATH=$(ruby -e '
  	puts ARGV.map {|p| 
  		if p =~ /\*$/
  		  (`cygpath -w #{p[0..-2]}`).strip + "*"
  		else
  		  p
  		end
  	}.join(";")
  ' "$@")
  echo "export CLASSPATH=\"$CLASSPATH\""
}
export -f mkcp

# Convert / to . on given string. (eg: convert output of jar into full class name.)
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
alias rjbd='rjb -c default'              # run jboss with default server config
alias mkcpjb='mkcp "/apps/jboss/client/*" "/apps/jboss/common/lib/*" "target/dependency/*" target/classes ./'

###############################
## Allow User Custom Overwrite
###############################
if [ -e ~/.bashrc-extra ] ; then
  source ~/.bashrc-extra
fi
