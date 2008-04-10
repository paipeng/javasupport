#!/bin/sh

SCRIPT_HOME=`dirname $0`
SCRIPT_PARENT_HOME=`cd $SCRIPT_HOME/.. && pwd`

groovy -classpath $SCRIPT_PARENT_HOME/src/main/resources $SCRIPT_PARENT_HOME/src/main/groovy/GenerateSpringMvc.groovy $@

