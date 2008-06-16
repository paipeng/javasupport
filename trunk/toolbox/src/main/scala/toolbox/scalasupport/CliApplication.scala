package toolbox.scalasupport

import scala.collection.mutable.{ HashMap, ListBuffer } 
trait CliApplication {
  def usage = "Usage: scala " + getClass.getName + " [options] [arg ...]" + """
    |   -h display helpage.
    |   -d turn debug mode ON.  
    """.stripMargin
  def printExitUsage {
    println(usage)
    exit(1)
  }
  /** Parse commandline args and return immutable args list and options map. 
  We will automatically insert/define -h for help and -d for debug option
  if subclass does not provide them. Also if subclass not defined a match
  then it will be add to arg list by default.
  */
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
}
