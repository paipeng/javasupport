#!/bin/env jruby
require 'java'
def grepjar(search, fn)
  if File.directory? fn
    Dir[fn+"/*.jar"].each { |subfn| grepjar(search, subfn) }
  else
    jar = java.util.jar.JarFile.new(fn)
    jar.entries.each do |entry|
      name = entry.name
      puts "#{fn}: #{name}" if name =~ search
    end
  end
end

search = ARGV.shift
ARGV.each do |name|
  grepjar(/#{search}/, name)
end
