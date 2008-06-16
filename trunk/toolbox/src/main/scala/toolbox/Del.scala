package toolbox
import toolbox.scalasupport._
import java.io.{ File, BufferedReader, InputStreamReader }
object Del extends CliApplication {   
  def main(argv: Array[String]){
    val (args, opts) = parseOptions(argv)            
    val dryrun = opts.getOrElse("dryrun", "false").toBoolean
    val debug = opts.getOrElse("debug", "false").toBoolean
    val stdin = opts.getOrElse("stdin", "false").toBoolean
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
    if(debug) println("args: " + args)
    
    if(opts.contains("-h"))
      exitWith(usage)
    
    //Process delete files  
    if(stdin){
      //read in stdin as each line equals a file name
      val br = new BufferedReader(new InputStreamReader(System.in))
      var ln = ""
      while({ ln = br.readLine; ln != null })
        delete(new File(ln.trim))
      br.close
    }else{
      if(args.size<=0) exitWith("Missing file argument.")
      args.foreach{ f => delete(new File(f)) }
    }
  }
  def usage = """
    |Program to delete files recursively.
    |Usage: scala Del [-n|-s|-d] file_or_dir [file_or_dir ...]
    |   -n dryrun only.
    |   -s read filename from STDIN one per line.
    |   -d turn debug mode ON.  
    """.stripMargin
    
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
