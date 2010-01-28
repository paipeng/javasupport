import java.io._

/**
 * IO object provides first-class func function object (closure) for typical 
 * IO processing such as files and streams.
 */
object IO {
    
  /** Run func with a PrintWriter that will write output to file. */
  def withPrintWriter(file : String)(func : PrintWriter => Unit) : Unit = {
    val writer = new PrintWriter(new FileWriter(file))
    try { func(writer) } finally { writer.close }
  }
  
  /** Run func on each line from the reader object. A BufferedReader is used
   * to wrap and read the reader object. 
   *
   * This method will NOT close the reader object. */
  def eachLine(reader : Reader)(func : String => Unit) : Unit = {   
    val breader = new BufferedReader(reader)
    var line : String = null
    while ({ line = breader.readLine; line != null }) {
      func(line)
    }
  }
  
  /** Run func on each line from a text file. */
  def eachLine(file : String)(func : String => Unit) : Unit = {
    val reader = new FileReader(file)
    try { eachLine(reader)(func)  } finally { reader.close }
  }
  
  /** Read the entire file into a string. */
  def getText(file : String) : String = {
    val sb = new StringBuffer
    eachLine(file) { ln => sb.append(ln) }
    sb.toString
  }  
  
  /** Run func on each block of bytes from an input stream object. 
   * The block array pass to func can be vary in lenght. User can not assume
   * they are same length. User may optional specify the max block size though,
   * which default to 8KB. 
   * 
   * This method will NOT close the input stream object. */
  def eachBytesBlock
    (input : InputStream, maxSize : Int = 1024 * 8)
    (func : Array[Byte] => Unit) : Unit = {
      
    var len = -1
    var buf = new Array[Byte](maxSize)
    while ({ len = input.read(buf, 0, maxSize); len != -1 }) {
      val data = java.util.Arrays.copyOf(buf, len)
      func(data)
    }
  }
}


