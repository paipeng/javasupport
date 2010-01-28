import java.io._

/**
 * IO object provides first-class action function object (closure) for typical 
 * IO processing such as files and streams.
 */
object IO {
  
  /** Run action on each line from the reader object. */
  def eachLine(reader : Reader)(action : String => Unit) : Unit = {
    val sb = new StringBuffer
    val breader = new BufferedReader(reader)
    var line : String = null
    while ({ line = breader.readLine; line != null }) {
      action(line)
    }
  }
  
  /** Run action on each line from a text file. */
  def eachLine(file : String)(action : String => Unit) : Unit = {
    eachLine(new FileReader(file))(action)
  }
  
  /** Read the entire file into a string. */
  def getText(file : String) : String = {
    val sb = new StringBuffer
    eachLine(file) { ln => sb.append(ln) }
    sb.toString
  }  
  
  /** Run action on each block of bytes from an input stream object. 
   * The block array pass to action can be vary in lenght. User can not assume
   * they are same length. User may optional specify the max block size though,
   * which default to 8KB. */
  def eachBytesBlock
    (input : InputStream, maxSize : Int = 1024 * 8)
    (action : Array[Byte] => Unit) : Unit = {
      
    var len = -1
    var buf = new Array[Byte](maxSize)
    while ({ len = input.read(buf, 0, maxSize); len != -1 }) {
      val data = java.util.Arrays.copyOf(buf, len)
      action(data)
    }
  }
}


