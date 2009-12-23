# How do I load jar without explicitly doing it in the source code with require statement?

=begin rdoc
http://java.sun.com/developer/technicalArticles/scripting/jruby/
You can call standard Java platform APIs, third-party Java platform APIs, 
or both from interactive Ruby (jirb) or a JRuby script by either putting the Java 
archive (JAR) files in the $RUBY_HOME/lib directory or modifying the CLASSPATH 
environment variable, and then using the require keyword in your program to include the libraries.

 dengz1@ORLIW7MNDC91 /s $ mkcptarget
export CP=target\classes;target\dependency\*
 dengz1@ORLIW7MNDC91 /s $ jruby importJavaClass4.rb
target\classes
target\dependency\camel-core-2.1.0.jar
target\dependency\commons-logging-api-1.1.jar
target\dependency\commons-management-1.0.jar
d:\apps\jruby-1.4.0\lib\jruby.jar
d:\apps\jruby-1.4.0\lib\profile.jar
CamelContext(camel-1)
=end

require 'java'
java.lang.System.properties["java.class.path"].split(";").each {|e| puts e}
include_class 'org.apache.camel.impl.DefaultCamelContext'
puts DefaultCamelContext.new
