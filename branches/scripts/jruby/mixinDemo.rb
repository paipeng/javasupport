# Nested modules
module Ex1
	module Foo
	end	
	def self.test
		puts Ex1::Foo
	end
end

# Nested modules
module Ex2
	module Foo
		def Foo.m1
			"#{self} m1"
		end
		def m2
			"#{self} m2"
		end
	end
  class Bar
  	include Foo
  end
	def self.test
		#puts Bar.m1
		#puts Bar.m2
		#puts Bar.new.m1
		puts Bar.new.m2
	end
end

# Run a module example by user input
method = ARGV.shift || "Ex1"
if method !~ /\w+::test$/
	method += "::test"
end
#puts "Running #{method}"
eval method
