import java.io._

object IO {
  def eachLine(file : String)(action : String => Unit) : Unit = {
    eachLine(new FileReader(file))(action)
  }
  def eachLine(reader : Reader)(action : String => Unit) : Unit = {
    val sb = new StringBuffer
    val breader = new BufferedReader(reader)
    var line : String = null
    while ({ line = breader.readLine; line != null }) {
      action(line)
    }
  }   
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


