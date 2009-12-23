=begin rdoc
$ cd /s
$ mvncpdp -f pom-active-camel.xml
$ mkcptarget
export CP=target\classes;target\dependency\*
$ ruby importJavaClass5.rb
target\classes
target\dependency\camel-core-2.1.0.jar
target\dependency\commons-logging-api-1.1.jar
target\dependency\commons-management-1.0.jar
d:\apps\jruby-1.4.0\lib\jruby.jar
d:\apps\jruby-1.4.0\lib\profile.jar
Java::OrgApacheCamel::CamelContext
Java::OrgApacheCamel::Exchange
Java::OrgApacheCamel::Processor
Java::OrgApacheCamel::ProducerTemplate
Java::OrgApacheCamelImpl::DefaultCamelContext
Class
=end

# Loading up all classes under a package into a module name.

require 'java'
java.lang.System.properties["java.class.path"].split(";").each {|e| puts e}

module Camel
  include_package 'org.apache.camel' 
end
#These are java interfaces, which visiable as Module in ruby
puts Camel::CamelContext.inspect
puts Camel::Exchange.inspect
puts Camel::Processor.inspect
puts Camel::ProducerTemplate.inspect

# A java concrete class is same as ruby class
module CamelImpl
  include_package 'org.apache.camel.impl' 
end
puts CamelImpl::DefaultCamelContext.inspect

