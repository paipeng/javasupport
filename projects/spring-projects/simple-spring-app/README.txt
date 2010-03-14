simple-spring-app
=========================
This is a simple Java 6 application with log4j framework configuration ready.

How to build it from source
===========================
To compile source
$ cd simple-spring-app
$ mvn compile

To generate a jar file
$ mvn package

To generate javadoc
$ mvn javadoc:jar

To clean up and then create a binary distribution package
$ mvn clean javadoc:jar assembly:assembly

How to run it
=============
$ cd simple-spring-app
$ mvn compile
$ mvn exec:java -Dexec.mainClass=deng.simplespringapp.LoggerDemo

OR 
$ mvn dependency:copy-dependencies
$ CP='target\classes;target\dependency\*'
$ java -cp $CP deng.simplespringapp.LoggerDemo

More Tools
==========
$ # A service container runner program
$ java -cp $CP deng.simplespringapp.containerservices.ContainerRunner conf/service-container.xml


