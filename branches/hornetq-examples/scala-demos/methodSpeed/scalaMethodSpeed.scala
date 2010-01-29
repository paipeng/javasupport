import Utils._
class Foo {
	def doWork() {}
}
val obj = new Foo
val repeat = 10
val n = if(args.length >= 1) args(0).toInt else 999999999
  
(1 to repeat).foreach { i =>
  var i = 0
  val started = ts
  while (i < n) { 
    i += 1
    obj.doWork
  }
  val stopped = ts
  val elapse = stopped - started
  val rate = n / (elapse / 1000.0)
  printf("Method call rate %.2f calls/sec\n", rate)
}
import Utils._
val doWork = ()=> {}
val repeat = 10
val n = if(args.length >= 1) args(0).toInt else 999999999
  
(1 to repeat).foreach { i =>
  var i = 0
  val started = ts
  while (i < n) { 
    i += 1
    doWork()
  }
  val stopped = ts
  val elapse = stopped - started
  val rate = n / (elapse / 1000.0)
  printf("Method call rate %.2f calls/sec\n", rate)
}
import Utils._

val repeat = 10
val n = if(args.length >= 1) args(0).toInt else 999999999
  
(1 to repeat).foreach { i =>
	def doWork() { }
  var i = 0
  val started = ts
  while (i < n) { 
    i += 1
    doWork()
  }
  val stopped = ts
  val elapse = stopped - started
  val rate = n / (elapse / 1000.0)
  printf("Method call rate %.2f calls/sec\n", rate)
}
import Utils._

def doWork() { }
val repeat = 10
val n = if(args.length >= 1) args(0).toInt else 999999999
  
(1 to repeat).foreach { i =>
  var i = 0
  val started = ts
  while (i < n) { 
    i += 1
    doWork()
  }
  val stopped = ts
  val elapse = stopped - started
  val rate = n / (elapse / 1000.0)
  printf("Method call rate %.2f calls/sec\n", rate)
}
