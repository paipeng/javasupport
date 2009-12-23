=begin rdoc
Poll target/input directory for text files.
=end
require 'java'
include_class 'java.lang.Runtime'
include_class('java.lang.Thread') { |p,n| 'J' + n }
include_class('java.lang.String') { |p,n| 'J' + n }
include_class 'org.apache.camel.impl.DefaultCamelContext'
include_class 'org.apache.camel.builder.RouteBuilder'

routeBuilder = RouteBuilder.new
class << routeBuilder
  def configure
    from("file://./target/input").process do |exchange|
      msg = exchange.in
      #puts "Received exchange: #{msg.body.inspect}"
      #puts "Received exchange: #{msg.body.class.ancestors.join(",")}"
      #puts "Received exchange: #{msg.body}"
      puts "Received exchange: #{msg.getBody(JString.java_class)}"
    end
  end
end

camelCtx = DefaultCamelContext.new
Runtime.getRuntime.addShutdownHook(JThread.new { camelCtx.stop })
camelCtx.addRoutes(routeBuilder)
camelCtx.start

# putting the main thread in wait mode
mainThread = JThread.currentThread
mainThread.synchronized { mainThread.wait }
