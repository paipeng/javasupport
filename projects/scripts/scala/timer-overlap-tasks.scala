/*
Timer scheduler overlap time are ignore! This mean you can't depend on number of 
repetition to be excuted within certain amout of time!
*/
import java.util._
val task = new TimerTask {
  def run { 
    println(Thread.currentThread.getName + ": running task at " + new Date)
    Thread.sleep(1300) 
    println(Thread.currentThread.getName + ": done.")
  }
}
val timer = new Timer
timer.schedule(task, 0, 1000)
Thread.sleep(5000)
timer.cancel

