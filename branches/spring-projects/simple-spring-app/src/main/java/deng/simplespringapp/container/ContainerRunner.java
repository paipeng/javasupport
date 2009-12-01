package deng.simplespringapp.container;

import java.util.Map;

import lombok.Setter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 * Bootstrap a spring application context and starts all Container beans.
 * 
 * @author dengz1
 *
 */
public class ContainerRunner extends FileSystemXmlApplicationContext {
	public Log logger = LogFactory.getLog(ContainerRunner.class);
	
	public static final String DEFAULT_CONFIG = "conf/service-container.xml";
	
	@Setter
	private String config;
	
	private Map<String, Container> containers;
		
	public void runMain() {					
		registerShutdownHook();
		setConfigLocation(config);
		
		logger.info("Starting ContainerRunner: " + config);
		refresh();
		
		// store all container instances that registered in Spring context
		containers = getBeansOfType(Container.class);
		logger.info(containers.size() + " container beans found.");
		if (containers.size() == 0) {
			throw new RuntimeException("Missing Container beans in " + config);
		}
		
		// Let's start all containers if user didn't configure them so 
		// in spring with init/destroy-method attrs.
		for (Map.Entry<String, Container> pair : containers.entrySet()) {
			String beanName = pair.getKey();
			Container container = pair.getValue();
			logger.info("Starting container bean: " + beanName);
			container.start();
		}
	}
	
	@Override
	protected void doClose() {	
		// Let's stop all containers if user didn't configure them so 
		// in spring with init/destroy-method attrs.
		logger.info("Closing down " + containers.size() + " container beans.");
		for (Map.Entry<String, Container> pair : containers.entrySet()) {
			String beanName = pair.getKey();
			Container container = pair.getValue();
			logger.info("Stopping container bean: " + beanName);
			container.stop();
		}
		
		super.doClose();	
	}
	

	public static void main(String[] args) {
		String config = DEFAULT_CONFIG;
		if (args.length >= 1) {
			config = args[0];
		}
		
		ContainerRunner runner = new ContainerRunner();
		runner.setConfig(config);
		runner.runMain();
	}
}
