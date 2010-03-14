package deng.myjava;

public abstract class AbstractService implements Service {
	private String name;
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	
	@Override
	public void init() {
		System.out.println("Initializing " + name);
	}
	@Override
	public void destroy() {
		System.out.println("Destroying " + name);
	}
}
