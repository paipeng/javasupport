public class JavaBean {
	public String getFoo(){
		return "java-foo";	
	}
	
	public String toString(){
		return super.toString() + ": " + getFoo();	
	}
}
