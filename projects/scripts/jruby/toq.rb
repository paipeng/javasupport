require 'java'
module Script
	include_package 'javax.jms'
	include_package 'javax.naming'	
	class ToQ
		def run
			puts "running"
		end
	end
end
Script::ToQ.new.run
