/**
 * Classes that support java.io.File better and provide more convinient Dir, BinaryFile, TextFile manipulation.
 */
package toolbox.scalasupport

import java.io._
import scala.io.Source

object RichFile{
  val DEFAULT_BLOCK_SIZE = RichStream.MB
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
  
  def parseFileExt(sep: String): (String, String) = { 
    val name = getName
    val idx = name.lastIndexOf(sep)
    (name.substring(0, idx), name.substring(idx+1))
  }
  def getExt = parseFileExt(".")._1
  def getBasename = parseFileExt(".")._2
  
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
    if(!isFile) throw new Exception("Source is not a file.")
    
    //if dest is a directory, then copy into it. else overwrite dest this.
    if(dest.isDirectory){
      val destFile = new File(dest, this.getName)
      RichStream.copyStream(new FileInputStream(this), new FileOutputStream(destFile), blockSize)
    }else{
      RichStream.copyStream(new FileInputStream(this), new FileOutputStream(dest), blockSize)    
    }
  }
  def eachLine(process: (String)=>Unit){
		RichStream.eachLine(new FileInputStream(this)){ ln => process(ln) }
  }
  
  def eachLineWithNumber(func: (String, Int)=>Unit){
    var i = 0
    eachLine{ ln => i += 1; func(ln, i) }    
  }
  
  def eachByte(process: (Byte)=>Unit){
    eachBlock(blockSize){ buf => for(b <- buf) process(b) }
  }
  
  def eachBlock(size:Int)(process: (Array[Byte])=>Unit){
		RichStream.eachBlock(new FileInputStream(this), size){ buf => process(buf) }
  }
  protected def ensurePathExists{ 
    if(!this.getParentFile.exists) 
      this.getParentFile.mkdirs //auto creates dir if doesn't already exists.
  }
  def writeText(text: String)={
    ensurePathExists
    withPrintWriter{ out => out.println(text) }
  }  
  //NewLine is expected on input.
  def writeLines(lines: Iterator[String]){
    ensurePathExists
    withPrintWriter{ out => for(ln <- lines) out.println(ln) }
  }  
  def withWriter(func: Writer=>Unit){
    val writer = new BufferedWriter(new FileWriter(this));
    try{ func(writer)}
    finally{ writer.close }
  }  
  def withPrintWriter(func: PrintWriter=>Unit){
    withWriter{ writer =>
      val out = new PrintWriter(writer)
      try{ func(out)}
      finally{ out.close }
    }
  }
  def readText: String = {
    val sep = File.separator
    val sb = new StringBuilder()
    RichStream.eachLine(new FileInputStream(this)){ ln => sb.append(ln); sb.append(sep) }
    sb.toString
  }
  
  def readLines: List[String] = {
    val lb = new scala.collection.mutable.ListBuffer[String]
    RichStream.eachLine(new FileInputStream(this)){ ln => lb.append(ln) }
    lb.toList
  }
}

/**
 * A class that handle zip and unzip thiss.
import java.util.zip._
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

