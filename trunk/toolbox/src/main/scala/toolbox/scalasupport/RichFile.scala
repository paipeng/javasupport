/**
 * Classes that support java.io.File better and provide more convinient Dir, BinaryFile, TextFile manipulation.
 */
package toolbox.scalasupport

import java.io.{File, FileInputStream, FileOutputStream, BufferedReader, FileReader, FileWriter}
import java.util.zip.{ZipOutputStream, ZipInputStream, ZipEntry}
import scala.io.Source
import java.net.URI
import RichStream.{copyStream}

object RichFile{
  val DEFAULT_BLOCK_SIZE = 1024*1024 //read size when reading stream.
  implicit def file2RichFile(f: File) = new RichFile(f)  
}

/**
 * Extension to java.io.File that provide many missing features.
 * @author Zemian Deng
 */
import RichFile._
class RichFile(parent: File, name: String, blockSize: Int) extends File(parent, name) {
  def this(name: String) = this(null: File, name, DEFAULT_BLOCK_SIZE)
  def this(file: File) = this(file.getParentFile, file.getName, DEFAULT_BLOCK_SIZE)
  def this(file: File, name: String) = this(file, name, DEFAULT_BLOCK_SIZE)
  def this(file: File, blockSize: Int) = this(file.getParentFile, file.getName, blockSize)
    
  /** Get only the path upto where it first defined if not abosulte. */
  def getPathname: String = {
    if(this.isAbsolute) this.getAbsolutePath
    else{
      var f = this //init file path name
      val sb = new StringBuilder(f.getName)
      while(f.getParentFile != null){
        f = f.getParentFile
        sb.insert(0, f.getName+File.separator)
      }
      sb.toString
    }
  }
  
  def walk(func: File=>Unit){
    def walk(walkedFile: File){
      if(walkedFile.isDirectory){
        val thiss = walkedFile.listFiles.toList
        for(subthis <- thiss.sort((f1,f2)=>f1.compareTo(f2)<0) )
          walk(subthis)
      }
      func(walkedFile)
    }
    walk(this)
  }
  
  def eachFile(func: File=>Unit){
    walk{ f =>  if(this.isFile) func(this) }
  }
  def eachDir(func: File=>Unit){    
    walk{ f =>  if(this.isDirectory) func(this) }
  }
  def deleteAll{
    walk{ f => f.delete }
  }    
  def copyTo(dest:File)={
    //if dest is a directory, then copy into it. else overwrite dest this.
    if(dest.isDirectory){
      val destFile = new File(dest, this.getName)
      copyStream(new FileInputStream(this), new FileOutputStream(destFile))
    }else{
      copyStream(new FileInputStream(this), new FileOutputStream(dest))    
    }
  }
  def eachLine(process: (String)=>Unit){
		var line: String = null
    val reader = new BufferedReader(new FileReader(this))
    try{
      while( {line = reader.readLine; line != null} ){
        process(line)
      }
    }finally{ reader.close() }
  }
  
  def eachLineWithNumber(func: (String, Int)=>Unit){
    var i = 0
    eachLine{ ln => i += 1; func(ln, i) }    
  }
  
  def eachByte(process: (Byte)=>Unit)={
    eachBlock(blockSize){ buf => for(b <- buf) process(b) }
  }
  
  def eachBlock(size:Int)(process: (Array[Byte])=>Unit)={
		val buf = new Array[Byte](size)
    var len = 0
    val instream = new FileInputStream(this)
    try{
      while( {len = instream.read(buf); len != -1} ){
        process(buf.subArray(0,len))
      }
		}finally{ instream.close }
  }
  protected def ensurePathExists{ 
    if(!this.getParentFile.exists) 
      this.getParentFile.mkdirs //auto creates dir if doesn't already exists.
  }
  def writeText(text: String)={
    ensurePathExists
    val writer = new FileWriter(this)
    try{ writer.write(text) }
    finally{ writer.close }
  }  
  //NewLine is expected on input.
  def writeLines(lines: Iterator[String]) ={
    ensurePathExists
    val writer = new FileWriter(this)
    try{ for(ln <- lines) writer.write(ln) }
    finally{ writer.close }
  }  
  def readText: String ={
    val sb = new StringBuilder()
		var cbuf = new Array[Char](blockSize)
    var len = 0
    val reader = new BufferedReader(new FileReader(this))
    try{
      while( {len = reader.read(cbuf); len != -1} ){
        sb.append(cbuf,0,len)
      }
    }finally{ reader.close() }
    sb.toString
  }
  //NewLine is preserved.
  def readLines = scala.io.Source.fromFile(this).getLines
}

/**
 * A class that handle zip and unzip thiss.
class RichZipFile(file: File) extends RichFile(file){
  def this(name: String) = this(new File(name))
  
  val entries = new collection.mutable.HashSet[File]
  var rootPrefix = ""  
  
  def add(files: File*): RichZipFile ={
    for(f <- files) entries += f
    this
  }
  
  def zip(files: File*){
    add(files)
    zip
  }
  
  def zip{    
    if(entries.size<1)
      throw new Exception("Can not zip empty entries.")
      
    ensurePathExists
    
    val zipStream = new ZipOutputStream(new FileOutputStream(this));
    def addFileToZipStream(path: String, this: File){
      zipStream.putNextEntry(new ZipEntry(path.substring(1) + this.getName))
      eachBlock(MB){ buf => zipStream.write(buf) }
      zipStream.closeEntry();
    }
    def traverse(prefix:String, entry:DirFile){     
      if(entry.isDirectory){
        entry.eachDirFile{dirFile=>traverse(prefix+"/"+entry.getName, dirFile)}
      }else{
        addFileToZipStream(prefix+"/", new BinaryFile(entry))
      }
    }
    try{ 
      entries.foreach{traverse(rootPrefix, _)}
      zipStream.finish();
    }finally{ 
      zipStream.close
    }
  }
  
  def listzip:List[String]={
    val javaZipFile = new java.util.zip.ZipFile(this)
    val enm = javaZipFile.entries
    var ls = new collection.mutable.ListBuffer[String]
    while(enm.hasMoreElements){
      ls += enm.nextElement.toString
    }
    ls.toList
  }
  
  def unzip():Unit={ unzipTo(null) }
  def unzipTo(dest:File)={
    val javaZipFile = new java.util.zip.ZipFile(this)
    val enm = javaZipFile.entries
    while(enm.hasMoreElements){
      val zipEntry = enm.nextElement
      if(!zipEntry.isDirectory){
        val destthis = new BinaryFile(dest, zipEntry.toString)
        if(destthis.getParentFile != null && !destthis.getParentFile.exists)
          destthis.getParentFile.mkdirs
        val outStream = new FileOutputStream(destthis) 
        val inStream = javaZipFile.getInputStream(zipEntry)
        try{
          var len = 0        
          val buf = new Array[Byte](MB)
          while( {len = inStream.read(buf); len != -1} ){
            outStream.write(buf, 0, len)
          }
        }finally{
          outStream.close
          inStream.close
        }
      }
    }
  }
} */

