require 'java'

#load and all jars to classpath at runtime!
#Dir.glob('/my/path/to/*.jar').each{ |p| require p }

# Access any java class with full package name
puts "My jruby with Java access starts here.!"
puts "Java current thread name: #{java.lang.Thread.currentThread.name}" 
puts "A java date: #{java.util.Date.new}"
puts "A java File: #{java.io.File.new(".").absolutePath}"    
puts "-" * 50
	
#Using module style to import all(one-ruby-conflicted) names from java.
module Script
	include_package 'java.lang'
	include_package 'java.util'
	include_package 'java.io'	
	
	import 'java.lang.Thread'
	import 'java.io.File'
	
	puts "My jruby with Java access starts here.!"
	puts "Java current thread name: #{Thread.currentThread.name}" 
	puts "A java date: #{Date.new}"
	puts "A java File: #{File.new(".").absolutePath}"                                         
end  
puts "-" * 50

# Method#2 - GOOD style!: Using module style to import all(one-ruby-conflicted) names from java.
module J
	include_package 'java.lang'
	include_package 'java.util'
	include_package 'java.io'                     
end  
puts "My jruby with Java access starts here.!"
puts "Java current thread name: #{J::Thread.currentThread.name}" 
puts "A java date: #{J::Date.new}"
puts "A java File: #{J::File.new(".").absolutePath}"    
puts "-" * 50


## Bad way to do it: Access java class directly(trump ruby class name), You get warnings!
#import 'java.lang.Thread'
#import 'java.util.Date'
#import 'java.io.File'	
#puts "My jruby with Java access starts here.!"
#puts "Java current thread name: #{Thread.currentThread.name}" 
#puts "A java date: #{Date.new}"
#puts "A java File: #{File.new(".").absolutePath}"  
#puts "-" * 50


# Access any java class with alias name - GOOD style!
JThread = java.lang.Thread
JDate = java.util.Date
JFile = java.io.File
puts "My jruby with Java access starts here.!"
puts "Java current thread name: #{JThread.currentThread.name}" 
puts "A java date: #{JDate.new}"
puts "A java File: #{JFile.new(".").absolutePath}"  
puts "-" * 50
