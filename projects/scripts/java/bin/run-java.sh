#!/bin/bash

#
#
# Start and run a generic java/JVM process with given main class as 
# argument. Caller may customize JAVA_HOME, JAVA_OPTS and JAVA_CLASSPATH
# variables that would drive this script, else it default to some simple
# settings.
#
# Author: Zemian Deng 05/6/2009
#

# Set the JAVA_APP_HOME var (not where wrapper script is, but the app home)
JAVA_APP_HOME=`dirname $0`
JAVA_APP_HOME=`cd $JAVA_APP_HOME/../ && pwd`

# Set java options
if [[ -z "${JAVA_OPTS}" ]] ; then
  JAVA_OPTS="-Xmx128m -Djava.app.home=$JAVA_APP_HOME"
fi

# Set java classpath
if [[ -z "${JAVA_CLASSPATH}" ]] ; then
  JAVA_CLASSPATH=".:$JAVA_APP_HOME/lib/*:$JAVA_APP_HOME/target/classes"
fi

# set Java command executable
JAVA_CMD=java
if [[ -n "${JAVA_HOME}" ]] ; then
  JAVA_CMD=$JAVA_HOME/bin/java	
fi

# Invoke JVM process with a Main class
$JAVA_CMD $JAVA_OPTS -classpath "$JAVA_CLASSPATH" "$@"

