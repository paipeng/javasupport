=begin rdoc
Poll target/input directory for text files.

NOTE: 
* You can't routeBuilder = RouteBuilder.new because RouteBuilder is abstract, and
ruby exception doesn't say so!
* CTRL+C will only work if you let the server run at least few millis seconds?
  I think this has to do with the CamelContext stop is not being implemented propertly.

=end
require 'java'
include_class 'java.lang.Runtime'
include_class('java.lang.Thread') { |p,n| 'J' + n }
include_class('java.lang.String') { |p,n| 'J' + n }
include_class 'org.apache.camel.impl.DefaultCamelContext'
include_class 'org.apache.camel.builder.RouteBuilder'

builder = Class.new(RouteBuilder) {
  def configure
    from("file://./target/input").process do |exchange|
      msg = exchange.in
      #puts "Received exchange: #{msg.body.inspect}"
      #puts "Received exchange: #{msg.body.class.ancestors.join(",")}"
      #puts "Received exchange: #{msg.body}"
      puts "Received exchange: #{msg.getBody(JString.java_class)}"
    end
  end
}.new

camelCtx = DefaultCamelContext.new
camelCtx.addRoutes(builder)

Runtime.getRuntime.addShutdownHook(JThread.new { camelCtx.stop })
camelCtx.start

# putting the main thread in wait mode
mainThread = JThread.currentThread
mainThread.synchronized { mainThread.wait }
