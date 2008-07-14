package javasupport.toolbox.generator;

import java.util.List;

public class CreateWebappBasic extends ProjectGenerator {
	
	public CreateWebappBasic(Project project, String outputPath){
		super(project, outputPath);
	}
	
	@Override
	public void init() throws Exception {
		if(outputDir.exists())
			throw new Exception("Output directory already exists. Dir: " + outputDir.getAbsolutePath());
		System.out.println("Creating webapp at "+outputDir.getAbsolutePath());
	}

	@Override
	protected String getTemplateSetName() {
		return "create-webapp-basic";
	}	

	protected String getOutputFilename(String filePathname) {
		List<String> names = RegexUtils.findGroupsByDelimeters(filePathname, "R_", "_R", "R_");
		//System.out.println(names);
		for(String name : names){
			if(name.equals("packageName"))
				filePathname = filePathname.replaceAll("R_"+name+"_R", project.getPackageName());
			else
				throw new RuntimeException("Unknown variable " + name+" in pathname.");
		}
		return filePathname;
	}
}
