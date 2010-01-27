package deng.myscalatool
object SvnRemove {
  def main(args:Array[String]):Unit={
    var workingDir = "."
    if(args.length > 0) workingDir = args(0)
    val statusCmd = "svn status " + workingDir
    var removeCmd = "svn remove "
    
    import collection.mutable.ArrayBuffer
    val removedFiles = new ArrayBuffer[String]
    exec(statusCmd){ output =>
      for(ln<-output.split("\n"))
        if(ln.startsWith("!")) removedFiles += ln.split(" ").last
    }
    println(removedFiles)
  }
  def exec(cmd:String)(func:(String)=>Unit):Unit={
    val lines = List("! test.txt", "? test2.txt")
    func(lines.mkString("\n"))
  }
}