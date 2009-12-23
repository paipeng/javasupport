require 'java'

## The built in implicit "main" object.
def ex0
  puts self
  puts self.class
  puts self.class.ancestors.join(", ")
end

# A new Ruby class and objects
module Ex1
  class Foo
    def info
      "My time is #{Time.new}" 
    end
  end
  
  #def Ex1.test
  def self.test
    puts Foo.inspect
    #puts Foo.class.inspect
    puts Foo.ancestors.join(", ") #Foo, Object, Kernel
    puts Foo.new.class
    puts Foo.new.info
  end
end

# Ruby and Java Object and class ancestors
module Ex2
  def self.test
    puts Object
    puts Object.ancestors.join(", ") # Object, Kernel
    puts Object.new
    puts Object.new
    
    puts java.lang.Object
    puts java.lang.Object.ancestors.join(", ") # Java::JavaLang::Object, ConcreteJavaProxy, JavaProxy, JavaProxyMethods, Object, Kernel
    puts java.lang.Object.new
    puts java.lang.Object.new
  end
end

# Ruby and Java String
module Ex3
  def self.test    
    puts "test".class
    puts java.lang.String.new("jtest").class
    
    puts "test".class.ancestors.join(", ")
    puts java.lang.String.new("jtest").class.ancestors.join(", ")
    
    s = "test"
    puts "id=#{s.object_id}, hash=#{s.hash}, symbol.object_id=#{s.to_sym.object_id}"
    s = "test"
    puts "id=#{s.object_id}, hash=#{s.hash}, symbol.object_id=#{s.to_sym.object_id}"
    s = "test"
    puts "id=#{s.object_id}, hash=#{s.hash}, symbol.object_id=#{s.to_sym.object_id}"
    
    s = java.lang.String.new("jtest")
    puts "id=#{s.object_id}, hash=#{s.hash}, hashCode=#{s.hashCode}, identityHashCode=#{java.lang.System.identityHashCode(s)}, identityHashCode(intern)=#{java.lang.System.identityHashCode(s.intern())}"
    s = java.lang.String.new("jtest")
    puts "id=#{s.object_id}, hash=#{s.hash}, hashCode=#{s.hashCode}, identityHashCode=#{java.lang.System.identityHashCode(s)}, identityHashCode(intern)=#{java.lang.System.identityHashCode(s.intern())}"
    s = java.lang.String.new("jtest")
    puts "id=#{s.object_id}, hash=#{s.hash}, hashCode=#{s.hashCode}, identityHashCode=#{java.lang.System.identityHashCode(s)}, identityHashCode(intern)=#{java.lang.System.identityHashCode(s.intern())}"
  end
end

# Ruby and Java Inheritance
module Ex4
  class Foo < Object
    def info
      "My time is #{Time.new}" 
    end
  end  
  class Foo2 < java.lang.Object
    def info
      "My time is #{Time.new}" 
    end
  end
  def self.test
    puts Foo
    puts Foo.ancestors.join(", ") # Object, Kernel
    puts Foo.new.inspect
    puts Foo.new.inspect
    
    puts Foo2
    puts Foo2.ancestors.join(", ") # Object, Kernel
    puts Foo2.new.inspect
    puts Foo2.new.inspect
    
    puts Foo.new.kind_of? Object
    puts Foo.new.kind_of? java.lang.Object 
    puts Foo2.new.kind_of? Object
    puts Foo2.new.kind_of? java.lang.Object
  end
end


# Ruby and Java anonymous classes.
# NOTE that Ruby syntax requires you to first create an instance of object
# before you can add modification!
module Ex5
  def self.test
    robj1 = Object.new {
      def data
        "Hello there."
      end
    }
    #puts rojb1.data        #This fails: data method doesn't exist.
    #puts robj1.send(:data) #This fails: data method doesn't exist.
    
    robj2 = Object.new
    class << robj2
      def data
        "Hello there."
      end
    end
    puts '--robj2--' * 3
    puts robj2.data
    
    robj3 = java.lang.Object.new
    class << robj3
      def data
        "Hello there."
      end
      #def to_s
      #  data
      #end
      def toString
        data
      end
    end
    puts '--robj3--' * 3
    puts robj3.data
    puts robj3.toString
    puts robj3.to_s  # Ruby string is not overwrite by Java oversion.
    puts "#{robj3}"
  end
end

# Ruby Module and Java Interfaces
module Ex6
  module RRunnable
    def run2
    end
  end  
  class Task
    include RRunnable
    include java.lang.Runnable
    def run
      "A Java interface method"
    end
    def run2
      "A Ruby interface/module method"
    end
  end
  def self.test
    t = Task.new
    puts t.inspect
    puts t.run
    puts t.run2    
    puts t.kind_of? Object
    puts t.kind_of? java.lang.Object  ## Wow: this is false!
    puts t.kind_of? RRunnable
    puts t.kind_of? java.lang.Runnable    
    puts t.class.ancestors.join(", ")
    
    t = java.lang.Object.new
    class << t      
      include RRunnable
      include java.lang.Runnable
      def run
        "A Java interface method"
      end
      def run2
        "A Ruby interface/module method"
      end
    end
    puts t.inspect
    puts t.run
    puts t.run2    
    puts t.kind_of? Object
    puts t.kind_of? java.lang.Object
    puts t.kind_of? RRunnable
    puts t.kind_of? java.lang.Runnable    
    puts t.class.ancestors.join(", ") # Hum... despite kind_of? test passed, but interface doesn't show up here?
  end
end

# Implementing Java interface like Java anonymous class style
module Ex7
  def self.testFailed
    task = Object.new
    class << task
      include java.lang.Runnable
      def run
        puts "A Java interface method"
      end
    end
    thread = java.lang.Thread.new(task)
    thread.start
    thread.join
  end
  def self.testFailed2
    task = java.lang.Object.new
    class << task
      include java.lang.Runnable
      def run
        puts "A Java interface method"
      end
    end
    thread = java.lang.Thread.new(task)
    thread.start
    thread.join
  end
  
  class Task
    include java.lang.Runnable
    def run
      puts "A Java interface method"
    end
  end
  def self.separatedTaskClassTest
    thread = java.lang.Thread.new(Task.new)
    thread.start
    thread.join
  end
  
  def self.test
    puts Task.ancestors.join(", ")  
    
    task2 = java.lang.Object.new
    class << task2
      include java.lang.Runnable
      def run
        puts "A Java interface method - with anonymous class definition"
      end
    end
    puts task2.class.ancestors.join(", ")
  end
end

module Ex7b
  class Task
    include java.lang.Runnable
    def run
      puts "A Java interface method"
    end
  end
  def self.test
    puts Task.ancestors.join(", ")

    task2 = java.lang.Object.new
    class << task2
      include java.lang.Runnable
      def run
        puts "A Java interface method - with anonymous class definition"
      end
    end
    puts task2.class.ancestors.join(", ")
  end
end

#Problematic usage in anonymous class implementation of a Java interface 
module Ex8
  def self.test
    task2 = java.lang.Object.new
    class << task2
      include java.lang.Runnable
      def run
        puts "A Java interface method - with anonymous class definition"
      end
    end    
    thread = java.lang.Thread.new(task2)
    thread.start
    thread.join
  end
end

#Using BLOCK conversion
module Ex9
  def self.test    
    thread = java.lang.Thread.new { puts "#{self} I am running." } # Hey this works!
    thread.start
    thread.join
  end
end

method = ARGV.shift || "Ex1::test"
eval method

