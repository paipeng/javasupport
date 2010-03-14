package sweet.helper
import java.io._
class StreamHelper(ins : InputStream){
  
  def foreachLine(proc : String => Unit) : Unit = {    
    val br = new BufferedReader(new InputStreamReader(ins))
    try{ while(br.ready) proc(br.readLine) }
    finally{ br.close }
  }
  
  def copy(outs : OutputStream) : Unit = {
    var len = -1
    val MAX_READ = 5120
    val buf = new Array[Byte](MAX_READ)
    val bins = if(ins.isInstanceOf[BufferedInputStream]) ins else new BufferedInputStream(ins)
    val bouts = if(ins.isInstanceOf[BufferedOutputStream]) outs else new BufferedOutputStream(outs)
    try{
      while({len = bins.read(buf, 0, MAX_READ); len != -1})
        bouts.write(buf, 0, len)
      bouts.flush
    }finally{ 
      bouts.close
      bins.close
    }
  }
}
object StreamHelper{
  implicit def streamHelper(ins : InputStream) = new StreamHelper(ins)
}

