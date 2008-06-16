package toolbox
import toolbox.scalasupport._
import RichFile._
object Grep extends CliApplication{
  def main(argv: Array[String]):Unit = {    
    val (args, opts) = parseOptions(argv)
    
    if(opts.contains("h") || opts.contains("help")){
      exitWith(
      """Grep text inside a text files.
        | usage: Grep [options] text file1 [file2...]
        | usage: Grep [options] text dir [dir2...]
        | [options] --help, -h   Display helpage.
        | [options] --debug      Debug search
      """)
    }
    
    val debug = opts.getOrElse("debug", "false").toBoolean
    val (search::files) = args    
    def grep(file: java.io.File){
      if(file.isDirectory) 
        file.listFiles.map( grep(_) )
      else
        if(debug) println("Searching " + file.getPathname)
        for(ln <- scala.io.Source.fromFile(file).getLines){
          if(ln.contains(search)) print(file.getPathname+":"+ln)  
        }
    }
    for(fn <- files){
      grep(new java.io.File(fn))  
    }
  }
}
