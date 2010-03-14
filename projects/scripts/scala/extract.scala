/**
Extract a zip or jar files in a consitent destination.

@author Zemian Deng (dengz1) Dec 17, 2008
*/

import java.io._
import java.util.jar._

def extractJar(file : File) : Unit = {

  def copyStream(istream : InputStream, ostream : OutputStream) : Unit = {
    var bytes =  new Array[Byte](1024)
    var len = -1
    while({ len = istream.read(bytes, 0, 1024); len != -1 })
      ostream.write(bytes, 0, len)
  }
  
  val basename = file.getName.substring(0, file.getName.lastIndexOf("."))
  val todir = new File(file.getParentFile, basename)
  val jar = new JarFile(file)
  val enu = jar.entries
  
  println("===> Processing File: " + file)
  todir.mkdirs()
  while(enu.hasMoreElements){
    val entry = enu.nextElement
    val name = entry.getName
    val entryPath = 
      if(name.startsWith(basename)) name.substring(basename.length) 
      else name
      
    println("Extracting to " + todir + "/" + entryPath)
    if(entry.isDirectory){
      new File(todir, entryPath).mkdirs
    }else{
      val istream = jar.getInputStream(entry)
      val ostream = new FileOutputStream(new File(todir, entryPath))
      copyStream(istream, ostream)
      ostream.close
      istream.close
     }
  }
}

def accept(file : File) : Boolean = 
  List(".zip", ".jar").find{ ext => file.getName.endsWith(ext) } != None

//Extract single zip/jar files or all of them in a directory.
args.foreach{ n => 
  val fn = new File(n)
  if(fn.isDirectory){
    val files = fn.listFiles.filter{ f => accept(f) }
    files.foreach{ f => extractJar(f) }
  }else if(accept(fn))
    extractJar(fn)
}
