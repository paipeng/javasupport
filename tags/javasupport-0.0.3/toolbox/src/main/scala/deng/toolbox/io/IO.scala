package deng.toolbox.io;

import java.io.{InputStream, OutputStream}
trait IO {
  val KB = 1024
  val MB = 1048576
  
  def copyStream(ins :InputStream, outs :OutputStream) :Unit = copyStream(ins, outs, MB)  
  def copyStream(ins :InputStream, outs :OutputStream, blockSize :Int) :Unit ={
    val buf = new Array[Byte](blockSize)
    var len = 0
    try{
      while( {len = ins.read(buf, 0, blockSize); len != -1} ){
        outs.write(buf,0,len)
      }
      outs.flush()
		}finally{ ins.close }
  }
}
