package javasupport.toolbox.generator;

import java.io.File;
import java.io.FileFilter;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.util.HashMap;

import javasupport.toolbox.generator.FileUtils.FileProcess;

import freemarker.template.Configuration;
import freemarker.template.Template;

public abstract class ProjectGenerator {
	
	protected Project project;
	protected Configuration config;
	protected HashMap<String, Object> model;
	protected File outputDir;
	protected File templateDir;
	protected String templatePathname;
	
	
	public ProjectGenerator(Project project, String outputPath){
		this.project = project;
		outputDir = new File(outputPath+"/"+project.getName());
		templateDir = new File(project.getTemplatePath()+"/"+getTemplateSetName());	
		model = new HashMap<String, Object>();
		config = new Configuration();
		templatePathname = FileUtils.getFilePathname(templateDir);
	}
	
	protected abstract String getTemplateSetName();

	protected String getOutputFilename(String filePathname){
		return filePathname;
	}
	
	public abstract void init() throws Exception;
	
	public void destroy() throws Exception { }
	
	public void run() throws Exception{
		
		config.setDirectoryForTemplateLoading(templateDir);	
		config.setTagSyntax(Configuration.SQUARE_BRACKET_TAG_SYNTAX);

		model.put("project", project);
		
		//creates all dirs
		FileUtils.eachDir(templateDir, getTemplateFileFilter(), getDirCopyDirProcess());
		
		//process all ftl file.
		FileUtils.walk(templateDir, getTemplateFileFilter(), getTemplateFileProcess());
	}
	
	private FileProcess getDirCopyDirProcess() {
		return new FileUtils.FileProcess(){
			public void process(File file) throws Exception {
				String filePathname = FileUtils.getFilePathname(file);				
				filePathname = filePathname.substring(templatePathname.length());				
				File outFile = new File(outputDir, getOutputFilename(filePathname));
				outFile.mkdirs();
			}
		};
	}

	protected FileUtils.FileProcess getTemplateFileProcess(){
		return new FileUtils.FileProcess(){
			public void process(File file) throws Exception {
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
		};
	}
	
	protected FileFilter getTemplateFileFilter(){
		return new FileFilter(){
			public boolean accept(File file) {
				if(file.getName().startsWith("."))
					return false;
				return true;
			}		
		};
	}	
}
