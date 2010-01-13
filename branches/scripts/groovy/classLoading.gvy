/*
NOTE: 	As of groovy 1.7, the groovy.bat -cp option will not take wildcard "*"!
				The java main class: GroovyStarter will also accept --classpath and it will not take wildcard paths.
			 
				To use wildcard, add %CLASSPATH% in startGroovy.bat and then on your cmd session, manually set 
				CLASSPATH variable to run groovy.bat
			 
				In Cygwin, to execute groovy.bat, you MUST use "export CLASSPATH=blah" to let the .bat pick up the value
			 
				You may also load jars programatically as follow:         
*/
					
def loadjar(filename) {
	def file = new File(filename)
	if (file.isDirectory()) {
		file.listFiles().findAll{ it.name.endsWith('.jar') }.each{ loadjar(it.path) }
	} else {
		println("Loading: ${filename}")
		this.class.classLoader.rootLoader.addURL(file.toURI().toURL())
	}
}

//Usage example: load any jar or dir from arguments
args.each { loadjar(it) }

