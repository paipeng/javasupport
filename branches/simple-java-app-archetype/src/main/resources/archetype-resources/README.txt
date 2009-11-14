#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
${artifactId} project created by Zemian Deng

To developers with the sources you may build the project with Maven.

To compile
mvn compile

To generate ${artifactId} jar
mvn package

To generate javadoc
mvn javadoc:jar

To clean up and then create a binary distribution
mvn clean javadoc:jar assembly:assembly


