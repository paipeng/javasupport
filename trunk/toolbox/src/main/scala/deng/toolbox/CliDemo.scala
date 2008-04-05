package deng.toolbox

object Demo extends deng.toolbox.lang.CliApplication {
  def main(argv: Array[String]):Unit = {    
    val (args, opts) = parseOptions(argv)
    
    if(opts.contains("help") || opts.contains("h")){
      println("usage: scala Demo [options] arguments")
      println("[options] --help, -h   Display helpage.")
      exit
    }
 
    println(args)
    println(opts)
  }
}
