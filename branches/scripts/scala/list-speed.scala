Benchmark.timeThis("Warm up", 10){ n =>
  (1 to 1000).foldLeft(0){ _+_}
}
println()

Benchmark.timeThis("java.util.ArrayList", 10){ n =>
  import collection.jcl.Conversions._
  val ls = new java.util.ArrayList[Int]
  for(i <- (1 to 100000)){ ls.add(i) }
  val sum = ls.foldLeft(0){ (e, sum) => sum + e }
  println("ls sum " + sum)
}
println()

Benchmark.timeThis("scala.collection.mutable.ListBuffer", 10){ n =>
  val ls = new scala.collection.mutable.ListBuffer[Int]
  for(i <- (1 to 100000)){ ls += i }
  val sum = ls.foldLeft(0){ (e, sum) => sum + e }
  println("ls sum " + sum)
}
println()

Benchmark.timeThis("scala.collection.mutable.ArrayBuffer", 10){ n =>
  val ls = new scala.collection.mutable.ArrayBuffer[Int]
  for(i <- (1 to 100000)){ ls += i }
  val sum = ls.foldLeft(0){ (e, sum) => sum + e }
  println("ls sum " + sum)
}
println()

Benchmark.timeThis("scala.List", 10){ n =>
  var ls : List[Int] = Nil
  for(i <- (1 to 100000)){ ls = i :: ls }
  //val sum = ls.reverse.foldLeft(0){ (e, sum) => sum + e }
  val sum = ls.foldLeft(0){ (e, sum) => sum + e }
  println("ls sum " + sum)
}
println()

Benchmark.timeThis("Retrieving items by index", 10){ n =>
  //Takes too LONG!!!!
  //println(" ...using scalaList")
  //var ls : List[Int] = Nil
  //for(i <- (1 to 100000)){ ls = i :: ls }
    
  //Takes too LONG!!!
  //println(" ...using scala.collection.mutable.ListBuffer")
  //val ls = new scala.collection.mutable.ListBuffer[Int]
  //for(i <- (1 to 100000)){ ls += i }
  
  println(" ...using scala.collection.mutable.ArrayBuffer")
  val ls = new scala.collection.mutable.ArrayBuffer[Int]
  for(i <- (1 to 100000)){ ls += i }
      
  var sum = 0
  var i = 0
  while(i < ls.size){
    sum += ls(i)
    i += 1
  }
  println("ls sum " + sum)
}
println()

Benchmark.timeThis("Retrieving items by index using java.util.ArrayList", 10){ n =>
  val ls = new java.util.ArrayList[Int]
  for(i <- (1 to 100000)){ ls.add(i) }
    
  var sum = 0
  var i = 0
  while(i < ls.size){
    sum += ls.get(i)
    i += 1
  }
  println("ls sum " + sum)
}
println()
