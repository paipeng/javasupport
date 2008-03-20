import groovy.text.*
class GenerateSpringMvc {
	def data = new Expando()	
	def templates = [
		'dao' :
			['jpa' : 'templates/spring/JPADao.java'],
		'model' :
			['javaBean' : 'templates/spring/JavaBeanModel.java'],
		'view' : 
			['create' : 'templates/spring/create.jsp',
			'edit' : 'templates/spring/edit.jsp',
			'list' : 'templates/spring/list.jsp',
			'show' : 'templates/spring/show.jsp',
			'delete' : 'templates/spring/delete.jsp'],
		'controller' : 
			['create' : 'templates/spring/CreateController.java',
			'edit' : 'templates/spring/EditController.java',
			'list' : 'templates/spring/ListController.java',
			'show' : 'templates/spring/ShowController.java',
			'delete' : 'templates/spring/DeleteController.java']
		]
	def templateEngine = new GStringTemplateEngine()
	def templateDir = "."
	
	static void main(args){
		if(args.size()<3)
		  throw new Exception("No enough arguments. Try: type(scaffold|model|dao|create|list|delete|update|show) packageName.ClassName fieldName1 [fieldName2 ...]")
		
		def main = new GenerateSpringMvc()
		main.data.scaffoldType = args[0]
		main.data.className = args[1]
		main.data.fields = args[2..-1]
		//main.data.scriptName = System.properties['script.name']
		main.data.scriptName = "/Users/thebugslayer/projects/java/javasupport/template-generator/GenerateSpringMvc.groovy"
		main.run()
	}
	
	def run(){	
		init()
		
		if(data.scaffoldType == "scaffold" || data.scaffoldType == "model"){
			createFromTemplate("src/main/java/${data.packagePath}/${data.className}.java", templates.model.javaBean)
			//println("mvn hibernate3:hbm2ddl".execute().text)
		}
		
		if(data.scaffoldType == "scaffold" || data.scaffoldType == "dao"){
			createFromTemplate("src/main/java/${data.packagePath}/${data.className}Dao.java", templates.dao.jpa)
			updateApplicationContextXml()
		}
		
		if(data.scaffoldType == "scaffold" || data.scaffoldType == "create"){
			createFromTemplate("src/main/java/${data.packagePath}/CreateController.java", templates.controller.create)
			createFromTemplate("src/main/webapp/${data.classNamePath}/create.jsp", templates.view.create)
		}
		
		if(data.scaffoldType == "scaffold" || data.scaffoldType == "edit"){
			createFromTemplate("src/main/java/${data.packagePath}/EditController.java", templates.controller.edit)
			createFromTemplate("src/main/webapp/${data.classNamePath}/edit.jsp", templates.view.edit)
		}
		
		if(data.scaffoldType == "scaffold" || data.scaffoldType == "delete"){
			createFromTemplate("src/main/java/${data.packagePath}/DeleteController.java", templates.controller.delete)
			createFromTemplate("src/main/webapp/${data.classNamePath}/delete.jsp", templates.view.delete)
		}
		
		if(data.scaffoldType == "scaffold" || data.scaffoldType == "list"){
			createFromTemplate("src/main/java/${data.packagePath}/ListController.java", templates.controller.list)
			createFromTemplate("src/main/webapp/${data.classNamePath}/list.jsp", templates.view.list)
		}
		
		if(data.scaffoldType == "scaffold" || data.scaffoldType == "show"){
			createFromTemplate("src/main/java/${data.packagePath}/ShowController.java", templates.controller.show)
			createFromTemplate("src/main/webapp/${data.classNamePath}/show.jsp", templates.view.show)
		}
		
		if(data.scaffoldType == "scaffold"){
			updateWebappServletContextXml()
		}
	}
	
	// updates for controllers
	def updateWebappServletContextXml(){
		def inputFilename = "src/main/webapp/WEB-INF/webapp-servlet-controller.xml"
		def outputFilename = "src/main/webapp/WEB-INF/webapp-servlet-controller.xml"
		def beansXml = new StringWriter()
		def operations = [ 'create', 'delete', 'list', 'edit', 'show']
		def urlControllerMappingKey = 'controllerUrlMappings'
		def urlMappingXmlPrefix = """
		<bean id="${urlControllerMappingKey}" class="org.springframework.web.servlet.handler.SimpleUrlHandlerMapping">
			<property name="mappings">
				<util:map>
		"""
		def urlMappingXmlSuffix = """
				</util:map>
			</property>
		</bean>
		"""
		
		if(new File(inputFilename).exists()){
			def out = new XmlNodePrinter(new PrintWriter(beansXml))				
			def beans = new XmlParser().parse(inputFilename)
			def opControllers = operations.collect{ it+"${data.className}Controller" }
			for(bean in beans.children()){
				def id = bean.'@id'
				if( !(id in opControllers || id == urlControllerMappingKey) ){ //reprint out all other beans.
					out.print(bean, null)
				}
			}
			
			def bean = beans.children().find{bean-> bean.'@id' == urlControllerMappingKey }
			if(bean){
				def entries = bean.children()[0].children()[0].children()
				def entriesXml = new StringWriter()
				def entriesXmlWriter = new XmlNodePrinter(new PrintWriter(entriesXml))
				def opUrls = operations.collect{ "/${data.classNamePath}/${it}" }
				for(entry in entries){
					if( !("${entry.'@key'}" in opUrls) ){
					entriesXmlWriter.print(entry, null) }
				}
				urlMappingXmlPrefix += entriesXml.toString()
			}
		}
			
		
		new File(outputFilename).withPrintWriter{ writer ->		
			writer.println('''<?xml version="1.0" encoding="UTF-8"?>
			<beans xmlns="http://www.springframework.org/schema/beans" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
				 xmlns:aop="http://www.springframework.org/schema/aop" xmlns:tx="http://www.springframework.org/schema/tx"
				 xmlns:util="http://www.springframework.org/schema/util" xmlns:lang="http://www.springframework.org/schema/lang"
				 xsi:schemaLocation="
					 http://www.springframework.org/schema/lang http://www.springframework.org/schema/lang/spring-lang-2.0.xsd
					 http://www.springframework.org/schema/util http://www.springframework.org/schema/util/spring-util-2.0.xsd
					 http://www.springframework.org/schema/tx http://www.springframework.org/schema/tx/spring-tx-2.0.xsd
					 http://www.springframework.org/schema/aop http://www.springframework.org/schema/aop/spring-aop-2.0.xsd
					 http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-2.0.xsd">
			''')
			
			//append url mapper.
			writer.println(urlMappingXmlPrefix)
			for(op in operations){
				writer.println("""<entry key="/${data.classNamePath}/${op}" value-ref="${op}${data.className}Controller" />""")	
			}
			writer.println(urlMappingXmlSuffix)
			
			for(op in operations){
				writer.println("""
				<bean id="${op}${data.className}Controller" class="${data.packageName}.${op[0].toUpperCase()+op[1..-1]}Controller">
					<property name="${data.beanName}Dao" ref="${data.beanName}Dao" />
				</bean>""")	
			}
			
			writer.println(beansXml.toString())
			
			writer.println("</beans>")
		}
	}
	
	// updates for DAOs
	def updateApplicationContextXml(){
		def inputFilename = "src/main/webapp/WEB-INF/applicationContext-Dao.xml"
		def outputFilename = "src/main/webapp/WEB-INF/applicationContext-Dao.xml"
		def beansXml = new StringWriter()
		
		if(new File(inputFilename).exists()){
			def out = new XmlNodePrinter(new PrintWriter(beansXml))				
			def beans = new XmlParser().parse(inputFilename)
			for(bean in beans.children()){
				if(bean.'@id' != "${data.beanName}Dao"){ //reprint out all other beans.
					out.print(bean, null)
				}
			}
		}
		
		new File(outputFilename).withPrintWriter{ writer ->		
			writer.println('''<?xml version="1.0" encoding="UTF-8"?>
			<beans xmlns="http://www.springframework.org/schema/beans" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
				 xmlns:aop="http://www.springframework.org/schema/aop" xmlns:tx="http://www.springframework.org/schema/tx"
				 xmlns:util="http://www.springframework.org/schema/util" xmlns:lang="http://www.springframework.org/schema/lang"
				 xsi:schemaLocation="
					 http://www.springframework.org/schema/lang http://www.springframework.org/schema/lang/spring-lang-2.0.xsd
					 http://www.springframework.org/schema/util http://www.springframework.org/schema/util/spring-util-2.0.xsd
					 http://www.springframework.org/schema/tx http://www.springframework.org/schema/tx/spring-tx-2.0.xsd
					 http://www.springframework.org/schema/aop http://www.springframework.org/schema/aop/spring-aop-2.0.xsd
					 http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-2.0.xsd">
			''')
			writer.println(beansXml.toString())
			writer.println("""
			<bean id="${data.beanName}Dao" class="${data.packageName}.${data.className}Dao">
				<property name="em" ref="entityManager" />
			</bean>""")			
			writer.println("</beans>")
		}
	}
	
	def createFromTemplate(output, template){
		template = templateDir+"/"+template //auto add path
		println("Creating " + output + " from " + template)
		def f = new File(template)
		def templateRet = templateEngine.createTemplate(f).make(data.properties)
		new File(output).write(templateRet.toString())
	}
	
	def init(){		
		// set template dir based on script name path
		if(data.scriptName != "")
			templateDir = new File(data.scriptName).getParentFile().getAbsolutePath()
		
		//find packageName
		def ret = data.className.split("\\.")
		if(ret.size()<2)
		  throw new Exception("Missing package name.")
		  
		//redefine className
		data.className = ret[-1]
		data.classNamePath = data.className.toLowerCase()
		data.packageName = ret[0..-2].join(".")
		data.packagePath = data.packageName.replaceAll("\\.", "/")
		data.beanName = data.className[0].toLowerCase()+data.className[1..-1]
		
		//make sure there is an id
		if(!data.fields.any{it =="id"})	data.fields += "id:Integer"
		
		//split fields into field, type, PropertyName, and Annotations.
		data.fields = data.fields.collect{ field->
			field = field[0].toLowerCase() + field[1..-1] //make sure first char is low case
			def parts = field.split(":").toList()
			if(parts.size()<2)
				parts << "String"
			
			//redifine some temp vars
			field = parts[0]
			
			def type = parts[1]
			if(type=="int")//we want Integer wrapper
				type = "Integer"
			type = type[0].toUpperCase()+type[1..-1] //Always cap first letter  on type.
			def lowCaseType = type.toLowerCase()
			
			parts << field[0].toUpperCase()+field[1..-1] //PropertyName
			
			//set annotations
			def annot = []
			if(field == "id"){
				annot << """@Id @GeneratedValue @Column(name = "id")"""
			}else if(type == data.className){
				//referencing itself(parent)
				annot << """@ManyToOne(cascade = CascadeType.ALL)"""
				annot << """@JoinColumn(name = "${field}_id")"""
			}else{
				//money
				if(type == "double"){
					annot << """@Column(name = "${field}", precision = 2)"""
				}else{
					annot << """@Column(name = "${field}")"""
				}
			}
			
			//IT's a date field.
			if(type == "date"){
				annot << """@Temporal(value = TemporalType.TIME)"""
			}else if(type == "byte[]"){
				annot << """@Lob"""
			}
			
			parts << annot.join(" ")
		}
		
		//create display fields
		data.displayFields = data.fields.findAll{ field ->
			!(field[0] == "id" || field[1] == data.className) 
		}
		
		//check to make sure you are in maven project dir
		//if(!new File("pom.xml").exists())
		//  throw new Exception("You are not in a Maven base directory.")
		
		if(!new File("src/main/java/${data.packagePath}").exists()){
			new File("src/main/java/${data.packagePath}").mkdirs()
		  //throw new Exception("Java source dir doesn't exists. Run mkdir -p src/main/java/${data.packagePath}")
		}
		
		if(!new File("src/main/webapp/${data.classNamePath}").exists()){
			new File("src/main/webapp/${data.classNamePath}").mkdirs()
		  //throw new Exception("Webapp view dir doesn't exists. Run mkdir -p src/main/webapp/${data.classNamePath}")
		}			
	}
}
