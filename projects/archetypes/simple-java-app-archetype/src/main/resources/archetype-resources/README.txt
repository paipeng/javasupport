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
${symbol_dollar} mvn exec:java -Dexec.mainClass=${package}.LoggerDemo

OR if you have quiet a few dependencies and want to run standalone java, try:
${symbol_dollar} mvn dependency:copy-dependencies
${symbol_dollar} CP='target/classes;target/dependency/*'
${symbol_dollar} java -cp ${symbol_dollar}CP deng.simplespringapp.LoggerDemo

How to enable JUL logging
=========================
Run the Main class with this sys prop set:
${symbol_dollar} mvn compile
${symbol_dollar} mvn exec:java -Dexec.mainClass=${package}.LoggerDemo -Djava.util.logging.config.file=conf/logging.properties

The content of logging.properties can be created as follow:
${symbol_dollar} mkdir conf
${symbol_dollar} echo '
handlers = java.util.logging.ConsoleHandler
java.util.logging.ConsoleHandler.level=ALL
java.util.logging.ConsoleHandler.formatter=java.util.logging.SimpleFormatter
.level=SEVERE
${package}.level=FINEST
' > conf/logging.properties
