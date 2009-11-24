package demo.dengz1.cameldemo;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Properties;

import org.apache.log4j.Logger;

public class MyApp {
	private static Logger logger = Logger.getLogger(MyApp.class);
	public static void main(String[] args) throws Exception {
		Properties props = getProps();

		logger.info("System properties size= " + props.size());
    
		StringWriter sw = new StringWriter();
		props.list(new PrintWriter(sw));
    
		logger.debug(sw.toString());
	}

	public static Properties getProps() {
		Properties props = System.getProperties();
		return props;
	}
}
