require 'java'
JThread = java.lang.Thread
JDate = java.util.Date
JFile = java.io.File

puts "#{JThread.currentThread.name}: Hello World."
puts "#{JThread.currentThread.name}: Datetime: #{JDate.new}"
puts "#{JThread.currentThread.name}: Current Directory: #{JFile.new(".").absolutePath}"
