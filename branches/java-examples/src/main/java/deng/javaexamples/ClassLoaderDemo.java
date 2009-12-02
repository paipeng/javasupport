package deng.javaexamples;

import java.net.URL;
import java.net.URLClassLoader;


/**
 * 

* For more examples, checkout Groovy CL
	println(this.class.classLoader.parent.parent.parent)
	println(this.class.classLoader.rootLoader)
	println(Thread.currentThread().getContextClassLoader())
	
	OUTPUT:
	sun.misc.Launcher$AppClassLoader@11b86e7
	org.codehaus.groovy.tools.RootLoader@42e816
	org.codehaus.groovy.tools.RootLoader@42e816


* JDK provided class loader.
java.lang.Object
  extended by java.lang.ClassLoader
      extended by java.security.SecureClassLoader
          extended by java.net.URLClassLoader
              extended by javax.management.loading.MLet
                  extended by javax.management.loading.PrivateMLet

 
 * @author dengz1
 *
 */
public class ClassLoaderDemo {

	public static void main(String[] args) throws Exception {
		ClassLoaderDemo demo = new ClassLoaderDemo();
		//demo.objCL();
		//demo.threadContextCL();
		demo.loadExternalClass();
	}

	private void loadExternalClass() throws Exception {
//		//Test 1
//		try {
//			Class<?> cls = Class.forName("deng.cltest.CLTest", true, null);
//			System.out.println(cls);
//		} catch (ClassNotFoundException e) {
//			System.out.println("Expected.");
//		}
		
//		//Test 2
//		String jarfile = "file:///D:/tmp/cl.jar";
//		ClassLoader cl = new URLClassLoader(new URL[]{new URL(jarfile) });
//		Class<?> cls = Class.forName("deng.cltest.CLTest", true, cl);
//		System.out.println(cls); // SUCCESS!
		
		//Test 3
		// This will not find class! bc it will use this.getClass().getClassLoader() instead!
		// So null to #forName classloader will not automatically use Thread.getContextClassLoader()!
		// usage for that has to be explicit.
		// So this is a built-in "ThreadLocal" space for this particular object!
		String jarfile = "file:///D:/tmp/cl.jar";
		ClassLoader cl = new URLClassLoader(new URL[]{new URL(jarfile) });
		Thread thread = Thread.currentThread();
		ClassLoader currentCL = thread.getContextClassLoader();
		thread.setContextClassLoader(cl);
		Class<?> cls = Class.forName("deng.cltest.CLTest", true, null);
		System.out.println(cls);
		thread.setContextClassLoader(currentCL);
	}

	public void threadContextCL() {
		Thread thread = Thread.currentThread();
		ClassLoader cl = thread.getContextClassLoader();		
		showCL("* Thread Context ClassLoader", cl);
		
		cl = ClassLoader.getSystemClassLoader();
		showCL("* ClassLoader.getSystemClassLoader()", cl);
	}
	
	private void showCL(String subj, ClassLoader cl) {
		System.out.println(subj);
		System.out.println(cl);
		while ((cl = cl.getParent()) != null) {
			System.out.println("parent: " + cl);
		}
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
