import Utils._
def doWork() { }
val repeat = 3

(1 to repeat).foreach { i =>
  val n = 100000
  var i = 0
  val started = ts
  while (i < n) { 
    i += 1
    doWork()
  }
  val stopped = ts
  val elapse = stopped - started
  val rate = n / (elapse / 1000.0)
  printf("Mathed call rate %.2f\n", rate)
}
