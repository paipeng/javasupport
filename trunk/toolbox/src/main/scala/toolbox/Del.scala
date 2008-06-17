package toolbox
import toolbox.scalasupport._
import java.io.{ File, BufferedReader, InputStreamReader }
import RichFile._
object Del extends CliApplication {
  def usage = """
    |Delete all files and directories recursively with possible preview.
    |Usage: scala Del [-n|-s|-d] file_or_dir [file_or_dir ...]
    |   -n dryrun only.
    |   -s read filename from STDIN one per line.
    |   -d turn debug mode ON.  
    """.stripMargin   
    
  def main(argv: Array[String]){
    val (args, opts) = parseOptions(argv)     
    if(opts.contains("h")) exitWith(usage)
    
    val debug = opts.contains("d")
    if(debug) println("args: " + args)
    val dryrun = opts.contains("n")
    val stdin = opts.contains("s")
    
    //delete file method
    def delete(file: File){
      val label = if(dryrun) "Will be deleting" else "Deleting"
      file.walk{ f =>        
        println(label +" file " + f.getPathname)
        if(!dryrun) f.delete
      }
    }    
    
    //Process delete files  
    if(stdin){
      RichStream.eachLine(System.in){ ln => delete(new File(ln.trim)) }
    }else{
      if(args.size<=0) exitWith("Missing file argument.")
      args.foreach{ f => delete(new File(f)) }
    }
  }
}
