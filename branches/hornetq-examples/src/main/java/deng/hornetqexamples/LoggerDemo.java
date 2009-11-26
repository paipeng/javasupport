package deng.hornetqexamples;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Demo of using a Logger.
 * 
 * @author Zemian Deng
 *
 * Created on Nov 12, 2009
 */
public class LoggerDemo {
	private Log logger = LogFactory.getLog(getClass());
	
	public static void main(String[] args) {
	  new LoggerDemo().logAllLevels();
	}
	
	public void logAllLevels() { 
		logger.trace("A message from logger.");
		logger.debug("A message from logger.");
		logger.info("A message from logger.");
		logger.warn("A message from logger.");
		logger.error("A message from logger.");
		logger.fatal("A message from logger.");
	}
}
