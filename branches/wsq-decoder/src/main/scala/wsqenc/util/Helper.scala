package wsqenc.util
object Helper {
	import java.io._
  	
	//stream out each block and call user function.
	//note that data block might not be full to blockSize!
	def eachBlock
	(ins: InputStream, blockSize: Int)
	(func: (Array[Byte]) => Unit): Unit = {
		val buf = new Array[Byte](blockSize)
		var readLen = -1
		while({readLen = ins.read(buf, 0, blockSize); readLen} != -1){
			val data = new Array[Byte](readLen)
			System.arraycopy(buf, 0, data, 0, readLen)
			func(data)
		}
	}
	
	//stream out each block and call user function with specific positions
	//note that data block might not be full to blockSize!
	def eachBlock
	(ins: InputStream, blockSize: Int, begin: Int, end: Int)
	(func: (Array[Byte]) => Unit): Unit = {
		val buf = new Array[Byte](blockSize)
		var readLen = -1
		var readTotal = begin
		var done = false
		while(!done){
			readLen = ins.read(buf, 0, blockSize)
			if(readLen != -1){
				if(readTotal + readLen > end){
					done = true
					
					val remainLen = end - readTotal					
					val data = new Array[Byte](remainLen)
					System.arraycopy(buf, 0, data, 0, remainLen)
					func(data)
				}else{
					val data = new Array[Byte](readLen)
					System.arraycopy(buf, 0, data, 0, readLen)
					func(data)
				}
				readTotal += readLen	
			}else{
				done = true
			}
		}
	}
  
  // === Methods to handle bytes manipulation and conversion
  def toJIntegerArray(ary: Int*): Array[java.lang.Integer] = {
    val res = new Array[java.lang.Integer](ary.length)
    var i = 0
    ary.foreach{ b =>
      res(i) = java.lang.Integer.valueOf(b)
      i += 1
    }
    res
  }
  def toJByteArray(ary: Byte*): Array[java.lang.Byte] = {
    val res = new Array[java.lang.Byte](ary.length)
    var i = 0
    ary.foreach{ b =>
      res(i) = java.lang.Byte.valueOf(b)
      i += 1
    }
    res
  }
  def bytesToInt(ary: Byte*): Int = {
    val fmt = "%X" * ary.length
    val javaByteAry = toJByteArray(ary: _*)
    val hex = String.format(fmt, javaByteAry: _*)
    java.lang.Integer.parseInt(hex, 16)
  }
  def bytesToLong(ary: Byte*): Long = {
    val fmt = "%X" * ary.length
    val javaByteAry = toJByteArray(ary: _*)
    val hex = String.format(fmt, javaByteAry: _*)
    java.lang.Long.parseLong(hex, 16)
  }
  def to2BytesHex(i: Int) = String.format("0x%04X", java.lang.Integer.valueOf(i))
}
