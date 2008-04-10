#!/bin/sh
SCRIPT_HOME=`dirname $0`
SCRIPT_PARENT_HOME=`cd $SCRIPT_HOME/.. && pwd`
scala -classpath "$SCRIPT_PARENT_HOME/target/classes" deng.toolbox.$@
