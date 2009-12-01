#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
${artifactId}
=========================
This is a simple Java 6 application with log4j framework configuration ready.

How to build it from source
===========================
To compile source
${symbol_dollar} cd ${artifactId}
${symbol_dollar} mvn compile

To generate a jar file
${symbol_dollar} mvn package

To generate javadoc
${symbol_dollar} mvn javadoc:jar

To clean up and then create a binary distribution package
${symbol_dollar} mvn clean javadoc:jar assembly:assembly

How to run it
=============
${symbol_dollar} cd ${artifactId}
${symbol_dollar} mvn compile
${symbol_dollar} mvn exec:java -Dexec.mainClass=${package}.LoggerDemo
