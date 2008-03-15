import groovy.text.*
class Scaffold {
	def data = new Expando()	
	def templates = [
		'dao' :
			['jpa' : 'templates/JPADao.java'],
		'model' :
			['javaBean' : 'templates/JavaBeanModel.java'],
		'view' : 
			['create' : 'templates/create.jsp',
			'edit' : 'templates/edit.jsp',
			'list' : 'templates/create.jsp',
			'delete' : 'templates/delete.jsp'],
		'controller' : 
			['create' : 'templates/CreateController.java',
			'edit' : 'templates/EditController.java',
			'list' : 'templates/ListController.java',
			'delete' : 'templates/DeleteController.java']
		]
	def templateEngine = new GStringTemplateEngine()
	def templateDir = "."
	
	static void main(args){
		if(args.size()<3)
		  throw new Exception("No enough arguments. Try: scaffoldType(all|model|dao|create|list|delete|update) packageName.ClassName fieldName1 [fieldName2 ...]")
		
		def main = new Scaffold()
		main.data.scaffoldType = args[0]
		main.data.className = args[1]
		main.data.fields = args[2..-1]
		main.scriptName = System.properties['script.name']
		main.run()
	}
	
	def run(){	
		init()
		
		if(data.scalffoldType == "all" || data.scalffoldType == "model"){
			createFromTemplate("src/main/java/${data.packagePath}/${data.className}.java", templateDir+"/"+templates.model.javaBean)
		}
		
		if(data.scalffoldType == "all" || data.scalffoldType == "dao"){
			createFromTemplate("src/main/java/${data.packagePath}/${data.className}Dao.java", templateDir+"/"+templates.dao.jpa)
		}
		
		if(data.scalffoldType == "all" || data.scalffoldType == "create"){
			createFromTemplate("src/main/java/${data.packagePath}/CreateController.java", templateDir+"/"+templates.controller.create)
			createFromTemplate("src/main/webapp/${data.classNamePath}/create.jsp", templateDir+"/"+templates.view.create)
		}
		
		if(data.scalffoldType == "all" || data.scalffoldType == "edit"){
			createFromTemplate("src/main/java/${data.packagePath}/EditController.java", templateDir+"/"+templates.controller.edit)
			createFromTemplate("src/main/webapp/${data.classNamePath}/edit.jsp", templateDir+"/"+templates.view.edit)
		}
		
		if(data.scalffoldType == "all" || data.scalffoldType == "delete"){
			createFromTemplate("src/main/java/${data.packagePath}/DeleteController.java", templateDir+"/"+templates.controller.delete)
			createFromTemplate("src/main/webapp/${data.classNamePath}/delete.jsp", templateDir+"/"+templates.view.delete)
		}
		
		if(data.scalffoldType == "all" || data.scalffoldType == "list"){
			createFromTemplate("src/main/java/${data.packagePath}/ListController.java", templateDir+"/"+templates.controller.list)
			createFromTemplate("src/main/webapp/${data.classNamePath}/list.jsp", templateDir+"/"+templates.view.list)
		}
	}		
	
	def createFromTemplate(output, template){
		println("Creating " + output + " from " + template)
		def f = new File(template)
		def templateRet = templateEngine.createTemplate(f).make(data.properties)
		new File(output).write(templateRet.toString())
	}
	
	def init(){		
		// set template dir based on script name path
		if(data.scriptMain != "")
			templateDir = new File(data.scriptMain).getParent().getAbsolutePath()
		
		//find packageName
		def ret = data.className.split("\\.")
		if(ret.size()<2)
		  throw new Exception("Missing package name.")
		  
		//redefine className
		data.className = ret[-1]
		data.classNamePath = data.className.toLowerCase()
		data.packageName = ret[0..-2].join(".")+"."+data.classNamePath
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
				if(type.toLowerCase() == "double"){
					annot << """@Column(name = "${field}", precision = 2)"""
				}else{
					annot << """@Column(name = "${field}")"""
				}
			}
			
			//IT's a date field.
			if(type.toLowerCase() == "date"){
				annot << """@Temporal(value = TemporalType.TIME)"""
			}else if(type.toLowerCase() == "byte[]"){
				annot << """@Lob"""
			}
			
			parts << annot.join(" ")
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
