/**
 * Classes that support java.io.File better and provide more convinient Dir, BinaryFile, TextFile manipulation.
 */
package toolbox.scalasupport

import java.io.{File, FileInputStream, FileOutputStream, BufferedReader, FileReader, FileWriter}
import java.util.zip.{ZipOutputStream, ZipInputStream, ZipEntry}
import scala.io.Source
import java.net.URI
import RichStream.{MB, copyStream}

/**
 * A class that handle dir and it's sub dirs and files content.
 * @author Zemian Deng
 */
class DirFile(parent:File, filename:String) extends File(parent, filename) {
  def this(filename:String)={
    this(null, filename)
  }
  def this(file:File)={    
    this(file.getParentFile, file.getName)
  }
  
  /** process each sub-files or sub-dirs in this dir */
  def eachDirFile(processDirFile: (DirFile)=>Unit)={
    val files = this.listFiles.toList.sort((e1,e2)=>e1.compareTo(e2)<0)
    files.foreach{dirFile=>processDirFile(new DirFile(dirFile))}
  }
  
  /** process each sub-files in this dir */
  def eachFile(processFile: (File)=>Unit)={
    val files = this.listFiles.toList.sort((e1,e2)=>e1.compareTo(e2)<0)
    files.foreach{file=>
      if(file.isFile) processFile(file)
    }
  }
  /** process each sub-dirs in this dir */
  def eachDir(processDir: (DirFile)=>Unit)={
    val files = this.listFiles.toList.sort((e1,e2)=>e1.compareTo(e2)<0)
    files.foreach{dir=>
      if(dir.isDirectory) processDir(new DirFile(dir))
    }
  }
        
  /** process each sub-files in each recursive dir. */
  def eachRecursiveFile(processFile: (File)=>Unit)={
    def traverse(dir:File){
      val files = dir.listFiles.toList.sort((e1,e2)=>e1.compareTo(e2)<0)
      files.foreach{file=>
        if(file.isDirectory) traverse(file)
        else processFile(file)
      }
    }
    traverse(this)
  }
  /** process each sub-dirs in each recursive dir. process will get call on each end leaf. */
  def eachRecursiveDir(processDir: (DirFile)=>Unit)={
    def traverse(dir:File){
      val files = dir.listFiles.toList.sort((e1,e2)=>e1.compareTo(e2)<0)
      files.foreach{file=>
        if(file.isDirectory) {
          traverse(file)
          processDir(new DirFile(file))
        }
      }      
    }
    traverse(this)
  }
  def deleteAll()={
    this.eachRecursiveFile{_.delete}
    this.eachRecursiveDir{_.delete}
    this.delete
  }
}

/**
 * A class that handle binary content.
 */
class BinaryFile(parent:File, filename:String) extends File(parent, filename){
  
  def this(filename:String)={
    this(null, filename)
  }
  def this(file:File)={    
    this(file.getParentFile, file.getName)
  }
    
  def eachByte(process: (Byte)=>Unit)={
    var b:Int = -1
    val instream = new FileInputStream(this)
    try{
      while( {b = instream.read; b != -1} ){
        process(b.toByte)
      }
		}finally{ instream.close }
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
  
  def copyTo(file:File)={
    //if dest is a directory, then copy into it. else overwrite dest file.
    val dest = {
      if(file.isDirectory) new BinaryFile(file, this.getName)
      else new BinaryFile(file)
    }
    copyStream(new FileInputStream(this), new FileOutputStream(dest))    
  }
}
    
/**
 * A class that handle text content.
 */
class TextFile(parent:File, filename:String) extends BinaryFile(parent, filename) {  
  def this(filename:String)={
    this(null, filename)
  }
  def this(file:File)={    
    this(file.getParentFile, file.getName)
  }
    
  /** process each line from this text file */
  def eachLine(process: (String)=>Unit)={
    for(ln <- readLines) process(ln)
  }
  
  def writeText(text:String)={
    if(getParentFile != null && !getParentFile.exists) getParentFile.mkdirs
    val writer = new FileWriter(this)
    try{ writer.write(text) }
    finally{ writer.close }
  }  
  def writeLines(lines:List[String])={
    if(getParentFile != null && !getParentFile.exists) getParentFile.mkdirs
    val writer = new FileWriter(this)
    try{ for(ln <- lines) writer.write(ln) }
    finally{ writer.close }
  }  
  def readText():String={
    val sb = new StringBuilder()
    val MB = 1024*1024
		var cbuf = new Array[Char](MB)
    var len = 0
    val reader = new BufferedReader(new FileReader(this))
    try{
      while( {len = reader.read(cbuf); len != -1} ){
        sb.append(cbuf,0,len)
      }
    }finally{ reader.close() }
    sb.toString
  }
  def readLines():List[String]={
    val lines = new collection.mutable.ListBuffer[String]
		var line:String = null
    val reader = new BufferedReader(new FileReader(this))
    try{
      while( {line = reader.readLine; line != null} ){
        lines += line
      }
    }finally{ reader.close() }
    lines.toList
  }
}


/**
 * A class that handle zip and unzip files.
 */
class ZipFile(parent:File, filename:String) extends BinaryFile(parent, filename) {
  def this(filename:String)={
    this(null, filename)
  }
  def this(file:File)={    
    this(file.getParentFile, file.getName)
  }
  
  val entries = new collection.mutable.HashSet[DirFile]
  var rootPrefix = ""
  
  
  def add(files:DirFile*):ZipFile={
    files.foreach{ entries += _ }
    this
  }
  
  def zip()={    
    if(entries.size<1)
      throw new RuntimeException("Can not zip empty entries file.")
      
    if(getParentFile != null && !getParentFile.exists) getParentFile.mkdirs
    
    val zipStream = new ZipOutputStream(new FileOutputStream(this));
    try{ 
      def addFileToZipStream(path:String, file:BinaryFile){
        zipStream.putNextEntry(new ZipEntry(path.substring(1) + file.getName))
        file.eachBlock(MB){ buf => zipStream.write(buf) }
        zipStream.closeEntry();
      }
      def traverse(prefix:String, entry:DirFile){     
        if(entry.isDirectory){
          entry.eachDirFile{dirFile=>traverse(prefix+"/"+entry.getName, dirFile)}
        }else{
          addFileToZipStream(prefix+"/", new BinaryFile(entry))
        }
      }
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
        val destfile = new BinaryFile(dest, zipEntry.toString)
        if(destfile.getParentFile != null && !destfile.getParentFile.exists)
          destfile.getParentFile.mkdirs
        val outStream = new FileOutputStream(destfile) 
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
}
