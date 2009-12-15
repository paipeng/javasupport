simple-java-app-archetype
=========================
This maven archetype will generate a simple Java 6 ready application with log4j
framework configuration ready.

How to generating a new project
===============================
$ cd simple-java-app-archetype
$ mvn install
$ cd ..
$ mvn archetype:generate -DarchetypeCatalog=local

How to run non-interactively
============================
$ NAME=test-1
$ PKG=`echo $NAME | ruby -pe "gsub(/[^[:alnum:]]/, '')"`
$ mvn archetype:generate -DarchetypeCatalog=local -DinteractiveMode=false \
  -DarchetypeGroupId=deng.archetypes -DarchetypeArtifactId=simple-java-app-archetype -DarchetypeVersion=1.0-SNAPSHOT \
  -DgroupId=deng.$NAME -DartifactId=$NAME -Dpackage=deng.$PKG



Test your new project
=====================
$ cd my-first-java-app
$ mvn compile
$ mvn exec:java -Dexec.mainClass=deng.myfirstjavaapp.LoggerDemo -Djava.util.logging.config.file=src/main/resources/logging.properties
