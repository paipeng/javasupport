#!/bin/sh
SCRIPT_HOME=`dirname $0`
SCRIPT_PARENT_HOME=`cd $SCRIPT_HOME/.. && pwd`
java $JAVA_OPTS -classpath "$JAVA_CLASSPATH:$SCRIPT_PARENT_HOME/lib/*" $@
