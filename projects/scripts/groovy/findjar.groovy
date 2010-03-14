// Display the content of a jar entry
// usage: groovy printjar.groovy myjar /pkg/path/to/any/text/entry
// @author Zemian Deng (dengz1) Jan 13, 2008

import java.util.jar.*
  
search = args[0]
dirs = ["."]
if (args.length > 1) dirs = args[1..-1]

if(search.indexOf('.') > 0) search = search.replaceAll('\\.', '/')
	
dirs.each { dir -> 
	new File(dir).eachFileRecurse { file ->
		if (file.name.endsWith(".jar")) {
			try {
				jar = new JarFile(file)
				entries = jar.entries()
				entries.each{ entry ->
					name = entry.name
					if (name.indexOf(search) >=0) {
						println("${file.toString().replaceAll('\\\\', '/')}: ${name}, ${name.replaceAll('/', '.')}")
					}
				}		
			} catch (Exception e) {
				System.err.println("skipped ${file}: ${e}")
			}
		}
	}
}
