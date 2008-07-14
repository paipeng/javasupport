package javasupport.toolbox.generator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.InetAddress;
import java.util.List;
import java.util.Properties;

public class CreateTomcatInstance extends ProjectGenerator {
	protected String tomcatPath;
	protected File tomcatIntancesDir;
	protected Properties tomcatIntanceProps;
	protected String defaultHttpPort = "8080";
	protected String defaultShutdownPort = "8005";
	protected File instancePropsFile;
	protected String httpPort;
	protected String shutdownPort;
	protected String tomcatInstanceDirname = "instances";
	
	public CreateTomcatInstance(Project project, String outputPath, String tomcatPath) {
		super(project, outputPath);
		this.tomcatPath = tomcatPath;
		
		//update model
		model.put("tomcatPath", tomcatPath);
		model.put("tomcatInstanceName", project.getName());
		model.put("tomcatInstanceDirname", tomcatInstanceDirname);
		
		tomcatIntancesDir = new File(tomcatPath+"/"+tomcatInstanceDirname);
		if(!tomcatIntancesDir.exists())
			tomcatIntancesDir.mkdirs();		
		outputDir = new File(tomcatIntancesDir, project.getName());
		model.put("tomcatInstancePath", outputDir.getAbsoluteFile());
		
		instancePropsFile = new File(tomcatIntancesDir, "/instances.properties");
	}
	
	@Override
	public void init() throws Exception {
		if(outputDir.exists())
			throw new Exception("Output directory already exists. Dir: " + outputDir.getAbsolutePath());
		
		tomcatIntanceProps = new Properties();
		if(instancePropsFile.exists())
			tomcatIntanceProps.load(new FileInputStream(instancePropsFile));
		
		httpPort = tomcatIntanceProps.getProperty("httpPort", defaultHttpPort);
		shutdownPort = tomcatIntanceProps.getProperty("shutdownPort", defaultShutdownPort);
		
		httpPort = ""+ (Integer.parseInt(httpPort) + 1);
		shutdownPort = ""+ (Integer.parseInt(shutdownPort) + 1);
		
		tomcatIntanceProps.setProperty("httpPort", httpPort);
		tomcatIntanceProps.setProperty("shutdownPort", shutdownPort);
		
		model.put("tomcatInstance", tomcatIntanceProps);

		model.put("tomcatHostIPAddress", InetAddress.getLocalHost().getHostAddress());
		
		System.out.println("Creating tomcat nstance at "+outputDir.getAbsolutePath());
		System.out.println("New httpPort " + httpPort+", shutdownPort "+ shutdownPort);
	}
	
	@Override
	public void destroy() throws Exception {
		System.out.println("Updating new port number to meta file.");
		tomcatIntanceProps.store(new FileOutputStream(instancePropsFile), "Create-New-Tomcat Metadata");	
	}

	@Override
	protected String getTemplateSetName() {
		return "create-tomcat-instance";
	}

	@Override
	protected String getOutputFilename(String filePathname) {
		List<String> names = RegexUtils.findGroupsByDelimeters(filePathname, "R_", "_R", "R_");
		//System.out.println(names);
		for(String name : names){
			if(name.equals("tomcatInstanceName"))
				filePathname = filePathname.replaceAll("R_"+name+"_R", project.getName());
			else
				throw new RuntimeException("Unknown variable " + name+" in pathname.");
		}
		return filePathname;
	}
}
