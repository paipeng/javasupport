class A
	def foo
		"foo"
	end
end
a = A.new
puts a
#puts a.new
#puts a.initialize

require 'java'

a = Java::JavaBean.new
puts a
#puts a.new
puts a.initialize

a = Java::JavaWithInitialize.new
puts a
puts a.initialize        #It's not calling java's initialized method!
puts a.send(:initialize) #It's not calling java's initialized method!
java_method = Java::JavaWithInitialize.java_class.declared_method(:initialize)
puts java_method.invoke(a.java_object) # Finally, it gets called!


