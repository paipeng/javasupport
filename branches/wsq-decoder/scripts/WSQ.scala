import java.io._
import java.lang.Integer
import wsqenc.util.Helper._

class WSQException(msg: String, cause: Throwable) extends RuntimeException(msg, cause){
  def this(msg: String) = this(msg, null)
}

object MarkerCode {
	val SOI  = 0xFFA0 // Start of Image
  val EOI  = 0xFFA1 // End of Image
  val SOF  = 0xFFA2 // Start Of Frame
  val SOB  = 0xFFA3 // Start Of Block
  val DTT  = 0xFFA4 // Define Transform Table
  val DQT  = 0xFFA5 // Define Quantization Table
  val DHT  = 0xFFA6 // Define Huffman Table
  val DRI  = 0xFFA7 // Define Restart Interval
  val RST0 = 0xFFB0 // Restart with Modulo 0
  val RST1 = 0xFFB1 // Restart with Modulo 1
  val RST2 = 0xFFB2 // Restart with Modulo 2
  val RST3 = 0xFFB3 // Restart with Modulo 3
  val RST4 = 0xFFB4 // Restart with Modulo 4
  val RST5 = 0xFFB5 // Restart with Modulo 5
  val RST6 = 0xFFB6 // Restart with Modulo 6
  val RST7 = 0xFFB7 // Restart with Modulo 7
  val COM  = 0xFFA8 // Comment
}

trait Table
trait DDT extends Table {
  class LowPass(
    val Sn: Int, // Sign of filter coefficient; zero is positive, nonzero is negative
    val Ex: Int, // Scala exponent; number of decimal point moved left
    val H0: Long // Low pass analysis filter elements
  )
  class HighPass(
    val Sn: Int, // Sign of filter coefficient; zero is positive, nonzero is negative
    val Ex: Int, // Scala exponent; number of decimal point moved left
    val H1: Long // Low pass analysis filter elements
  )
    
  def Lt: Int // Transform table definition length
  def L0: Int // Number of analysis low pass filter coefficients (length of H0)
  def L1: Int // Number of analysis high pass filter coefficients (length of H1)
  def lowPass: List[LowPass]
  def highPass: List[HighPass]
  
  override def toString: String =
    String.format("DDT(Lt=%d, L0=%d, L1=%d)", toJIntegerArray(Lt, L0, L1): _*)
}

trait DQT extends Table {
  class Quantization(
    val Eq: Int, // Decimal point of Q
    val Q:  Int, // Quantization table element
    val Ez: Int, // Decimal point of Z
    val Z:  Int  // Zero bin table element
  )
    
  def Lq: Int // Quantization table definition length
  def Ec: Int // Decimal point in C
  def C:  Int // Quantizer bin center parameter
  def quantizations: List[Quantization]
  
  override def toString: String =
    String.format("DQT(Lq=%d, Ec=%d, C=%d)", toJIntegerArray(Lq, Ec, C): _*)
}
trait DHT extends Table {
  def Lh: Int // Huffman table definition length
  def Th: Int // One of eight possible destinations at the decoder into Huffman table
  def lengths: List[Int] // Huffman code length
  def values:  List[List[Int]] // Huffman code value
  
  override def toString: String =
    String.format("DHT(Lh=%d, Th=%d)", toJIntegerArray(Lh, Th): _*)
}
trait COM extends Table {
  def Lc: Int // Comment table definition length
  def comment: String // Comment
  
  override def toString: String =
    "COM(Lc=" + Lc + ", comment=" + comment + ")"
}

trait FrameHeader{
  def Lf: Int // Frame header length
  def A : Int // Scanner black calibration value
  def B : Int // scanner white calibration value
  def Y : Int // Number of lines in image
  def X : Int // Number of samples per line in image
  def Em: Int // Scale exponent for M
  def M : Int // Location value for image transformation parmaters
  def Er: Int // Scale exponent for R
  def R : Int // Scale value for image transformation paramters
  def Ev: Int // Identifies the WSQ encoder algorithm used on image
  def Sf: Int // Identifies that software implementation
  
  override def toString: String =
    String.format(
      "FrameHeader(Lf=%d, A=%d, B=%d, Y=%d, X=%d" + 
      ", Em=%d, M=%d, Er=%d, R=%d, Ev=%d, Sf=%d)", 
      toJIntegerArray(Lf, A, B, Y, X, Em, M, Er, R, Ev, Sf): _*)
}
trait Frame{
  def tables: List[Table]
  def header: FrameHeader
  
  override def toString: String =
      "Frame(\n" + 
      "  tables length =" + tables.length + "\n" + 
      "  header =" + header + "\n" + 
      ")"  
}
trait BlockHeader{
  def Ls: Int // Subband header length
  def Td: Int // Huffman coding table selector
  
  override def toString: String =
    String.format("BlockHeader(Ls=%d, Td=%d)", toJIntegerArray(Ls, Td): _*)
}
trait Block{
  def tables: List[Table]
  def header: BlockHeader
  def ecs: Array[Byte]
  
  override def toString: String =
      "Block(\n" + 
      "  tables length =" + tables.size + "\n" + 
      "  header=" + header + "\n" + 
      ")" 
}

class WSQ {
  var frame: Frame = null
  private var blocks_ = new scala.collection.mutable.ListBuffer[Block]
  
  def blocks = blocks_.toList
  def addBlock(block: Block) = { blocks_ += block }
  
  override def toString: String =
      "WSQ(\n" + 
      "  frame=" + frame + "\n" + 
      "  block=" + blocks + "\n" + 
      ")"
}

class BytesIterator(val buffer: Array[Byte]){
  private var i = 0
  private def iInc = {val old = i; i +=1; old}
  def getNextRawByte = buffer(iInc)
  def getNextByte = bytesToInt(buffer(iInc))
  def getNextWord = bytesToInt(buffer(iInc), buffer(iInc))
  def getNextLong = bytesToLong(buffer(iInc), buffer(iInc), buffer(iInc), buffer(iInc))
  def indexPos = i
  def dumpHex = toString + "\n" +org.apache.commons.io.HexDump.dump(buffer, 0, System.out, 0)
 
  override def toString = 
    "BytesIterator(i=" + i + ", buffer.length=" + buffer.length + ")"
}
  
class DataInputStreamParser(inStream: DataInputStream){
  def this(buf: Array[Byte]) = this(new DataInputStream(new ByteArrayInputStream(buf)))
  def this(ins: InputStream) = this(new DataInputStream(ins))
  
  private def read(byteCount: Int) : BytesIterator = {
    val buf = new Array[Byte](byteCount)
    inStream.readFully(buf, 0, byteCount)
    new BytesIterator(buf)
  }  
  
  def nextByte = withNextBytes(1){ bitr => bitr.getNextByte }  
  def nextWord = withNextBytes(2){ bitr => bitr.getNextWord }  
  def nextLong = withNextBytes(4){ bitr => bitr.getNextLong }
  
  def withNextBytes[T](count: Int)(func: (BytesIterator) => T): T = {
    val bitr = read(count)
    func(bitr)
  }        
  
  def seek2BytesMarker(marker: Int): Array[Byte] = {
    val dataBeforeMarker = new ByteArrayOutputStream
    var done = false
    var found = false
    while(!done){
      try{
        val bitr = read(2)
        val wordData = bitr.getNextWord
        if(wordData == marker){
          found = true
          done = true
        }else{
          dataBeforeMarker.write(bitr.buffer, 0, 2)  
        }
      }catch{
        case x: EOFException => done = true
      }
    }
    if(!found)
      throw new WSQException("Marker " + to2BytesHex(marker) + " not found.")
    
    dataBeforeMarker.toByteArray    
  }
}

class Parser(file: String) {
  
  def parse: WSQ = {
    val fins = new FileInputStream(file)
    try{      
      val wsqParser = new DataInputStreamParser(fins)
      val wsq = new WSQ
      
      // === Start Parsing Stream ===
      
      // => Start of Image
      wsqParser.withNextBytes(2){ bitr =>
        val wordData = bitr.getNextWord
        if(wordData != MarkerCode.SOI) 
          throw new WSQException("Invalid SOI marker " + to2BytesHex(wordData))
      }
      
      // => Process each marker inside image
      var done = false
      val tables = new scala.collection.mutable.ListBuffer[Table]
      while(!done){
        wsqParser.withNextBytes(2){ bitr =>
          val wordData = bitr.getNextWord
          wordData match {
            case MarkerCode.EOI => done = true
            
            case MarkerCode.DTT => {
              val t = parseDTT(wsqParser)
              tables += t
              println("Parsed " + t)
            }
            case MarkerCode.DQT => {
              val t = parseDQT(wsqParser)
              tables += t
              println("Parsed " + t)
            }
            case MarkerCode.DHT => {
              val t = parseDHT(wsqParser)
              tables += t
              println("Parsed " + t)
            }
            case MarkerCode.DRI => {
              throw new WSQException("Not yet implemeneted marker " + to2BytesHex(wordData))    
            }
            case MarkerCode.COM => {
              val t = parseCOM(wsqParser)
              tables += t
              println("Parsed " + t)
            }
          
            case MarkerCode.SOF => {
              wsq.frame = parseFrame(wsqParser, tables.toList)
              tables.clear
              println("Parsed " + wsq.frame)              
            }
            case MarkerCode.SOB => {
              val block = parseBlock(wsqParser, tables.toList)
              wsq.addBlock(block)
              tables.clear
              println("Parsed " + block)
                            
              //TODO: parse ECS => Entropy Coded Segment
              //wsqParser.withNextBytes(7476){ bitr => }
              //val x = wsqParser.seek2BytesMarker(MarkerCode.SOB)
              //println(new BytesIterator(x))
            }
            
            case MarkerCode.RST0 |
              MarkerCode.RST1 |
              MarkerCode.RST2 |
              MarkerCode.RST3 |
              MarkerCode.RST4 |
              MarkerCode.RST5 |
              MarkerCode.RST6 |
              MarkerCode.RST7 => {
              throw new WSQException("Not yet implemeneted marker " + to2BytesHex(wordData))    
            }
            
            case _ =>
              throw new WSQException("Invalid segment marker " + to2BytesHex(wordData))              
          }
        }    
      }
      // === Done Parsing Stream ===
      
      //return a parsed WSQ object.
      wsq
    }finally{ fins.close }
  }
  
  def parseFrame(wsqParser: DataInputStreamParser, _tables: List[Table]): Frame =
    new Frame{ 
      val tables = _tables
      val header = wsqParser.withNextBytes(17){ bitr => 
        new FrameHeader {
          val Lf = bitr.getNextWord // 2 byte(s)
          val A  = bitr.getNextByte // 1 byte(s)
          val B  = bitr.getNextByte // 1 byte(s)
          val Y  = bitr.getNextWord // 2 byte(s)
          val X  = bitr.getNextWord // 2 byte(s)
          val Em = bitr.getNextByte // 1 byte(s)
          val M  = bitr.getNextWord // 2 byte(s)
          val Er = bitr.getNextByte // 1 byte(s)
          val R  = bitr.getNextWord // 2 byte(s)
          val Ev = bitr.getNextByte // 1 byte(s)
          val Sf = bitr.getNextWord // 2 byte(s)
        }
      }
    }
  
  def parseBlock(wsqParser: DataInputStreamParser, _tables: List[Table]): Block = 
    new Block{ 
      val tables = _tables
      val header = wsqParser.withNextBytes(3){ bitr => 
        new BlockHeader {
          val Ls = bitr.getNextWord // 2 byte(s)
          val Td = bitr.getNextByte // 1 byte(s)
        }
      }
      val ecs = {
        val tblOpt = tables.find{ table => table.isInstanceOf[DHT] }
        tblOpt match {
          case None => throw new WSQException("No huffman table(DHT) table is found on this block.")
          case Some(tbl) => {
            val huffmanTbl = tbl.asInstanceOf[DHT]
            val len = huffmanTbl.values.foldLeft(0){ (aSum, a) => aSum + a.foldLeft(0){ (bSum, b) => bSum + b  } }
            println(len)
            wsqParser.withNextBytes(len){ bitr => bitr.buffer }
          }
        }
      }
      
      def huffmanSize(bits): Int = {
        var k = 0, i = 0, j = 0
        var size = 0
        var done = false
        while(!done){
          if(j > bits(i)){
            i = i + 1
            j = 1
            
            if(i > 16){
              done = true
              size = 0 //???
              lask = k
            }
          }else{
            size = i
            k = k + 1
            j = j + 1
          }
        }
        size        
      }
    }
  
  def parseDTT(wsqParser: DataInputStreamParser) = {
    val blockLen = wsqParser.nextWord
    val dataLen = blockLen - 2
    wsqParser.withNextBytes(dataLen){ bitr =>
      new DDT {
        val oriIndexPos = bitr.indexPos
        val Lt = blockLen
        val L0 = bitr.getNextByte
        val L1 = bitr.getNextByte
                
        val lowPass = {
          val lsBuffer = new scala.collection.mutable.ListBuffer[LowPass]
          for(i <- 1 to (L0 - L1)){
            lsBuffer += 
              new LowPass(
                bitr.getNextByte,
                bitr.getNextByte,
                bitr.getNextLong
              )
          }
          lsBuffer.toList
        }
        val highPass = {
          val lsBuffer = new scala.collection.mutable.ListBuffer[HighPass]
          for(i <- 1 to L1){
            lsBuffer += 
              new HighPass(
                bitr.getNextByte,
                bitr.getNextByte,
                bitr.getNextLong
              )
          }
          lsBuffer.toList
        }
        
        val consummed = bitr.indexPos - oriIndexPos
        if(consummed != dataLen)
          throw new WSQException("Internal Parser error: data consummed length " + 
            consummed + " does not match to DDT.Lt " + Lt)
      }
    }
  }
  
  def parseDQT(wsqParser: DataInputStreamParser) = {
    val blockLen = wsqParser.nextWord
    val dataLen = blockLen - 2
    wsqParser.withNextBytes(dataLen){ bitr =>
      new DQT {
        val oriIndexPos = bitr.indexPos
        val Lq = blockLen
        val Ec = bitr.getNextByte
        val C = bitr.getNextWord
        
        val quantizations = {
          val lsBuffer = new scala.collection.mutable.ListBuffer[Quantization]
          for(i <- 1 to 64){
            lsBuffer += 
              new Quantization(
                bitr.getNextByte,
                bitr.getNextWord,
                bitr.getNextByte,
                bitr.getNextWord
              )
          }
          lsBuffer.toList
        }
        
        val consummed = bitr.indexPos - oriIndexPos
        if(consummed != dataLen)
          throw new WSQException("Internal Parser error: data consummed length " + 
            consummed + " does not match to DQT.Lq " + Lq)
      }
    }
  }
  
  def parseDHT(wsqParser: DataInputStreamParser) = {
    val blockLen = wsqParser.nextWord
    val dataLen = blockLen - 2
    wsqParser.withNextBytes(dataLen){ bitr =>
      //bitr.dumpHex      
      new DHT {
        val oriIndexPos = bitr.indexPos
        val Lh = blockLen
        val Th = bitr.getNextByte
        
        val lengths = {
          val lsBuffer = new scala.collection.mutable.ListBuffer[Int]
          for(i <- 1 to 16){
            lsBuffer += bitr.getNextByte
          }
          lsBuffer.toList
        }
        
        val values = {
          val iLsBuffer = new scala.collection.mutable.ListBuffer[List[Int]]
          for(i <- lengths){
            val jLsBuffer = new scala.collection.mutable.ListBuffer[Int]
            for(j <- 1 to i){
              jLsBuffer += bitr.getNextByte
            }
            iLsBuffer += jLsBuffer.toList          
          }
          iLsBuffer.toList
        }
        val consummed = bitr.indexPos - oriIndexPos
        if(consummed != dataLen)
          throw new WSQException("Internal Parser error: data consummed length " + 
            consummed + " does not match to DHT.Lh " + Lh)
      }
    }
  }
    
  def parseCOM(wsqParser: DataInputStreamParser) = {
    val blockLen = wsqParser.nextWord
    val dataLen = blockLen - 2
    wsqParser.withNextBytes(dataLen){ bitr =>
      new COM {
        val oriIndexPos = bitr.indexPos
        val Lc = blockLen    
        val comment = {
          val lsBuffer = new scala.collection.mutable.ListBuffer[Byte]
          for(i <- 1 to Lc){
            lsBuffer += bitr.getNextRawByte
          }
          new String(lsBuffer.toArray)
        }
        
        val consummed = bitr.indexPos - oriIndexPos
        if(consummed != dataLen)
          throw new WSQException("Internal Parser error: data consummed length " + 
            consummed + " does not match to COM.Lc " + Lc)
      }
    }
  }
}
