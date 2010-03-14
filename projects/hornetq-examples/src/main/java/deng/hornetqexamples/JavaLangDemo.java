package deng.hornetqexamples;

import java.util.logging.Logger;

/**
 * Demo of Java Lang and JDK lib usage.
 * 
 * @author Zemian Deng
 *
 * Created on Nov 12, 2009
 */
public class JavaLangDemo {
	
	public static void main(String[] args) {
		JavaLangDemo bean = new JavaLangDemo();
		bean.testFinalSetter();
	}
	
	Logger logger = Logger.getLogger(getClass().getName());
	String name = "TEST";
	
	public void testFinalSetter() {
		setName("FOO");
		System.out.println(name);
	}
	
	public void setName(final String name) {
		this.name = name;
	}
}
