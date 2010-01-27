package deng.myrunbean;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 *
 * A main entry of program that create application context with Spring IoC container
 * an invoke a RunnableBean.
 *
 * @author zemian
 * 
 */
public class RunBean {
	public static void main(String[] args) {
		Logger logger = LoggerFactory.getLogger(RunBean.class);
		logger.debug("Command arguments " + Arrays.asList(args));

		String config = "conf/beans.xml";
		String mainBeanName = "main";

		if (args.length >= 1) {
			config = args[0];
			
			// check for help page.
			if(config.toLowerCase().equals("h") || config.toLowerCase().equals("help")){
				logger.info("Usage: java RunBean [configFile] [beanName]");
				logger.info("  Default configFile = conf/beans.xml, beanName=main");
				System.exit(0);
			}
		}
		if (args.length >= 2) {
			mainBeanName = args[1];
		}

		logger.debug("Loading " + config);
		FileSystemXmlApplicationContext applicationContext = new FileSystemXmlApplicationContext(config);
		applicationContext.registerShutdownHook();

		Runnable bean = (Runnable) applicationContext.getBean(mainBeanName);
		logger.info("Run bean [" + mainBeanName + "] " + bean);

		try {
			bean.run();
		}
		catch (RuntimeException e) {
			logger.error("Run error.", e);
		}

		//logger.info("Done.");
	}
}