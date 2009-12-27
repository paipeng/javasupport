require 'java'

module Ex1
	def self.run
		thread = java.lang.Thread.new { puts "Shutting down nicely. #{java.lang.Thread.currentThread} - #{self}" }
		java.lang.Runtime.runtime.addShutdownHook(thread)
		
		puts "Running. CTRL+C to shutdown."
		lock = java.lang.Object.new
		lock.synchronized { lock.wait }
	end
end

eval(ARGV.shift || "Ex1::run")
