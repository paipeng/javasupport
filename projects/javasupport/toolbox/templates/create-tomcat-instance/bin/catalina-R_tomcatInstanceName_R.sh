#!/bin/sh

echo "Tomcat Instance: ${tomcatInstanceName}"
echo "Http Port: ${tomcatInstance.httpPort}"
echo "Shutdown Port: ${tomcatInstance.shutdownPort}"
echo "IP Alias: ${tomcatHostIPAddress}"

SCRIPT_HOME=`cd $(dirname $0) && pwd`
export CATALINA_HOME=${tomcatPath}
export CATALINA_BASE=${tomcatPath}/${tomcatInstanceDirname}/${tomcatInstanceName}
export JAVA_OPTS="-Xmx128m"
$CATALINA_HOME/bin/catalina.sh $@

