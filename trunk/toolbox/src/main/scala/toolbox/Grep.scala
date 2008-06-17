package toolbox
import toolbox.scalasupport._
import RichFile._
object Grep extends CliApplication{
  def main(argv: Array[String]):Unit = {    
    val (args, opts) = parseOptions(argv)
    
    if(opts.contains("h")){
      exitWith(
      """Grep text inside a text files.
        | usage: Grep [options] text file1 [file2...]
        | usage: Grep [options] text dir [dir2...]
        | [options] -h   Display helpage.
        | [options] -v   Revese display with found text.
        | [options] -s   Read from STDIN
        | [options] -d   Debug search.
      """.stripMargin)
    }
    
    val debug = opts.contains("d")
    val reverse = opts.contains("v")
    val stdin = opts.contains("s")
    val (search::files) = args
    
    def grep(file: java.io.File){
      if(file.isDirectory) 
        file.listFiles.map( grep(_) )
      else
        if(debug) println("Searching " + file.getPathname)
        if(reverse)          
          for(ln <- scala.io.Source.fromFile(file).getLines)
            if(!ln.contains(search)) print(file.getPathname+":"+ln)            
        else
          for(ln <- scala.io.Source.fromFile(file).getLines)
            if(ln.contains(search)) print(file.getPathname+":"+ln)  
    }
    if(stdin){
      for(ln <- scala.io.Source.fromInputStream(System.in).getLines)
       if(!ln.contains(search)) print(ln)             
    }else{
      for(fn <- files)
        grep(new java.io.File(fn))  
    }
  }
}
