# importing core jdk classes

require 'java'

def ex1
  import java.lang.Thread # This will overshadown ruby's Thread class!
  puts Thread.currentThread
end
def ex1b
  # include_class is same as import
  include_class "java.lang.Thread" # This will overshadown ruby's Thread class!
  puts Thread.currentThread
end
def ex1c
  # include/import and change it's name!
  include_class("java.lang.Thread") { |pkg, name| "J" + name }
  puts JThread.currentThread
end
def ex1d
  # import multiple class names and rename it.
  include_class(["Thread", "Integer"].map{ |e| "java.lang." + e}) { |pkg, name| "J" + name }
  puts JThread.currentThread
  puts JInteger::MAX_VALUE
  puts JInteger::MIN_VALUE
end
module JLang
  include_package 'java.lang'
end
def ex1e
  puts JLang::Thread.currentThread
end

def ex2
  import java.util.logging.Logger
  log = Logger.getLogger("importJavaClass.rb")
  log.info("Just a test")
end

def ex3
  import javax.naming.InitialContext
  ctx = InitialContext.new
  puts ctx
  begin
    ctx.list("").each{ |it| puts it }
  rescue => detail
    #puts detail.inspect
    #puts detail.class.ancestors
    #puts detail.class.public_methods{ |it| puts it }
    puts detail.backtrace.join("\n")
    puts "Closing up context"
    ctx.close
  end
end

# Let user pick a method to run, or default ot ex1
method = ARGV.shift || "ex1"
send method

