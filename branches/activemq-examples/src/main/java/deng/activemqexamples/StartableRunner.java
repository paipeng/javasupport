package deng.activemqexamples;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class StartableRunner {
	private static Log logger = LogFactory.getLog(StartableRunner.class);
	
	public static void main(String[] args) throws Exception {
		new StartableRunner().runBean(args[0]);
	}
	
	public static void addShutdownHook(final Startable bean) {
		logger.debug("Setup shutdown hook for stoppable bean: " + bean);
		Runtime.getRuntime().addShutdownHook(new Thread(){
			public void run() {
				logger.debug("Stopping " + bean);
				bean.stop();
				logger.info("Bean " + bean + " stopped.");
			}			
		});		
	}

	public void runBean(String className) {
		try {
			logger.debug("Creating Starable bean: " + className);
			Class<?> cls = Class.forName(className);
			Startable bean = (Startable)cls.newInstance();			
			addShutdownHook(bean);
			logger.debug("Starting " + bean);
			bean.start();
			
			logger.info("Bean " + bean + " started.");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
