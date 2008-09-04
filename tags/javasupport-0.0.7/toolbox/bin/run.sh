#!/bin/sh
SCRIPT_HOME=`dirname $0`
SCRIPT_PARENT_HOME=`cd $SCRIPT_HOME/.. && pwd`

# this is for java6 only.
# java $JAVA_OPTS -classpath "$JAVA_CLASSPATH:$SCRIPT_PARENT_HOME/lib/*" $@

# for java5, add all jar files from lib
for file in $SCRIPT_PARENT_HOME/lib/*.jar; do
  if [[ -f $file ]]; then CP=$CP:$file; fi
done

java $JAVA_OPTS -classpath "$JAVA_CLASSPATH:$CP" $@

