#!/bin/sh
SCRIPT_HOME=`dirname $0`
export JAVA_OPTS=-Dtoolhome=$SCRIPT_HOME/..
$SCRIPT_HOME/run.sh javasupport.toolbox.TemplateGenerator $@
