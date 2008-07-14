package javasupport.toolbox.generator;

public class Project {
	private String name;
	private String groupId;
	private String artifactId;
	private String javasupportVersion;
	private String packageName;
	private String templatePath;
		
	public Project(String name, String javasupportVersion, String templatePath) {
		this.name = name;
		
		groupId = name.toLowerCase();
		groupId.replaceAll("-", "");
		groupId.replaceAll("_", "");
		
		artifactId = groupId;
		this.javasupportVersion = javasupportVersion;
		
		packageName = groupId;		
		this.templatePath = templatePath;
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
