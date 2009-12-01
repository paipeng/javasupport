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
[INFO] Scanning for projects...
[INFO] Searching repository for plugin with prefix: 'archetype'.
[INFO] ------------------------------------------------------------------------
[INFO] Building Maven Default Project
[INFO]    task-segment: [archetype:generate] (aggregator-style)
[INFO] ------------------------------------------------------------------------
[INFO] Preparing archetype:generate
[INFO] No goals needed for project - skipping
[INFO] Setting property: classpath.resource.loader.class => 'org.codehaus.plexus.velocity.ContextClassLoaderResourceLoader'.
[INFO] Setting property: velocimacro.messages.on => 'false'.
[INFO] Setting property: resource.loader => 'classpath'.
[INFO] Setting property: resource.manager.logwhenfound => 'false'.
[INFO] [archetype:generate {execution: default-cli}]
[INFO] Generating project in Interactive mode
[INFO] No archetype defined. Using maven-archetype-quickstart (org.apache.maven.archetypes:maven-archetype-quickstart:1.0)
Choose archetype:
1: local -> simple-java-app-archetype (simple-java-app-archetype)
Choose a number:  (1): 1
Define value for groupId: : deng.my-first-java-app
Define value for artifactId: : my-first-java-app
Define value for version:  1.0-SNAPSHOT: :
Define value for package:  deng.my-first-java-app: : deng.myjavaapp
Confirm properties configuration:
groupId: deng.my-first-java-app
artifactId: my-first-java-app
version: 1.0-SNAPSHOT
package: deng.myjavaapp
 Y: : Y
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESSFUL
[INFO] ------------------------------------------------------------------------
[INFO] Total time: 49 seconds
[INFO] Finished at: Tue Dec 01 05:45:01 EST 2009
[INFO] Final Memory: 8M/14M
[INFO] ------------------------------------------------------------------------


Test your new project
=====================
$ cd my-first-java-app
$ mvn compile
$ mvn exec:java -Dexec.mainClass=deng.myfirstjavaapp.LoggerDemo
