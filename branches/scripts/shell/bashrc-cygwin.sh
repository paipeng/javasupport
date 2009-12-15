export PATH=$PATH:/apps/groovy-1.6.7/bin

function wpath { 
  cygpath -wl $(ruby -e "puts File.expand_path(ARGV[0])" $1) 
}
export wpath
function open {
  explorer $(wpath $1)
}
export open

alias ll='ls -lA'
alias findx='find . -name'
alias mvngen='mvn archetype:generate -DarchetypeCatalog=local'

function mvngenjava {
  PREF=deng
  PKG=$(echo $1 | ruby -pe "gsub(/[^[:alnum:]]/, '')")
  CMD="mvn archetype:generate -DinteractiveMode=false \
  -DarchetypeCatalog=local -DarchetypeArtifactId=simple-java-app-archetype -DarchetypeGroupId=deng.archetype \
  -DgroupId=$PREF.$1 -DartifactId=$1 -Dpackage=$PREF.$PKG"
  echo $CMD
  $CMD
}
export mvngenjava

