package deng.simplespringapp.container.services;

import java.util.Properties;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import deng.simplespringapp.container.Service;

/**
 * A simple service that dumps out the Java System Properties to logger
 * when run is called.
 * 
 * @author dengz1
 *
 */
public class SysPropsService implements Service {
	private Log logger = LogFactory.getLog(getClass());
	
	@Override
	public void destroy() {
		logger.info("Service is destroyed.");
	}

	@Override
	public void init() {
		logger.info("Service is ready.");
	}
		
	@Override
	public void run() {		
		StringBuilder sb = new StringBuilder();
		Properties props = System.getProperties();
		Set<String> names = props.stringPropertyNames();
		for (String name : names) {
			sb.append(name + ": " + props.getProperty(name) + "\n");
		}
		logger.info("=== System Properties ===\n" + sb.toString() + "\n=== System Properties ===");
	}
}
