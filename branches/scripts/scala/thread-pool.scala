import java.util.concurrent._

def task(n : Int) = new Runnable{
  def run : Unit = { 
    println(Thread.currentThread.getName + " processing numbers.")
    //(0 to 360 by 15).map{ n => Math.cos(n) }
    Thread.sleep(n)
  }
}         

val threadPool = Executors.newFixedThreadPool(2)
threadPool.submit(task(300))
threadPool.submit(task(1000))
threadPool.submit(task(200))
threadPool.submit(task(3000))
threadPool.submit(task(300))

java.lang.Runtime.getRuntime.addShutdownHook(new Thread(){
  override def run : Unit = {
    println("Shutting down threadPool")
    threadPool.shutdownNow
  }
})

while(true)
  Thread.sleep(300)

