/**
Display the content of a jar entry

@author Zemian Deng (dengz1) Dec 17, 2008
*/

import java.io._
import java.util.jar._

def copyStream(istream : InputStream, ostream : OutputStream) : Unit = {
	var bytes =  new Array[Byte](1024)
	var len = -1
	while({ len = istream.read(bytes, 0, 1024); len != -1 })
	  	ostream.write(bytes, 0, len)
}
  
val filename = args(0)
val printEntry = if(args.length < 2) "META-INF/MANIFEST.MF" else args(1)

val jar = new JarFile(filename)
val enu = jar.entries

while(enu.hasMoreElements){
	val entry = enu.nextElement
	val entryPath = entry.getName
	//println(entryPath)	
	if(entryPath == printEntry){
		println("===== ENTRY " + entryPath)
		println()
		val istream = jar.getInputStream(entry)
		val ostream = System.out
		copyStream(istream, ostream)
		istream.close
	}
}
