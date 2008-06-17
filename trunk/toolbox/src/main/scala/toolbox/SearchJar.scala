package toolbox
import toolbox.scalasupport._
import java.io.{File}
import java.util.regex.{Pattern, Matcher}
import java.util.zip.{ZipFile,ZipEntry}
object SearchJar {   
  var options = Map[String, String]()
  
  def main(argv: Array[String]): Unit = {
    var args = List[String]()
    for(s <- argv) s match {
      case "-h" | "--help" => printUsageAndExit
      case "-d" | "--debug" => options += ("debug"->"true")
      case "-f" | "--foldersearch" => options += ("foldersearch"->"true")
      case _ => args = s::args
    }
    
    if(args.size <=1) printUsageAndExit
    args = args.reverse
    
    val text = args(0)
    val jars = args.drop(1)
    val foldersearch = options.contains("foldersearch")
    
    debug("Searching: " + text)
    for(name <- jars){
      val dir = new File(name)
      if(dir.isFile && dir.getName.endsWith(".jar")){
        searchJar(text, dir)  
      }else{
        //search dir for jar, then match for text inside a jar.
        walkWithFile(dir){ subfile =>            
          if(subfile.getName.endsWith(".jar"))
            searchJar(text, subfile)  
        }
        
        if(foldersearch){
          //search dir for filename, then match text to filename(cases where classpath is a dir)
          walkWithDir(dir){ subdir =>
            val files = subdir.listFiles.filter{ _.getName.contains(text) }
            files.foreach{ f => println(getPathname(f)) }
          }
        }
      }
    }
  }

  def debug(s: String) = if(options.contains("debug")) println(s)

  def printUsageAndExit(){	
    println("Program to search text(entry name) inside a jar file.")
    println("Usage: scala SearchJar [-d|-f] text jarfile_or_dir [jarfile_or_dir ...]")
    println("   -d turn debug mode on.")
    println("   -f also search any folder for file name match.")
    exit(1)
  }
  
  def searchJar(text: String, jarfile: File): Unit = {
    debug("Searching: " + text + " in jarfile "+ getPathname(jarfile))
    val enu = new ZipFile(jarfile).entries
    while(enu.hasMoreElements){
      val entry = enu.nextElement.asInstanceOf[ZipEntry]
      val name = entry.getName
      if(name.contains(text))
        println(jarfile.getAbsolutePath + " " + name)
    }
  }
  
  /** walk a directory recursively and call func with each file found. */
  def walkWithFile(dir: File)(func: (File)=>Unit): Unit = {
    debug("Walk: " + dir)
    if(dir.isDirectory()) 
      dir.listFiles.foreach{ f => walkWithFile(f)(func) }
    else
      func(dir) //it's a file
  }
  /** walk a directory recursively and call func with each dir found. */
  def walkWithDir(dir: File)(func: (File)=>Unit): Unit = {
    debug("Walk: " + dir)
    if(dir.isDirectory()){
      func(dir) //it's a dir
      dir.listFiles.foreach{ f => walkWithDir(f)(func) }
    }
  }
  /** Get only the path upto where it first defined if not abosulte. */
  def getPathname(file: File): String = {
    if(file.isAbsolute) file.getAbsolutePath
    else{
      var f = file //init file path name
      val sb = new StringBuilder(f.getName)
      while(f.getParentFile != null){
        f = f.getParentFile
        sb.insert(0, f.getName+File.separator)
      }
      sb.toString
    }
  }
}
