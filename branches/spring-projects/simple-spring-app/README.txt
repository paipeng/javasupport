simple-spring-app
=========================
This is a simple Java 6 application with log4j framework configuration ready.

How to build it from source
===========================
To compile source
$ cd test2
$ mvn compile

To generate a jar file
$ mvn package

To generate javadoc
$ mvn javadoc:jar

To clean up and then create a binary distribution package
$ mvn clean javadoc:jar assembly:assembly

How to run it
=============
$ cd test2
$ mvn compile
$ mvn exec:java -Dexec.mainClass=deng.simplespringapp.LoggerDemo

