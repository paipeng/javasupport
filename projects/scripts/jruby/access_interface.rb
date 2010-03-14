require 'java'

# a normal class that implements/include an interface
JThread = java.lang.Thread
class Task
	include java.lang.Runnable
	def run
		puts "Running."
	end
end
task = Task.new
puts task.kind_of? java.lang.Runnable

t = JThread.new(Task.new)
t.start

# A closure might able coerce into a Java interface. eg:
t2 = JThread.new{ puts "Running#2" }
t2.start

# Using a map and coerce into an interface
module InvokableHash
  def as(java_ifc)
	 java_ifc.impl { |name, *args| self[name].call(*args) }
  end
end
class Hash
  include InvokableHash
end
impl = {
  :i => 10,
  :hasNext => proc { impl[:i] > 0 },
  :next => proc { impl[:i] -= 1 }
}
iter = impl.as java.util.Iterator
puts(iter.kind_of? java.util.Iterator)
puts(iter.kind_of? java.lang.Runnable)
while(iter.hasNext)
	puts iter.next
end
