require 'java'
puts Kernel.methods.grep /java/
puts '-' * 10
puts Module#ancestors  
puts '-' * 10
puts ObjectSpace::each_object.to_a.join(", ")
puts '-' * 10

__END__

dengz1@ORLIW7MNDC91 /s $ jruby namespace.rb 
java_kind_of?
java
javax

