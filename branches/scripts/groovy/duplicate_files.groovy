file = args[0]
dest = args[1]
n = args[2].toInteger()

antbuilder = new AntBuilder()
file_ = new File(file)
dest_ = new File(dest)
ext = file_.name.substring(file_.name.lastIndexOf("."))
basename = file_.name.substring(0, file_.name.lastIndexOf("."))

n.times{ i ->
	to = basename + "_" + i + ext
	destfile = new File(dest_, to)
	println("Duplicating to $destfile")
	antbuilder.copy(file : file, tofile : destfile)
}

/*import org.apache.commons.io.FileUtils
file = args[0]
dest = args[1]
n = args[2].toInteger()
n.times{ i ->
	ext = FileUtils.getExtension(file)
	to = FileUtils.getBaseName(file) + "_" + i + ext
	destfile = new File(new File(dest), to)
	println("Duplicating to $destfile")
	FileUtils.copy(file, destfile)
}*/
