# invoking Module.method and Module::method is the same
# invoking Class.method and Class::method is the same

module X
	def foo
		"foo"
	end
end
#puts X.new.foo # => undefined method `new' for X:Module (NoMethodError)
#puts X.foo     # => undefined method `foo' for X:Module (NoMethodError)
#puts X::foo    # => undefined method `foo' for X:Module (NoMethodError)

class Y 
	include X
end
puts Y.new.foo 
#puts Y.foo     
#puts Y::foo    

module X2
	def X2.foo
		"foo"
	end
end
puts X2.foo     
puts X2::foo   
class Y2
	#include X
	include X2
end
#puts Y2.new.foo 
#puts Y2.foo     
#puts Y2::foo
