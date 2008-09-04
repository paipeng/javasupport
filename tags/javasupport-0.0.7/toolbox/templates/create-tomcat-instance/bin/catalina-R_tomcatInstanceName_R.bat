@echo off

echo "Tomcat Instance: ${tomcatInstanceName}"
echo "Http Port: ${tomcatInstance.httpPort}"
echo "Shutdown Port: ${tomcatInstance.shutdownPort}"
echo "IP Alias: ${tomcatHostIPAddress}"

set SCRIPT_HOME=%~dp0
set CATALINA_HOME=${tomcatPath}
set CATALINA_BASE=${tomcatPath}\${tomcatInstanceDirname}\${tomcatInstanceName}
set JAVA_OPTS="-Xmx128m"
%CATALINA_HOME%\bin\catalina.bat %*

