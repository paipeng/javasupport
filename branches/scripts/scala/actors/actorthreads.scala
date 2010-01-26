//$ scalac -d out actorthreads.scala
//$ time scala -cp out actorthreads 20 1000
//  this example creates 20 instances of actors, and uses 20 threads for processing.
object actorthreads {
  import scala.actors._
  case class Stop()
  class Actor1 extends Actor {
    def act() {
      while(true) {
        receive {
          case msg : Stop => exit
          case msg : AnyRef => println(Thread.currentThread + " received " + msg + ", class=" + msg.getClass)
        }
      }
    }
  }
  def main(args : Array[String]) {
    val numOfActors = args(0).toInt
    val numOfMsg = args(1).toInt
    val actors = Array.fill(numOfActors) { new Actor1 }
    actors.foreach { actor => actor.start }    
    (1 to numOfMsg).foreach { msg => actors.foreach { actor => actor ! msg } }
    actors.foreach { actor => actor ! Stop() }    
  }
}