require 'java'
module J
	include_package 'java.lang'
	include_package 'java.util'
	include_package 'java.io'
	
	#load all jars for parser
	Dir.glob('D:/opt/apache-ant-1.7.1/lib/*.jar').each do |p| require p end    
	
	# expose more classes
	include_package "org.apache.tools.ant"
end

# usage example
J::Main.main(["-v"].to_java(:string))