import groovy.text.*
class GenerateSpringMvcFromModel extends GenerateSpringMvc {
	static void main(args){
		if(args.size()<2)
		  throw new Exception("No enough arguments. Try: type(scaffold|model|dao|create|list|delete|update|show) packageName.ClassName")
		
		def main = new GenerateSpringMvcFromModel()
		main.data.scaffoldType = args[0]
		main.data.className = args[1]
		main.data.scriptName = System.properties['script.name']
		//main.data.scriptName = System.properties['user.home'] + "/projects/java/javasupport/template-generator/GenerateSpringMvc.groovy"
		main.run()
	}
	
	def init(){		
		//auto reflect all fields from model bean
		def modelClass = Class.forName(data.className)
		def bi = java.beans.Introspector.getBeanInfo(modelClass)
		data.fields = bi.propertyDescriptors.collect{ it.displayName }
		
		super.init()
	}
}
