require 'java'
props = java.lang.System.properties
names = props.keys.sort
names.each { |name| puts "#{name} = #{props[name]}" }

