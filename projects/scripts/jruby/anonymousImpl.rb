require 'java'

module Ex2b
	def self.deng
		Java::Deng
	end
	def self.run
		# Ruby's anonymous class implementation of an java interface
		# NOTE that the anonymous class is created using Ruby's Class.new, 
		#      and you need to create an new instance at the end!
		
		service = Class.new(java.lang.Object) {
			#include deng.myjava.Service         # ??? Why can't I use deng.myjava.Service
			                                     # I get this error: anonymousImpl.rb:26:in `run': undefined local variable or method `deng' for #<Class:01x19518cc> (NameError)

			include Java::DengMyjava::Service
			def init
				puts "initializing #{java.lang.Thread.currentThread} - #{self}"				
			end
			def destroy
				puts "destroying #{java.lang.Thread.currentThread} - #{self}"
			end
			def run
				puts "hello #{java.lang.Thread.currentThread} - #{self}"
			end
		}.new		
		deng.myjava.Utils.runTaskService(service)
		puts "=" * 25
		
		service = Class.new(java.lang.Object) {
			include java.lang.Runnable
			def run
				puts "hello #{java.lang.Thread.currentThread} - #{self}"
			end
		}.new		
		deng.myjava.Utils.runTaskService(service)
		puts "=" * 25
		
		service = Class.new(deng.myjava.AbstractService) {
			def run
				puts "hello #{java.lang.Thread.currentThread} - #{self}"
			end
		}.new		
		deng.myjava.Utils.runTaskService(service)
		puts "=" * 25
		
		# Hum... this is not calling the right overloaded method.
		service = Class.new(deng.myjava.TaskService) {
			def run
				puts "hello #{java.lang.Thread.currentThread} - #{self}"
			end
		}.new		
		deng.myjava.Utils.runTaskService(service)
		puts "=" * 25
		
		# Now it works
		service = deng.myjava.TaskService.new		
		deng.myjava.Utils.runTaskService(service)
		puts "=" * 25
	end
end

module Ex2
	def self.run
		# Ruby's anonymous class implementation of an java interface
		# NOTE that the anonymous class is created using Ruby's Class.new, 
		#      and you need to create an new instance at the end! 
		task = Class.new(java.lang.Object) {
			include java.lang.Runnable
			def run
				puts "hello #{java.lang.Thread.currentThread} - #{self}"
			end
		}.new
		thread = java.lang.Thread.new(task)
		thread.start
		thread.join
	end
end

module Ex1c
	def self.deng
		Java::Deng
	end
	def self.run
		# Jruby magic Proc/Block to Java Interface conversion.
		deng.myjava.Utils.runTask { puts "hello #{java.lang.Thread.currentThread} - #{self}" }
	end
end

module Ex1b
	def self.run
		# Jruby magic Proc/Block to Java Interface conversion.
		include_class 'deng.myjava.Utils'
		Utils.runTask { puts "hello #{java.lang.Thread.currentThread} - #{self}" }
		
		# Calling java's overloaded methods with different interface parameter.
		Utils.runTaskService { puts "hello #{java.lang.Thread.currentThread} - #{self}" } # => Invoked the Service param
	end
end

module Ex1
	def self.run
		# Jruby magic Proc/Block to Java Interface conversion.
		thread = java.lang.Thread.new { puts "hello #{java.lang.Thread.currentThread} - #{self}" }
		thread.start
		thread.join
	end
end

eval(ARGV.shift || "Ex1::run")
