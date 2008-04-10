import groovy.text.*
class GenerateSpringMvcFromModel extends GenerateSpringMvc {
	static void main(args){
		if(args.size()<2)
		  throw new Exception("No enough arguments. Try: type(scaffold|model|dao|create|list|delete|update|show) packageName.ClassName [newPackageName]")
		
		def main = new GenerateSpringMvcFromModel()
		main.data.scaffoldType = args[0]
		main.data.className = args[1]
		main.data.newPackageName = args[2]
		main.data.scriptName = System.properties['script.name']
		//main.data.scriptName = System.properties['user.home'] + "/projects/java/javasupport/template-generator/GenerateSpringMvc.groovy"
		
		//auto reflect all fields from model bean
		def modelClass = Class.forName(main.data.className)
		def bi = java.beans.Introspector.getBeanInfo(modelClass)
		//main.data.fields = bi.propertyDescriptors.collect{ it.displayName + ':'+it.propertyType }
		main.data.fields = bi.propertyDescriptors.collect{ it.displayName }
		
		//replace with new package name
		if(main.data.newPackageName){
			main.data.origModelClassName = main.data.className
			def pkgs = main.data.className.split('\\.')
			main.data.className = main.data.newPackageName+'.'+pkgs[-1]
		}
		
		main.run()
	}
}
