out = "groovycp.bat"
dirs = args.size() <= 0 ? ["."] : args
result = []
dirs.each{ name -> 
		new File(name).eachFileRecurse{ file ->
		if(file.name =~ /\.jar/){
			result << file.absolutePath
		}
	}
}
println("Creating groovy startup file with -cp set.")
new File(out).write("@echo off\ngroovy -cp \"${result.join(File.pathSeparator)}\" %*\n")
println(out)

