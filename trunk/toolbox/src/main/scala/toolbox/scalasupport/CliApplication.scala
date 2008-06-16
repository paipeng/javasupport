package toolbox.scalasupport

import scala.collection.mutable.{ HashMap, ListBuffer } 
trait CliApplication {
  /**
   * Simple parser for short and long style command line options and arguments.
   * Short option format is single dash prefix with single char flag. Anything beyong
   * the first char will be treated as parameter value for it's flag.
   * Long option format is two dashes prefix with one or more chars flag. Option parameter
   * must specify with equal char.
   *
   * @return tuple of 2 elements: list of clean args and map of options.
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
  def exitWith(msg: String) {
    RichSystem.exitWith(msg, 0)
  }
  
  /** Parse commandline args and return immutable args list and options map. 
  We will automatically insert/define -h for help and -d for debug option
  if subclass does not provide them. Also if subclass not defined a match
  then it will be add to arg list by default.
  def parseOptions(args: Array[String])
  (f: (ListBuffer[String], HashMap[String, String]) => PartialFunction[String, Unit]):
  (List[String], Map[String, String]) = {
    val argList = new ListBuffer[String]   
    val options = new HashMap[String, String]
    val pf = f(argList, options)
    for(a <- args) 
      if(!pf.isDefinedAt(a)) argList.append(a) else pf(a)
    
    if(!pf.isDefinedAt("-h"))
      printExitUsage
    if(!pf.isDefinedAt("-d") && options.contains("-d"))
      options("debug") = "true"
      
    (argList.toList, Map(options.toArray: _*))
  }  
  */
}
