package deng.hornetqexamples;

import org.apache.log4j.Logger;

/**
 * Demo of using a Logger.
 * 
 * @author Zemian Deng
 *
 * Created on Nov 12, 2009
 */
public class LoggerDemo {
	private static Logger logger = Logger.getLogger(LoggerDemo.class);
	
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
