// NOTE: The groovy.bat -cp option will not take wildcard "*"!

// Load required libraries
// =============================================================================
def loadJar(name) {
	def loader = this.class.classLoader.rootLoader
	def load_ = { file -> 
		println(file)
		loader.addURL(file.toURI().toURL())
	}	
	def file = new File(name)
	if (file.name.endsWith(".jar")) { load_(file) } 
	else {
		if (file.isDirectory()) {
			file.listFiles().findAll{ it.name.endsWith('.jar') }.each{ load_(it) }
		}
	}
}
loadJar("/jb/client")
loadJar("/jb/common/lib/jbosssx.jar")

