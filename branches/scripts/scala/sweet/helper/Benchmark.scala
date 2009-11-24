package sweet.helper

/**
Benchmark java/scala code that produce CPU, System, User and Real times.

<pre>
//Simple use case is:
Benchmark.measure{ var sum = 0; for(i <- 1 to 100){ sum += i } }

//Measure and compare codes:
Benchmark.bm{ bm =>
  bm.measure("for-comp:"){ var sum = 0; for(i <- 1 to 100){ sum += i } }
  bm.measure("foreach:"){ var sum = 0; (1 to 100).foreach{ i => sum += i } }
  bm.printtotal
  bm.printavg
}

//Same as above, but run the benmark once as warm up first
Benchmark.bmWarm{ bm =>
  bm.measure("for-comp:"){ var sum = 0; for(i <- 1 to 100){ sum += i } }
  bm.measure("foreach:"){ var sum = 0; (1 to 100).foreach{ i => sum += i } }
  bm.printtotal
  bm.printavg
}
</pre>
*/
object Benchmark{
  val defaultLabelWidth = 15
  val defaultTimeWidth = 10
  
  /** Measure a single task and print result. */
  def measure(task: => Unit): Unit = {
    val noLabelWidth = 1
    new Benchmark(noLabelWidth, defaultTimeWidth).printheader.measure(task)
  }
  
  /** Create a default Benchmark object, print header, then call custom func. */
  def bm(func: Benchmark => Unit) = {
    val bm = new Benchmark(defaultLabelWidth, defaultTimeWidth)
    bm.printheader
    func(bm)
  }
  
  /** Run the func once first to warm up JVM before actual run. */
  def bmWarm(func: Benchmark => Unit) = {
    val warmer = new Benchmark(defaultLabelWidth, defaultTimeWidth)
    println("=== Warming up JVM ===")
    warmer.printheader
    func(warmer)
    warmer.printtotal
    warmer.printavg
    println("=== End of Warming JVM ===")
    
    val bm = new Benchmark(defaultLabelWidth, defaultTimeWidth)
    bm.printheader
    func(bm)
  }
  
  /** Stub for JDK1.4 since it doesn't have the threadbean.
  val threadbean = new Object {
    def getCurrentThreadCpuTime = realTime * 1000
    def getCurrentThreadUserTime = realTime * 1000  
  }
  */
  val threadbean = java.lang.management.ManagementFactory.getThreadMXBean
  
  /** in nano seconds */
  //def realTime = System.currentTimeMillis * 1000
  def realTime = System.nanoTime
  /** in nano seconds */
  def cpuTime = threadbean.getCurrentThreadCpuTime
  /** in nano seconds */
  def userTime = threadbean.getCurrentThreadUserTime
  /** in nano seconds */
  def sysTime = userTime - cpuTime
}

class Benchmark(labelWidth: Int, timeWidth: Int) {
  import collection.jcl.Conversions._
  import Benchmark.{ realTime, cpuTime, sysTime }
  
  /** Hold all the times generated in measure method */
  private var totalTimes = new java.util.ArrayList[List[Long]]
 
  def printheader: Benchmark = {
    print(" " * labelWidth)
    val timefmt = ("%" + timeWidth + "s") * 4
    println(timefmt.format("CPU", "System", "User", "Real"))
    this
  }
  
  private def printtime(label: String, times : List[Long]): Unit = {
    val List(cpu, sys, user, real) = times
    val cpusec = "%.3fs".format(cpu / 1000000.0)
    val syssec = "%.3fs".format(sys / 1000000.0)
    val usersec = "%.3fs".format(user / 1000000.0)
    val realsec = "%.3fs".format(real / 1000000.0)
    
    val labelfmt = "%-" + labelWidth + "s"
    val timefmt = ("%" + timeWidth + "s") * 4
    val out = (labelfmt + timefmt).format(label, cpusec, syssec, usersec, realsec)
    println(out)
  }
  
  def printtotal: Benchmark = {
    val times = (0 until 4).map{ i => 
      totalTimes.foldRight(0L){ (elem, sum) => sum + elem(i) }
    }.toList
    printtime("total:", times)
    this
  }
  
  def printavg: Benchmark = {
    val count = totalTimes.size
    val times = (0 until 4).map{ i => 
      totalTimes.foldRight(0L){ (elem, sum) => sum + elem(i) } / count
    }.toList
    printtime("average:", times)
    this
  }
  
  def measure(task: => Unit): Unit = measure("")(task)
  
  def measure(label: String)(task: => Unit) : Unit = {  
    val r1 = realTime
    val s1 = sysTime
    val c1 = cpuTime
    
    task
    
    val cpu = cpuTime - c1
    val sys = sysTime - s1
    val user = cpu + sys
    val real = realTime - r1
  
    totalTimes.add(List(cpu, sys, user, real))
    printtime(label, List(cpu, sys, user, real))
  }
}

/** Test
Benchmark.bmWarm{ bm =>
  bm.measure("while:"){ var sum = 0; var i = 0; while(i < 100){ sum += i; i+=1 } }
  bm.measure("foreach:"){ var sum = 0; (1 to 100).foreach{ i => sum += i } }
  bm.measure("foldRight:"){ val sum = (1 to 100).foldRight(0)(_+_); () }
  bm.measure("for-comp:"){ var sum = 0; for(i <- 1 to 100){ sum += i } }
  bm.printtotal
  bm.printavg
}
*/

