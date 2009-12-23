=begin rdoc
This script act as server that starts Camel context.
=end
require 'java'
include_class 'java.lang.Runtime'
include_class 'java.lang.Runnable'
include_class('java.lang.Thread') { |p,n| 'J' + n }
include_class('java.lang.String') { |p,n| 'J' + n }
include_class 'org.apache.camel.impl.DefaultCamelContext'
include_class 'org.apache.camel.builder.RouteBuilder'
include_class 'org.apache.camel.Exchange'
include_class 'org.apache.camel.Processor'

class ShutdownTask
  include Runnable
  def initialize(camelCtx)
    @camelCtx = camelCtx
  end
  def run
    @camelCtx.stop
  end
end

class Processor1
  include Processor
  def process(exchange)
    msg = exchange.in
    #puts "Received exchange: #{msg.body.inspect}"
    #puts "Received exchange: #{msg.body.class.ancestors.join(",")}"
    #puts "Received exchange: #{msg.body}"
    puts "Received exchange: #{msg.getBody(JString.java_class)}"
  end
end
class RouteBuilder1 < RouteBuilder
  def configure
    from("file://./target/input").process(Processor1.new)
  end
end
camelCtx = DefaultCamelContext.new
Runtime.getRuntime.addShutdownHook(JThread.new(ShutdownTask.new(camelCtx)))
camelCtx.addRoutes(RouteBuilder1.new)
camelCtx.start

# putting the main thread in wait mode
mainThread = JThread.currentThread
mainThread.synchronized { mainThread.wait }
