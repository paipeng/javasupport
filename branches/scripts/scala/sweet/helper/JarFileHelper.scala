package sweet.helper

// file: JarFileHelper.scala
import java.io._
import java.util.jar._

class JarFileHelper(jarFile : JarFile) {
  def this(filename : String) = this(new JarFile(filename))
  
  lazy val jarFileDir = new File(jarFile.getName).getParentFile
  lazy val jarFileName = new File(jarFile.getName).getName
  lazy val extractDir = {
    val jarBasename = jarFileName.substring(0, jarFileName.lastIndexOf("."))
    val firstEntry = jarFile.entries.nextElement
    val entryName = firstEntry.getName
    val isBasenameInEntryPath = entryName.startsWith(jarBasename)
    
    val todir = 
      if(isBasenameInEntryPath) jarFileDir
      else new File(jarFileDir, jarBasename)
    todir
  }
  
  def copyStream(istream : InputStream, ostream : OutputStream) : Unit = {
    var bytes =  new Array[Byte](1024)
    var len = -1
    while({ len = istream.read(bytes, 0, 1024); len != -1 })
      ostream.write(bytes, 0, len)
  }
  
  def jarEntries : Iterator[JarEntry] = {
    val enu = jarFile.entries
    new Iterator[JarEntry] {
      def hasNext = enu.hasMoreElements
      def next = enu.nextElement
    }
  }
  
  def extractEntry(jarEntry: JarEntry, todir : File) : Unit = {
    val entryPath = jarEntry.getName
    if(jarEntry.isDirectory){
      new File(todir, entryPath).mkdirs
    }else{
      val istream = jarFile.getInputStream(jarEntry)
      val ostream = new FileOutputStream(new File(todir, entryPath))
      copyStream(istream, ostream)
      ostream.close
      istream.close
    }
  }
  
  def extract : Unit = {
    jarEntries.foreach{ entry => extractEntry(entry, extractDir) }
  }
}

object JarFileHelper{
	implicit def jarfileHelper(jarfile: JarFile) = new JarFileHelper(jarfile)
}
