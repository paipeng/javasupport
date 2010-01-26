//$ scalac -d out actorthreadless.scala
//$ time scala -cp out actorthreadless 20 1000
//  Let's change the thread pool size to 20.
object actorthreadless2 {
  import scala.actors._
  case class Stop()
  class Actor1 extends Actor {
    def act() {
      Actor.loop {
        react {
          case msg : Stop => exit
          case msg : AnyRef => println(Thread.currentThread + " received " + msg + ", class=" + msg.getClass)
        }
      }
    }
  }
  def main(args : Array[String]) {
    System.setProperty("actors.corePoolSize", "20");
    System.setProperty("actors.maxPoolSize", "20");  
    
    val numOfActors = args(0).toInt
    val numOfMsg = args(1).toInt
    val actors = Array.fill(numOfActors) { new Actor1 }
        
    actors.foreach { actor => actor.start }    
    (1 to numOfMsg).foreach { msg => actors.foreach { actor => actor ! msg } }
    actors.foreach { actor => actor ! Stop() }
  }
}