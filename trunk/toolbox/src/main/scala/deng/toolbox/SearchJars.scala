package deng.toolbox

object SearchJars extends deng.toolbox.lang.CliApplication{
  def main(argv: Array[String]):Unit = {    
    val (args, opts) = parseOptions(argv)
    
    if(opts.contains("h") || opts.contains("help")){
      println("usage: SearchJars [options] arguments")
      println("[options] --help, -h   Display helpage.")
      println("[options] --search=<filter>")
      exit
    }
    
    val search = opts.get("search")
    
    import java.util.jar._
    for(fn <- args){
      println("Searching " + fn)
      val entries = new JarFile(fn).entries
      while(entries.hasMoreElements){
        val entry = entries.nextElement.asInstanceOf[JarEntry]
        val name = entry.getName
        
        search match {
          case Some(filter) => if (name.contains(filter)) println("Found: "+name)
          case None => println("Found: "+name)
        }
      }
    }
  }
}
