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
		this.name = name;
		this.templateSetName = templateSetName;
		
		groupId = name;
		
		packageName = name.toLowerCase();
		packageName = packageName.replaceAll("-", "");
		packageName = packageName.replaceAll("_", "");
		
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
