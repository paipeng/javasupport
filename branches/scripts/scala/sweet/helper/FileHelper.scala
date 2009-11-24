package sweet.helper
import java.io._
class FileHelper(file : File) {
  import FileHelper._
  
  /** return proper list of subfiles, includes File and Dir. */
  def subfiles : List[File] = file.listFiles match {
    case null => List()
    case res @ _ => res.toList
  }
  /** traverse file and call proc for each item found, including subfiles, recusively. 
   * the callback proc paramter will contains all the files been walked. The first item
   * is the current file been processed. */
  def walk(proc : List[File] => Unit) : Unit = {
    def walk(wfile : File, walked : List[File], proc : List[File] => Unit) : Unit = {
      val walkedFiles = wfile :: walked
      if(wfile.isDirectory){
        new FileHelper(wfile).subfiles.foreach{ f => walk(f, walkedFiles, proc) }
      }
      proc(walkedFiles)
    }
    walk(file, Nil, proc)
  }
  /** call proc for each line read in text file */
  def foreachLine(proc : String => Unit) : Unit = {
    val br = new BufferedReader(new FileReader(file))
    try{ while(br.ready) proc(br.readLine) }
    finally{ br.close }
  }
  /** write a text into a file. no new line char is added. */
  def write(text : String) : Unit = {
    val fw = new FileWriter(file)
    try{ fw.write(text) }
    finally{ fw.close }
  }
  /** read entire text file into a single string. */
  def read : String = {
    val res = new StringBuilder
    foreachLine{ ln => res.append(ln).append(LINE_SEP) }
    res.toString
  }
  /** traverse file as dir recursively and call proc for each File found. */
  def foreachFile(proc : File => Unit) : Unit = {
    walk{ paths =>
      val f = paths.head
      if(f.isFile)
        proc(file)
    }
  }
  /** traverse file as dir recursively and call proc for each Dir found. */
  def foreachDir(proc : File => Unit) : Unit = {
    walk{ paths =>
      val f = paths.head
      if(f.isDirectory)
        proc(file)
    }
  }
  /** delete this file. if file is a dir then delete all subfiles recursively. */
  def deleteAll : Unit = walk{ paths =>  paths.head.delete }
  
  /** copy this file content into another file. */
  def copy(tofile: File) : Unit = {
    val out = new FileOutputStream(tofile)
    new StreamHelper(new FileInputStream(file)).copy(out)
  }
  
  /** move this file into another file. notice that after the move, this file instance is deleted! */
  def move(tofile: File) : Unit = {
    file.renameTo(tofile)
  }
  
  def withOutputStream(p: OutputStream => Unit): Unit = {
    val s = new FileOutputStream(file)
    try{ p(s) } finally{ s.close }
  }
  def withInputStream(p: InputStream => Unit): Unit = {
    val s = new FileInputStream(file)
    try{ p(s) } finally{ s.close }
  }
  def withWriter(p: Writer => Unit): Unit = {
    val w = new FileWriter(file)
    try{ p(w) } finally{ w.close }
  }
  def withReader(p: Reader => Unit): Unit = {
    val r = new FileReader(file)
    try{ p(r) } finally{ r.close }
  }
  def withPrintWriter(p: PrintWriter=>Unit): Unit = {
    withWriter{ w => 
      val pw = new PrintWriter(w)
      try{ p(pw) } finally {pw.close}
    }
  }      
}
object FileHelper{
  /** convert java.io.File into this FileHelper object. */
  implicit def file2helper(file : File) = new FileHelper(file)
  
  val LINE_SEP = System.getProperty("line.separator")
}