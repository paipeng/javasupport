package deng.simplespringapp.containerservices;

import lombok.Setter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class ContainerRunner {
	public static Log logger = LogFactory.getLog(ContainerRunner.class);
	
	public static final String DEFAULT_CONFIG = "conf/service-container.xml";
		
	public static void main(String[] args) {		
		String config = DEFAULT_CONFIG;
		if (args.length >= 1) {
			config = args[0];
		}

		logger.info("Starting ContainerRunner: " + config);
		final AbstractXmlApplicationContext applicationContext = new FileSystemXmlApplicationContext();		
		applicationContext.registerShutdownHook();
		Runtime.getRuntime().addShutdownHook(new Thread(){
			@Override
			public void run() {
				logger.info("Stopping ContainerRunner.");
				if (applicationContext.isActive()) {
					applicationContext.destroy();
				}
				logger.info("ContainerRunner is done.");
			}
		});
		applicationContext.setConfigLocation(config);
		applicationContext.refresh();
	}

}
