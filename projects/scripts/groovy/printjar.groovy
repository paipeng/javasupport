// Display the content of a jar entry
// usage: groovy printjar.groovy myjar /pkg/path/to/any/text/entry
// @author Zemian Deng (dengz1) Jan 13, 2008

import java.util.jar.*
  
filename = args[0]
printEntry = "META-INF/MANIFEST.MF"
if(args.length >= 2)
	printEntry = args[1]
//println([filename, printEntry])

jar = new JarFile(filename)
entries = jar.entries()
entries.each{ entry ->
	entryPath = entry.name
	if(printEntry == "--list"){
		println(entryPath)	
	}
	if(entryPath == printEntry){
		println()
		jar.getInputStream(entry).eachLine{ ln -> println(ln) }
	}
}