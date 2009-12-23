=begin rdoc
$ cd /s
$ mvncpdp -f pom-active-camel.xml
$ mkcptarget
export CP=target\classes;target\dependency\*
$ jruby camelDemo.rb
Dec 23, 2009 11:48:13 AM org.apache.camel.impl.DefaultCamelContext doStart
INFO: Apache Camel 2.1.0 (CamelContext:camel-1) is starting
Dec 23, 2009 11:48:13 AM org.apache.camel.impl.DefaultCamelContext createManagementStrategy
INFO: JMX enabled. Using DefaultManagedLifecycleStrategy.
Dec 23, 2009 11:48:13 AM org.apache.camel.impl.DefaultCamelContext createManagementStrategy
WARNING: Could not find needed classes for JMX lifecycle strategy. Needed class is in spring-context.jar using Spring 2.
5 or newer ( spring-jmx.jar using Spring 2.0.x). NoClassDefFoundError: org/springframework/jmx/export/metadata/JmxAttrib
uteSource
Dec 23, 2009 11:48:13 AM org.apache.camel.impl.DefaultCamelContext createManagementStrategy
WARNING: Cannot use JMX. Fallback to using DefaultManagementStrategy.
Dec 23, 2009 11:48:13 AM org.apache.camel.impl.DefaultCamelContext start
INFO: Apache Camel 2.1.0 (CamelContext:camel-1) started
Dec 23, 2009 11:48:14 AM org.apache.camel.impl.DefaultCamelContext doStop
INFO: Apache Camel 2.1.0 (CamelContext:camel-1) is stopping
Dec 23, 2009 11:48:14 AM org.apache.camel.impl.DefaultInflightRepository doStop
INFO: Shutting down with no inflight exchanges.
Dec 23, 2009 11:48:14 AM org.apache.camel.impl.DefaultCamelContext doStop
INFO: Apache Camel 2.1.0 (CamelContext:camel-1) stopped
=end

require 'java'
include_class 'org.apache.camel.impl.DefaultCamelContext'
camelCtx = DefaultCamelContext.new
camelCtx.start
sleep(1)
camelCtx.stop

