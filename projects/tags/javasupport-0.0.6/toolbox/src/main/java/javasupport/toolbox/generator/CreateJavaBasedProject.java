package javasupport.toolbox.generator;

import java.util.List;

public class CreateJavaBasedProject extends ProjectGenerator {
	
	public CreateJavaBasedProject(Project project, String outputPath){
		super(project, outputPath);
	}
	
	@Override
	public void init() throws Exception {
		if(outputDir.exists())
			throw new Exception("Output directory already exists. Dir: " + outputDir.getAbsolutePath());
		System.out.println("Creating webapp at "+outputDir.getAbsolutePath());
	}

	protected String getOutputFilename(String filePathname) {
		List<String> names = RegexUtils.findGroupsByDelimeters(filePathname, "R_", "_R", "R_");
		for(String name : names){
			if(name.equals("packageName")){
				String packageNameToPath = project.getPackageName().replaceAll("\\.", "/");
				filePathname = filePathname.replaceAll("R_"+name+"_R", packageNameToPath);
			}else{
				throw new RuntimeException("Unknown variable " + name+" in pathname.");
			}
		}
		return filePathname;
	}
}
