package javasupport.toolbox.generator;

import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.util.HashMap;

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
		outputDir = new File(outputPath);
		templateDir = new File(project.getTemplatePath());	
		model = new HashMap<String, Object>();
		config = new Configuration();
	}
	
	public void generate() throws Exception{
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
		File outFile = new File(outputDir, filePathname);
		if(!outFile.getParentFile().exists())
			outFile.getParentFile().mkdirs();
		FileWriter out = new FileWriter(new File(outputDir, filePathname));
		template.process(model, out);
		out.close();
	}	
	
	
}
