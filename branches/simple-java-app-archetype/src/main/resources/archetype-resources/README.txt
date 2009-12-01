#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
${artifactId}
=========================
This is a simple Java 6 application.

How to build it from source
===========================
To compile source
${symbol_dollar} cd ${artifactId}
${symbol_dollar} mvn compile

To generate a jar file
${symbol_dollar} mvn package

How to run it
=============
${symbol_dollar} cd ${artifactId}
${symbol_dollar} mvn compile
${symbol_dollar} mvn exec:java -Dexec.mainClass=${package}.LoggerDemo -Djava.util.logging.config.file=src/main/resources/logging.properties

OR if you have quiet a few dependencies and want to run standalone java, try:
${symbol_dollar} mvn dependency:copy-dependencies
${symbol_dollar} CP='target/classes;target/dependency/*'
${symbol_dollar} java -cp $CP deng.simplespringapp.LoggerDemo

