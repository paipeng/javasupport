=begin rdoc
Similar to camelDemo2, but trying to use Proc or Block that act as Runnable interface.

* It doesn't work yet. :(
=end
require 'java'
include_class 'java.lang.Runtime'
include_class 'java.lang.Runnable'
include_class('java.lang.Thread') { |p,n| 'J' + n }
include_class 'org.apache.camel.impl.DefaultCamelContext'

class RunnableProc
  include Runnable
  def initialize &block
    @block = block
  end
  def run
    puts "Tesing"
    @block.call
  end
end

camelCtx = DefaultCamelContext.new
Runtime.getRuntime.addShutdownHook(JThread.new(RunnableProc.new { camelCtx.stop }))
camelCtx.start

# putting the main thread in wait mode
mainThread = JThread.currentThread
mainThread.synchronized { mainThread.wait }
