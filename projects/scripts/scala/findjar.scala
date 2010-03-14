import helper._

val search = args(0)
val files = args.drop(1)

val findjar = (file: java.io.File) => {
  //println("Searching " + file)
	val jarfile = new java.util.jar.JarFile(file)
	val entries = new JarFileHelper(jarfile).jarEntries.filter{ e => e.getName.contains(search) }
	entries.foreach{ e => println(file + " : " + e.getName) }
}

files.foreach{ fn =>
	val file = new java.io.File(fn)
	
	if(file.isFile && file.getName.endsWith(".jar")){
		findjar(file)
	}else{		
		new FileHelper(file).foreachFile{ file =>
      //println(file)
			if(file.getName.endsWith(".jar"))
				findjar(file)
		}
	}
}
