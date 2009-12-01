#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
${artifactId}
=========================
This is a simple Java 6 application with log4j framework configuration ready.

How to build it from source
===========================
\$ mvn compile

How to run it
=============
\$ cd ${artifactId}
\$ mvn compile
\$ mvn exec:java -Dexec.mainClass=${package}.LoggerDemo

How to build it from source
===========================
To generate a jar file only
\$ mvn package

To generate javadoc
\$ mvn javadoc:jar

To clean up and then create a binary distribution package
\$ mvn clean javadoc:jar assembly:assembly

