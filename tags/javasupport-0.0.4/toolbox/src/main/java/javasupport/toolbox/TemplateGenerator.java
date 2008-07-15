package javasupport.toolbox;

import javasupport.jdk.lang.CliApplication;
import javasupport.toolbox.generator.CreateTomcatInstance;
import javasupport.toolbox.generator.CreateWebappBasic;
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
		
		if(subCommandName.equals("create-webapp-basic")){
			if(args.length<2){
				throw new Exception("Invalid arguments: create-webapp-basic <project-name>");
			}
			Project project = new Project(args[1], javasupportVersion, templatePath, templateSetName);
			runGenerator(new CreateWebappBasic(project, "."));			
		}else if(subCommandName.equals("create-tomcat-instance")){
			if(args.length<3){
				throw new Exception("Invalid argument: craete-tomcat-instance <tomcat-path> <tomcat-intance-name>");
			}
			Project project = new Project(args[2], javasupportVersion, templatePath, templateSetName);
			runGenerator(new CreateTomcatInstance(project, ".", args[1]));
		}else{
			throw new Exception("Unknow subCommandName " + subCommandName);
		}
		
		
	}
	
	private static void runGenerator(ProjectGenerator gen) throws Exception{
		gen.init();
		gen.run();	
		gen.destroy();		
	}

	private static void printExitUsage() {
		System.out.println("template [options] <subCommand> [subCommandArguments]");
		System.out.println("  [options]");
		System.out.println("  -v<VERSION>  Change javasupport version.");
		System.out.println("  -t<DIR>      Change templates directory.");
		System.out.println("  -s<DIR>      Change templates set name.");
		System.exit(0);
	}
}
