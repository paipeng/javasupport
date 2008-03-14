=====
ABOUT
=====
java-support:
  is a common library supporting all java projects development.

Sub projects:

maven-support:
  provide archetypes and other custom maven work for projects build and management.

jdk-support:
  is a common library supporting built-in java JDK library such as IO, Dates, System etc.
		
spring-support:
  is a common library supporting spring framework. Any reuseable controller, bean factory utilities etc.
	

===========
DEVELOPMENT
===========

To use NetBeans with maven plugin:
1) Open projects by browse to the directory. NetBeans supports maven project natively.

To build project on command line:
1) mvn package

===========================
MAVEN-ARCHETYPE USAGE NOTES
===========================
* If dash is used in artifactId, rename conf/*/application.properties database name with underscore.
  maven-archetype-springwebapp-hibernate will generate a config to connect to database, and 
  it defualt database name with artifactId. Default mysql won't allow dash.
	

==================
PERFORMING RELEASE
==================
Find and replace all version string in maven-support/bin/*.sh and *.bat files to release version.
svn commit -m "preparing scripts for release"
svn update .
mvn release:prepare
mvn release:perform
cd target/checkout
mvn assembly:assembly
* Upload zip file to http://code.google.com/p/javasupport

