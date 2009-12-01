java-examples
=============
This project is used to test out all JDK library. There should not be any
dependency used.

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
$ mvn exec:java -Dexec.mainClass=deng.javaexamples.LoggerDemo

