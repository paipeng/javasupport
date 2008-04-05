package deng.toolbox.lang


/**
 * Trait that provide simple command line parsing implementation.
 *
 * Example:
 * <pre>
 * object Demo extends deng.toolbox.lang.CliApplication {
 *   def main(argv: Array[String]):Unit = {    
 *     val (args, opts) = parseOptions(argv)
 *     if(opts.contains("help") || opts.contains("h")){
 *       println("usage: scala Demo [options] arguments")
 *       println("[options] --help, -h   Display helpage.")
 *       exit
 *     }
 *     println(args)
 *     println(opts)
 *   } 
 * }
 * </pre>
 *
 * @author thebugslayer
 */
trait CliApplication {
          
  /** 
   * Simple parser for short and long style command line options and arguments. 
   * Short option format is single dash prefix with single char flag. Anything beyong
   * the first char will be treated as parameter value for it's flag.
   * Long option format is two dashes prefix with one or more chars flag. Option parameter
   * must specify with equal char.
   * 
   * @return tuple of 2 elements: parstedArgs and parsedOpts.
   */
  def parseOptions(argv: Array[String]): (List[String], Map[String, String])={
      var args = List[String]()
      var opts = Map[String,String]()
      for(arg <- argv){
          if (arg.startsWith("--")) {
              val s = arg.substring(2).split("=")
              if (s.length >= 2) {
                  opts = opts + (s(0) -> s(1))
              } else {
                  opts = opts + (s(0) -> "true")
              }
          } else if (arg.startsWith("-")) {
              val s = arg.substring(1)
              if (s.length() > 1) {
                  opts = opts + (s.substring(0, 1) -> s.substring(1))
              } else {
                  opts = opts + (s -> "true")
              }
          } else {
              args = arg :: args;
          }
      }
      (args.reverse, opts);
  }
}

