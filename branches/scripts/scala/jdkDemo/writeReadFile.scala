  import java.io._
  val out = new BufferedWriter(new FileWriter("C:\\test.txt"))
  try{
    (1 to 99).foreach{ i => out.write("test " + i + "\n") }
  }finally{
    out.close
  }
  
  import scala.io.Source
  val lines = Source.fromFile("C:\\test.txt").getLines
  lines.foreach{ line =>
    print(line)
  }
