package javasupport.toolbox.generator;

import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.util.HashMap;
import java.util.List;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class CreateWebappBasic {
	private Project project;
	private HashMap<String, Object> model;
	private Configuration config;
	private File outputDir;
	private File templateDir;
	
	public CreateWebappBasic(Project project, String outputPath){
		this.project = project;
		outputDir = new File(outputPath+"/"+project.getName());
		templateDir = new File(project.getTemplatePath()+"/create-webapp-basic");	
		model = new HashMap<String, Object>();
		config = new Configuration();
	}
	
	public void generate() throws Exception{
		if(outputDir.exists())
			throw new Exception("Output directory already exists. Dir: " + outputDir.getAbsolutePath());
		System.out.println("Creating project at "+outputDir.getAbsolutePath());
		
		config.setDirectoryForTemplateLoading(templateDir);	
		config.setTagSyntax(Configuration.SQUARE_BRACKET_TAG_SYNTAX);
		model.put("project", project);		
		walk(templateDir);
	}

	private void walk(File dir) throws Exception {
		FilenameFilter filter = new FilenameFilter(){
			public boolean accept(File dir, String name) {
				if(dir.getName().equals(".svn"))
					return false;
				if(name.startsWith("."))
					return false;
				return true;
			}		
		};
		for(File file: dir.listFiles(filter)){
			if(file.isDirectory())
				walk(file);
			else
				process(file);
		}
	}

	private void process(File file) throws Exception {
		String templatePathname = FileUtils.getFilePathname(templateDir);
		String filePathname = FileUtils.getFilePathname(file);
		
		filePathname = filePathname.substring(templatePathname.length());
		Template template = config.getTemplate(filePathname);
		
		File outFile = new File(outputDir, getOutputFilename(filePathname));
		System.out.println("Creating file: "+ FileUtils.getFilePathname(outFile));
		
		if(!outFile.getParentFile().exists())
			outFile.getParentFile().mkdirs();
		FileWriter out = new FileWriter(outFile);
		template.process(model, out);
		out.close();
	}

	private String getOutputFilename(String filePathname) {
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
