package javasupport.toolbox.generator;

public class Project {
	private String name;
	private String groupId;
	private String artifactId;
	private String javasupportVersion;
	private String packageName;
	private String templatePath;
	private String templateSetName;
		
	public Project(String name, String javasupportVersion, String templatePath, String templateSetName) {
		String packageName = name.toLowerCase();
		packageName = packageName.replaceAll("-", "");
		packageName = packageName.replaceAll("_", "");
		
		init(name, javasupportVersion, templatePath, templateSetName, packageName);
	}
	
	public Project(String name, String javasupportVersion, String templatePath, String templateSetName, String packageName) {
		init(name, javasupportVersion, templatePath, templateSetName, packageName);
	}
	
	public void init(String name, String javasupportVersion, String templatePath, String templateSetName, String packageName) {
		this.name = name;
		this.templateSetName = templateSetName;
		
		this.groupId = name;
		
		this.packageName = packageName;
		
		artifactId = groupId;
		this.javasupportVersion = javasupportVersion;
		
		this.templatePath = templatePath;
	}
		
	public String getTemplateSetName() {
		return templateSetName;
	}
	
	public String getTemplatePath() {
		return templatePath;
	}
	
	public String getName() {
		return name;
	}
	public String getGroupId() {
		return groupId;
	}
	public String getArtifactId() {
		return artifactId;
	}
	public void setArtifactId(String artifactId) {
		this.artifactId = artifactId;
	}
	public String getJavasupportVersion() {
		return javasupportVersion;
	}
	public String getPackageName() {
		return packageName;
	}
}
