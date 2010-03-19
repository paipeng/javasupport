package deng.javaexamples;

public class MyBean {
	private String name;
	
	public MyBean(){
		this("MyBean" + System.currentTimeMillis());
	}
	public MyBean(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
//	@Override
//	public String toString() {
//		return name;
//	}
}
