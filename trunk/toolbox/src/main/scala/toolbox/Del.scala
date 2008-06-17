package toolbox
import toolbox.scalasupport._
import java.io.{ File, BufferedReader, InputStreamReader }
import RichFile._
object Del extends CliApplication {   
  def main(argv: Array[String]){
    val (args, opts) = parseOptions(argv)            
    val dryrun = opts.getOrElse("dryrun", "false").toBoolean
    val debug = opts.getOrElse("debug", "false").toBoolean
    val stdin = opts.getOrElse("stdin", "false").toBoolean
    def delete(file: File){
      val label = if(dryrun) "Will be deleting" else "Deleting"
      file.walk{ f =>        
        println(label +" file " + f.getPathname)
        if(!dryrun) f.delete
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
    |Delete all files and directories recursively with possible preview.
    |Usage: scala Del [-n|-s|-d] file_or_dir [file_or_dir ...]
    |   -n dryrun only.
    |   -s read filename from STDIN one per line.
    |   -d turn debug mode ON.  
    """.stripMargin
}
