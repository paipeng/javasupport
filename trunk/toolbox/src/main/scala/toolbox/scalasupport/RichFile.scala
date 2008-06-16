/**
 * Classes that support java.io.File better and provide more convinient Dir, BinaryFile, TextFile manipulation.
 */
package toolbox.scalasupport

import java.io.{File, FileInputStream, FileOutputStream, BufferedReader, FileReader, FileWriter}
import java.util.zip.{ZipOutputStream, ZipInputStream, ZipEntry}
import scala.io.Source
import java.net.URI
import RichStream.{MB, copyStream}

object RichFile{
  implicit def file2RichFile(f: File) = new RichFile(f)  
}

/**
 * Provide a rich file access to java.io.File.
 * @author Zemian Deng
 */
class RichFile(file: File) {
  def eachFile(func: File=>Unit){
    def traverse(traverseFile: File){
      if(traverseFile.isDirectory)
        for(subfile <- traverseFile.listFiles.toList.sort((f1,f2)=>f1.compareTo(f2)<0) )
          traverse(subfile)
      else
        func(traverseFile)
    }
    traverse(file) 
  }
  def eachDir(func: File=>Unit){
    def traverse(traverseDir: File){
      if(traverseDir.isDirectory){
        for(subdir <- traverseDir.listFiles.toList.sort((f1,f2)=>f1.compareTo(f2)<0) )
          traverse(subdir)
        func(traverseDir)
      }
    }
    traverse(file)
  }
  def deleteAll{
    eachFile{_.delete}
    eachDir{_.delete}
    file.delete
  }
    
  def copyTo(dest:File)={
    //if dest is a directory, then copy into it. else overwrite dest file.
    if(dest.isDirectory){
      val destFile = new File(dest, file.getName)
      copyStream(new FileInputStream(file), new FileOutputStream(destFile))
    }else{
      copyStream(new FileInputStream(file), new FileOutputStream(dest))    
    }
  }
  def eachLine(process: (String)=>Unit){
		var line: String = null
    val reader = new BufferedReader(new FileReader(file))
    try{
      while( {line = reader.readLine; line != null} ){
        process(line)
      }
    }finally{ reader.close() }
  }
  
  def eachLineWithNumber(func: (Int, String)=>Unit){
    var i = 0
    eachLine{ ln =>
      i += 1
      func(i, ln)
    }    
  }
  
  def eachByte(process: (Byte)=>Unit)={
    eachBlock(1){ buf => process(buf(0)) }
  }
  
  def eachBlock(size:Int)(process: (Array[Byte])=>Unit)={
		val buf = new Array[Byte](size)
    var len = 0
    val instream = new FileInputStream(file)
    try{
      while( {len = instream.read(buf); len != -1} ){
        process(buf.subArray(0,len))
      }
		}finally{ instream.close }
  }
  def writeText(text: String)={
    if(!file.getParentFile.exists) 
      file.getParentFile.mkdirs //auto creates dir if doesn't already exists.
    val writer = new FileWriter(file)
    try{ writer.write(text) }
    finally{ writer.close }
  }  
  def writeLines(lines: Iterator[String]) ={
    if(!file.getParentFile.exists) 
      file.getParentFile.mkdirs //auto creates dir if doesn't already exists.
    val writer = new FileWriter(file)
    try{ for(ln <- lines) writer.write(ln) }
    finally{ writer.close }
  }  
  def readText: String ={
    val sb = new StringBuilder()
    val MB = 1024*1024
		var cbuf = new Array[Char](MB)
    var len = 0
    val reader = new BufferedReader(new FileReader(file))
    try{
      while( {len = reader.read(cbuf); len != -1} ){
        sb.append(cbuf,0,len)
      }
    }finally{ reader.close() }
    sb.toString
  }
  def readLines = scala.io.Source.fromFile(file).getLines
}

/**
 * A class that handle zip and unzip files.
class RichZipFile(file: File) {
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
*/
