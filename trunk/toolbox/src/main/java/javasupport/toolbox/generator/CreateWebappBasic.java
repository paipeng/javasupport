package javasupport.toolbox.generator;

import java.io.File;
import java.io.FileWriter;
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

		config.setDirectoryForTemplateLoading(templateDir);	

		model.put("project", project);
		
		walk(templateDir);
	}

	private void walk(File dir) throws Exception {
		for(File file: dir.listFiles()){
			if(file.isDirectory())
				walk(file);
			else
				process(file);
		}
	}

	private void process(File file) throws Exception {
		String name = file.getName();
		Template template = config.getTemplate(name);
		FileWriter out = new FileWriter(new File(outputDir, name));
		template.process(model, out);
	}	
}
