package toolbox.scalasupport

import java.io._
object RichStream {
  val KB = 1024
  val MB = KB*KB
  
  def eachLine(ins: InputStream)(func: String => Unit){
    val br = new BufferedReader(new InputStreamReader(ins))
    try{
      var ln = ""
      while({ ln = br.readLine; ln != null })
        func(ln)
    }finally{ br.close }
  }
  def eachBlock(ins: InputStream, size:Int)(process: (Array[Byte])=>Unit){
		val buf = new Array[Byte](size)
    var len = 0
    val bins = new BufferedInputStream(ins)
    try{
      while( {len = bins.read(buf, 0, size); len != -1} )
        process(buf.subArray(0,len))
		}finally{ bins.close }
  }
  def copyStream(ins: InputStream, outs: OutputStream): Unit = copyStream(ins, outs, MB)  
  def copyStream(ins: InputStream, outs: OutputStream, blockSize: Int): Unit = {
    val buf = new Array[Byte](blockSize)
    var len = 0
    try{
      while( {len = ins.read(buf, 0, blockSize); len != -1} )
        outs.write(buf,0,len)
      outs.flush
		}finally{ ins.close }
  }
}

