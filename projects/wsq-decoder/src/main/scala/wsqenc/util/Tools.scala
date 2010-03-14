package wsqenc.util
object ExtractBytes {
	import wsqenc.util.Helper._
	import java.io._
	
	def main(args: Array[String]): Unit = {	
		val file = args(0)
		val begin = if(args.length >=2) args(1).toInt else 0
		val end = if(args.length >=3) args(2).toInt else 0
		
		val f = new File(file)
		val fins = new FileInputStream(file)
		val datas = new DataInputStream(fins)
		val flen = f.length.toInt
		val len = if(end == 0) flen.toInt else (end - begin)
		val outFile = new File(file + "." + len + ".out")
		printf("Extracting %d out of %d bytes to %s\n", len, flen, outFile)
		try{
			val buf = new Array[Byte](len)
			datas.readFully(buf)
			val fouts = new FileOutputStream(outFile)
			try{ fouts.write(buf) }
			finally{ fouts.close }
			
		}finally{ datas.close }
	}
}

object HexDump {
	import wsqenc.util.Helper._
	import java.io._
	
	//Dump a binary data stream in hex to stdout.
	def dumphex(ins: InputStream, begin: Int, end: Int): Unit = {	
		val blockblockSize = 1024
		val maxBytesPerRow = 16
		var i = begin
		
		val isPrintable = (b: Byte) => (b >= 0x21 && b <= 0x7E)
		val displayData = (data: Array[Byte]) => {
			data.foreach{ b =>
				if(i % maxBytesPerRow == 0){
					printf("\n%09d: ", i)
				}
				val display = if(isPrintable(b)) b.asInstanceOf[Char] else '.'
				printf("%02X(%c) ", b, display)
				i += 1			
			}
		}
		
		if(end == 0){
			eachBlock(ins, blockblockSize){ data => displayData(data) }
		}else{
			ins.skip(begin)
			eachBlock(ins, blockblockSize, begin, end){ data => displayData(data) }
		}
	}
	
	// Diplay a file content in HEX. You may specific range of bytes to display.
	// - The row count column is displayed in DEC, and starts from ZERO!
	// - When total up the file length, it should equal to the column display count + 1
	def main(args: Array[String]): Unit = {	
		val file = args(0)
		val begin = if(args.length >=2) args(1).toInt else 0
		val end = if(args.length >=3) args(2).toInt else 0
		
		val f = new File(file)
		val fins = new FileInputStream(file)
		printf("File name: %s length: %d", f.getName, f.length)
		try{
			dumphex(fins, begin, end)				
		}finally{
			fins.close
		}
	}
}