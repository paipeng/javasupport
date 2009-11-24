package demo.dengz1.myjbpm;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Properties;

public class MyApp {
	public static void main(String[] args) throws Exception {
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
