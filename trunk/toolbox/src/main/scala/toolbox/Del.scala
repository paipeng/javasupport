package sayp.tool
import java.io.{ File, BufferedReader, InputStreamReader }
object Del {   
  def main(args: Array[String]){
    val (argList, options) = parseOptions(args)            
    val dryrun = options.getOrElse("dryrun", "false").toBoolean
    val debug = options.getOrElse("debug", "false").toBoolean
    val stdin = options.getOrElse("stdin", "false").toBoolean
    def delete(file: File){
      val label = if(dryrun) "Will be deleting" else "Deleting"
      if(file.isDirectory){
        file.listFiles.foreach{ child => delete(child) }
        println(label +" dir " + getPathname(file))
        if(!dryrun) file.delete()
      }else{
        println(label +" file " + getPathname(file))
        if(!dryrun) file.delete()
      }
    }
    if(debug) println("argList: " + argList)
    if(stdin){
      //read in stdin as each line equals a file name
      val br = new BufferedReader(new InputStreamReader(System.in))
      var ln = ""
      while({ ln = br.readLine; ln != null })
        delete(new File(ln.trim))
      br.close
    }else{
      if(argList.size<=0) printExitUsage
      argList.foreach{ f => delete(new File(f)) }
    }
  }
  def usage = """
    |Program to delete files recursively.
    |Usage: scala Del [-n|-s|-d] file_or_dir [file_or_dir ...]
    |   -n dryrun only.
    |   -s read filename from STDIN one per line.
    |   -d turn debug mode ON.  
    """.stripMargin
  def printExitUsage {
    println(usage)
    exit(1)
  }
  /** Parse commandline args and return immutable args list and options map. */
  def parseOptions(args: Array[String]): (List[String], Map[String, String]) = {
    import scala.collection.mutable.{ HashMap, ListBuffer } 
    val argList = new ListBuffer[String]   
    val options = new HashMap[String, String]
    for(a <- args) a match {
      case "-h" => printExitUsage
      case "-d" => options("debug") = "true"
      case "-n" => options("dryrun") = "true"
      case "-s" => options("stdin") = "true"
      case _ => argList.append(a)
    }
    (argList.toList, Map(options.toArray: _*))
  }  
  /** Get only the path upto where it first defined if not abosulte. */
  def getPathname(file: File): String = {
    if(file.isAbsolute) file.getAbsolutePath
    else{
      var f = file //init file path name
      val sb = new StringBuilder(f.getName)
      while(f.getParentFile != null){
        f = f.getParentFile
        sb.insert(0, f.getName+File.separator)
      }
      sb.toString
    }
  }
}
