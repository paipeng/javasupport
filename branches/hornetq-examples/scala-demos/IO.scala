import java.io._

object IO {
  def eachLine(reader : Reader)(action : String => Unit) = {
    val sb = new StringBuffer
    val breader = new BufferedReader(reader)
    var line : String = null
    while ({ line = breader.readLine; line != null }) {
      action(line)
    }
  }
}


