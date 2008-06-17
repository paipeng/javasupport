package toolbox
import toolbox.scalasupport._
import RichFile._
object Find extends CliApplication{
  def main(argv: Array[String]):Unit = {    
    val (args, opts) = parseOptions(argv)
    
    if(opts.contains("h") || args.size < 1){
      exitWith(
      """Find file by name or list them all from a dir.
        | usage: Find [options] dir name
        | [options] -h   Display helpage
      """)
    }
    
    val dir = args(0)    
    val dirFile = new RichFile(dir)
    if(!dirFile.isDirectory)
      exitWith("First argument is not a directory.")
    
    if(args.size < 2)
      dirFile.walk{ f => println(f.getPathname) }
    else
      dirFile.walk{ f => { val p = f.getPathname; if(p.contains(args(1))) println(p) } }
  }
}
