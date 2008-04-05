package deng.toolbox

object Grep extends deng.toolbox.lang.CliApplication{
  def main(argv: Array[String]):Unit = {    
    val (args, opts) = parseOptions(argv)
    
    if(opts.contains("h") || opts.contains("help")){
      println("usage: Grep [options] text file1 [file2...]")
      println("usage: Grep [options] text dir [dir2...]")
      println("[options] --help, -h   Display helpage.")
      println("[options] --debug      Debug search")
      exit
    }
    
    val debug = opts.getOrElse("debug", "false").toBoolean
    val List(search, files@_*) = args

    for(fn <- files){
      def grep(file: java.io.File){
        if(file.isDirectory) 
          file.listFiles.map( grep(_) )
        else
          if(debug) println("Searching " + file.getName)
          for(ln <- scala.io.Source.fromFile(file).getLines){
            if(ln.contains(search)) print(file.getName+":"+ln)  
          }
      }
      grep(new java.io.File(fn))  
    }
  }
}
