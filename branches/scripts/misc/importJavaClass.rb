# importing core jdk classes

require 'java'

def ex1
  #example1 : Overshadown ruby's Thread class!
  import java.lang.Thread
  puts Thread.currentThread
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

# Pick and run the main example here
def main
  ex3
end
main
