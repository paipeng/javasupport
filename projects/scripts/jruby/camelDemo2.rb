=begin rdoc
This script act as server that starts Camel context.
=end
require 'java'
include_class 'java.lang.Runtime'
include_class 'java.lang.Runnable'
include_class('java.lang.Thread') { |p,n| 'J' + n }
include_class 'org.apache.camel.impl.DefaultCamelContext'

class ShutdownTask
  include Runnable
  def initialize(camelCtx)
    @camelCtx = camelCtx
  end
  def run
    @camelCtx.stop
  end
end

camelCtx = DefaultCamelContext.new
Runtime.getRuntime.addShutdownHook(JThread.new(ShutdownTask.new(camelCtx)))
camelCtx.start

# putting the main thread in wait mode
mainThread = JThread.currentThread
mainThread.synchronized { mainThread.wait }
