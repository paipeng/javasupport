require 'java'

import java.util.concurrent.Executors

class Task
  def initialize(taskDuration)
    @taskDuration = taskDuration
  end
  include java.lang.Runnable
  def run
    t = java.lang.Thread.currentThread
    puts "#{t} I am running. This will take #{@taskDuration}s."
    sleep(@taskDuration)
    puts "#{t} I am done."
  end
end

numOfTasks = (ARGV.shift || "4").to_i
numOfThreads = (ARGV.shift || "1").to_i
taskDuration = (ARGV.shift || "1").to_i # in seconds
puts "Execute #{numOfTasks} tasks with #{numOfThreads} threads. Each task duration is #{taskDuration}s."
threadPool = Executors.newFixedThreadPool(numOfThreads)
t1 = java.lang.System.currentTimeMillis
numOfTasks.times { threadPool.execute(Task.new(taskDuration)) }
threadPool.shutdown
while (!threadPool.isTerminated)
  #wait
end
t2 = java.lang.System.currentTimeMillis
puts "Done in #{(t2 - t1)/1000.0}s"

