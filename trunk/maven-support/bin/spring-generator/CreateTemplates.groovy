import groovy.text.*
class CreateTemplates {
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
	
	static void main(args){
		if(args.size()<2)
		  throw new Exception("No enough arguments. Try: packageName.ClassName fieldName1 [fieldName2 ...]")
		
		def main = new CreateTemplates()
		main.data.className = args[0]
		main.data.fields = args[1..-1]
		main.run()
	}
	
	def run(){	
		init()
		
		//javaBean
		createFromTemplate("src/main/java/${data.packagePath}/${data.className}.java", templates.model.javaBean)
		
		//DAO
		createFromTemplate("src/main/java/${data.packagePath}/${data.className}Dao.java", templates.dao.jpa)
		
		//Controllers
		createFromTemplate("src/main/java/${data.packagePath}/CreateController.java", templates.controller.create)
		createFromTemplate("src/main/java/${data.packagePath}/EditController.java", templates.controller.edit)
		createFromTemplate("src/main/java/${data.packagePath}/DeleteController.java", templates.controller.delete)
		createFromTemplate("src/main/java/${data.packagePath}/ListController.java", templates.controller.list)
				
		//view
		createFromTemplate("src/main/webapp/${data.classNamePath}/create.jsp", templates.view.create)
		createFromTemplate("src/main/webapp/${data.classNamePath}/edit.jsp", templates.view.edit)
		createFromTemplate("src/main/webapp/${data.classNamePath}/delete.jsp", templates.view.delete)
		createFromTemplate("src/main/webapp/${data.classNamePath}/list.jsp", templates.view.list)
	}		
	
	def createFromTemplate(output, template){
		println("Creating " + output + " from " + template)
		def f = new File(template)
		def templateRet = templateEngine.createTemplate(f).make(data.properties)
		new File(output).write(templateRet.toString())
	}
	
	def init(){		
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
