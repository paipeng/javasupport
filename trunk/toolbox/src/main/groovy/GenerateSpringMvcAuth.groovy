/**
 * This generator will blindly copy some templates for authentication uses.
 * It assume the following:
 *   You created a <packagename>.user.User model
 *   It will generate everything under <packagename>.auth
 */
import groovy.text.*
class GenerateSpringMvcAuth {
	def templateEngine = new GStringTemplateEngine()
	
	
	static void main(args){
		if(args.size()<1)
		  throw new Exception("No enough arguments. Try: packageName <before user package.>")
		
		def packageName = args[0]		
		new GenerateSpringMvcAuth().run(packageName)
	}
	
		
	def run(packageName){
		def data = new Expando()
		data.packageName = packageName
		data.packagePath = packageName.replaceAll("\\.", "/")
		def controllerPath = "src/main/java/"+data.packagePath+"/auth"
		def viewPath = "src/main/webapp/WEB-INF/view/auth"
		new File(controllerPath).mkdirs()
		new File(viewPath).mkdirs()
		
		
		copy("webapp-servlet-auth-controllers.xml", "templates/spring/auth", "src/main/webapp/WEB-INF", data)
		copy("AuthDao.java                           ", "templates/spring/auth/controller", controllerPath, data)
		copy("EditProfileController.java             ", "templates/spring/auth/controller", controllerPath, data)
		copy("LoginController.java                   ", "templates/spring/auth/controller", controllerPath, data)
		copy("LogoutController.java                  ", "templates/spring/auth/controller", controllerPath, data)
		copy("PasswordNotMatchException.java         ", "templates/spring/auth/controller", controllerPath, data)
		copy("SecuredUserInterceptor.java            ", "templates/spring/auth/controller", controllerPath, data)
		copy("ShowProfileController.java             ", "templates/spring/auth/controller", controllerPath, data)
		copy("UserConstants.java                     ", "templates/spring/auth/controller", controllerPath, data)
		copy("UsernameNotFoundException.java         ", "templates/spring/auth/controller", controllerPath, data)
		copy("editProfile.ftl  ", "templates/spring/auth/view", viewPath, data)
		copy("login.ftl        ", "templates/spring/auth/view", viewPath, data)
		copy("logout.ftl       ", "templates/spring/auth/view", viewPath, data)
		copy("showProfile.ftl  ", "templates/spring/auth/view", viewPath, data)
	}
	
	def copy(fn, from, to, data){
		fn = fn.trim()
		println("Creating from ${from+"/"+fn} to ${to+"/"+fn}")
		
		def url = getClass().getResource(from+"/"+fn)
		def templateRet = templateEngine.createTemplate(url).make(data.properties)
		new File(to+"/"+fn).write(templateRet.toString())
	}
}
