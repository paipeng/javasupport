package deng.toolbox

object CliDemo extends deng.toolbox.lang.CliApplication {
  def main(argv: Array[String]):Unit = {    
    val (args, opts) = parseOptions(argv)
    
    if(opts.contains("help") || opts.contains("h")){
      println("usage: scala CliDemo [options] arguments")
      println("[options] --help, -h   Display helpage.")
      exit
    }
 
    println(args)
    println(opts)
  }
}
