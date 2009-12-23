=begin rdoc
=end
require 'java'
include_class 'org.apache.camel.impl.DefaultCamelContext'

class ShutdownTask
  include java.lang.Runnable
  def initialize(camelCtx)
    @camelCtx = camelCtx
  end
  def run
    @camelCtx.stop
  end
end

camelCtx = DefaultCamelContext.new
java.lang.Runtime.getRuntime.addShutdownHook(java.lang.Thread.new(ShutdownTask.new(camelCtx)))
camelCtx.start
sleep(10) # 10s, hit CTRL+C to shutdown
camelCtx.stop

