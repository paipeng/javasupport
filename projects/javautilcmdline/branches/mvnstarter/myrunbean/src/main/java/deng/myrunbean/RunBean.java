package deng.myrunbean;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 * 
 * A main entry of program that create and load application context beans.xml for Spring IoC 
 * container and invoke one or more Runnable or RunnableBean.
 *  
 * Each bean found is run in synchronized manner. If you need a-synchronized thread to run your
 * beans, use {@Link RunnableBeanExecutor} instead.
 *  
 * Usage: java RunBean configFile beanName [beanName]...
 * 
 * @author zemian
 * 
 */
public class RunBean {
	public static void main(String[] args) {
		Logger logger = LoggerFactory.getLogger(RunBean.class);
		logger.debug("Command arguments " + Arrays.asList(args));
		
		if (args.length < 2) {
			throw new IllegalArgumentException("Missing beans.xml config file.");
		}
		String config = args[0];
		logger.debug("Loading " + config);
		FileSystemXmlApplicationContext applicationContext = new FileSystemXmlApplicationContext(config);
		applicationContext.registerShutdownHook();
				
		for(int i =1; i<args.length;i++){
			String beanName = args[i];
					
			try {
				logger.debug("Looking up beanName " + beanName);
				Runnable bean = (Runnable) applicationContext.getBean(beanName);

				if(bean instanceof RunnableBean){
					logger.info("Initializing bean.");
					((RunnableBean)bean).init();
				}
				
				logger.info("Running bean.");
				bean.run();
				
				if(bean instanceof RunnableBean){
					logger.info("Destroying bean.");
					((RunnableBean)bean).destroy();
				}
			}
			catch (Exception e) {
				logger.error("Failed to run bean.", e);
			}
		}
	}
}