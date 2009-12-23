## CASE3: include_class works better than import!
=begin rdoc
 dengz1@ORLIW7MNDC91 /s $ jruby -I'd:\source\javasupport\branches\scripts\misc\myjava\target' importJavaClass3.rb
deng.myjava.Task@913dc1 Thread[main,5,main] I am running
=end
require 'java'
require 'myjava-1.0-SNAPSHOT.jar'
include_class "deng.myjava.Task"
task = Task.new
task.run

## CASE2: Only after added def deng method, it will work!
#=begin rdoc
# dengz1@ORLIW7MNDC91 /s $ jruby -I'd:\source\javasupport\branches\scripts\misc\myjava\target' importJavaClass3.rb
#deng.myjava.Task@913dc1 Thread[main,5,main] I am running
#=end
#require 'java'
#require 'myjava-1.0-SNAPSHOT.jar'
#def deng
#  Java::Deng
#end
#import deng.myjava.Task
#task = Task.new
#task.run


## CASE1: Will not work
#=begin rdoc
# dengz1@ORLIW7MNDC91 /s $ jruby importJavaClass3.rb
#importJavaClass3.rb:2:in `require': no such file to load -- myjava-1.0-SNAPSHOT (LoadError)
#        from importJavaClass3.rb:2
# dengz1@ORLIW7MNDC91 /s $ jruby -I'd:\source\javasupport\branches\scripts\misc\myjava\target' importJavaClass3.rb
#importJavaClass3.rb:3: undefined local variable or method `deng' for main:Object (NameError)
#
#* Only after added def deng method, it will work!
# dengz1@ORLIW7MNDC91 /s $ jruby -I'd:\source\javasupport\branches\scripts\misc\myjava\target' importJavaClass3.rb
#deng.myjava.Task@913dc1 Thread[main,5,main] I am running
#=end
#require 'java'
#require 'myjava-1.0-SNAPSHOT.jar'
#import deng.myjava.Task
#task = Task.new
#task.run

