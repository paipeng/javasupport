package javasupport.toolbox;

import javasupport.jdk.lang.CliApplication;
import javasupport.toolbox.generator.CreateTomcatInstance;
import javasupport.toolbox.generator.CreateJavaBasedProject;
import javasupport.toolbox.generator.Project;
import javasupport.toolbox.generator.ProjectGenerator;

public class TemplateGenerator extends CliApplication {
	
	public static void main(String[] argv) throws Exception{
		String[] args = parseOptions(argv);
		if(hasOpt("h") || hasOpt("help")){
			printExitUsage();
		}
		
		if(args.length<1){
			throw new Exception("Missing subCommand name");
		}

		String toolHome = System.getProperty("toolhome", ".");
		String javasupportVersion = getOpt("v", "0.0.4");
		String templatePath = getOpt("t", toolHome+"/templates");
		
		String subCommandName = args[0];
		String templateSetName = getOpt("s", subCommandName);
		
		if(subCommandName.equals("create-tomcat-instance")){
			if(args.length<3){
				throw new Exception("Invalid argument: craete-tomcat-instance <tomcat-path> <tomcat-intance-name>");
			}
			Project project = new Project(args[2], javasupportVersion, templatePath, templateSetName);
			runGenerator(new CreateTomcatInstance(project, ".", args[1]));
		}else if(subCommandName.startsWith("create-webapp")){
			if(args.length<2){
				throw new Exception("Invalid arguments: Missing <project-name>");
			}
			Project project = null;
			String projName = args[1];
			String packageName = getOpt("p", null);
			if(packageName == null)
				project = new Project(projName, javasupportVersion, templatePath, templateSetName);
			else
				project = new Project(projName, javasupportVersion, templatePath, templateSetName, packageName);
			
			runGenerator(new CreateJavaBasedProject(project, "."));
		}else{
			//Just use subCommand as a template set of any project and run as generator.
			//This allow user to extends any template set and create their own project.
			if(args.length<2){
				throw new Exception("Invalid arguments: Missing <template-set-name>");
			}
			String projName = args[1];			
			Project project = new Project(projName, javasupportVersion, templatePath, templateSetName);
			runGenerator(new CreateJavaBasedProject(project, "."));
		}
	}
	
	private static void runGenerator(ProjectGenerator gen) throws Exception{
		gen.init();
		gen.run();	
		gen.destroy();		
	}

	private static void printExitUsage() {
		System.out.println("A template generator tool to create and/or setup project using a set of");
		System.out.println("Freemarker(FTL) templates files.");
		System.out.println("");
		System.out.println("Usage:");
		System.out.println("template.sh[.bat] [options] <subCommand>");
		System.out.println("");
		System.out.println("[options]");
		System.out.println("  -v<VERSION>  Change javasupport version.");
		System.out.println("  -t<DIR>      Change templates directory.");
		System.out.println("  -s<DIR>      Change templates set name.");
		System.out.println("");
		System.out.println("<subCommand>");
		System.out.println("  create-webapp-basic <project-name>");
		System.out.println("    Create a basic Servlet webapp project.");
		System.out.println("    [additional supported options]");
		System.out.println("      -p<PACKAGENAME> Specify package name.");
		System.out.println("");
		System.out.println("  create-webapp-spring <project-name>");
		System.out.println("    Create a SpringMVC+Jsp View webapp project.");
		System.out.println("    [additional supported options]");
		System.out.println("      -p<PACKAGENAME> Specify package name.");
		System.out.println("");
		System.out.println("  create-webapp-spring-ftl <project-name>");
		System.out.println("    Create a SpringMVC+Freemarker View webapp project.");
		System.out.println("    [additional supported options]");
		System.out.println("      -p<PACKAGENAME> Specify package name.");
		System.out.println("");
		System.out.println("  create-tomcat-instance <tomcat-home-path> <new-tomcat-base-name>");
		System.out.println("    Create and setup a new Tomcat(CATALINA_BASE) to be run as separated instance.");
		System.out.println("");
		System.out.println("  <any-template-set-dir-name> <project-name>");
		System.out.println("    Just provide your own template set under templates directory, and this command");
		System.out.println("    will substitube any FTL variables and generate your own project.");
		System.out.println("");
		System.exit(0);
	}
}
