package deng.javaexamples;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Properties;

import javax.naming.InitialContext;

public class MyApp {
	public static void main(String[] args) throws Exception {
		//MyBean b = getBean();		
		MyBean b = doLookupBean("demo.dengz1.mysimpleapp.MyBean");		
		System.out.println(b.getName());
	}
	
	private static <T> T doLookupBean(String className) throws Exception {
		return (T) Class.forName(className).newInstance();
	}

	public static void mainProps(String[] args) throws Exception {
		Properties props = getProps();

		System.out.println(props.size());
		StringWriter sw = new StringWriter();
		props.list(new PrintWriter(sw));
	}

	public static Properties getProps() {
		Properties props = System.getProperties();
		return props;
	}
}
