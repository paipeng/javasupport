package toolbox
import toolbox.scalasupport._  
import RichFile._
import java.io.File
object Cat extends CliApplication{
  def usage = """Print text file content and conCAT if more than one is given.
        | usage: Cat [options] file1 [file2...]
        | [options] -h   Display helpage.
        | [options] -n   Display line number
      """.stripMargin
      
  def main(argv: Array[String]){    
    val (args, opts) = parseOptions(argv)    
    if(opts.contains("h") || args.size < 1) exitWith(usage)
    
    val printLine = opts.contains("n")
    for(fn <- args){    
      new File(fn).eachLineWithNumber{ (ln, i) =>
        if(printLine) printf("%5d", i)
        println(ln) 
      }
    }
  }
}
