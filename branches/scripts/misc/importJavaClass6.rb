require 'java'

module Ex1
	def self.deng
		Java::Deng
	end
	def self.run
		# Jruby magic Proc/Block to Java Interface conversion.
		deng.myjava.Utils.runTask { puts "hello #{java.lang.Thread.currentThread} - #{self}" }
	end
end

eval(ARGV.shift || "Ex1::run")
