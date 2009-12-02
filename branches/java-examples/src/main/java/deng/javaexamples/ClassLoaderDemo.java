package deng.javaexamples;

public class ClassLoaderDemo {

	public static void main(String[] args) {
		ClassLoaderDemo demo = new ClassLoaderDemo();
		demo.arrayCL();
	}

	/**
	 * All JDK classes are loaded by "bootstrap class loader"
	 * While custom classes are loaded by 
	 * sun.misc.Launcher$ExtClassLoader
	 *   |
	 *   +-> sun.misc.Launcher$AppClassLoader
	 */
	public void objCL(){
		/*
		 * the system class loader is the default delegation parent for new ClassLoader instances, 
		 * and is typically the class loader used to start the application. 
		 */
		System.out.println("* ClassLoader.getSystemClassLoader()");
		System.out.println(ClassLoader.getSystemClassLoader());
		System.out.println(ClassLoader.getSystemClassLoader().getParent());
		System.out.println(ClassLoader.getSystemClassLoader().getParent().getParent());
		
		System.out.println("* Object instnace");
		Object o = new Object();
		System.out.println(o.getClass());
		System.out.println(o.getClass().getClassLoader());
		
		System.out.println("* Date instnace");
		java.util.Date d = new java.util.Date();
		System.out.println(d.getClass());
		System.out.println(d.getClass().getClassLoader());
		
		System.out.println("* this class instance");
		System.out.println(this.getClass());
		System.out.println(this.getClass().getClassLoader());
		System.out.println(this.getClass().getClassLoader().getParent());
	}
	
	public void arrayCL(){
		System.out.println("* primative int array instnace");
		int[] iary = new int[]{1,2,3};
		System.out.println(iary.getClass());
		System.out.println(iary.getClass().getClassLoader());

		System.out.println("* Object array instnace");
		Object[] oary = new Object[]{new Object(), new Object(), new Object()};
		System.out.println(oary.getClass());
		System.out.println(oary.getClass().getClassLoader());

		System.out.println("* Date array instnace");
		java.util.Date[] dary = new java.util.Date[]{new java.util.Date(), new java.util.Date(), new java.util.Date()};
		System.out.println(dary.getClass());
		System.out.println(dary.getClass().getClassLoader());
	}
}
