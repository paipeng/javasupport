#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package};

import java.util.logging.*;

/**
 * Demo of using a Logger.
 * 
 * @author Zemian Deng
 *
 * Created on Nov 12, 2009
 */
public class LoggerDemo {
	private Logger logger = Logger.getLogger(getClass().getName());
	
	public static void main(String[] args) {
	  new LoggerDemo().logAllLevels();
	}
	
	public void logAllLevels() { 
		logger.severe("A message from logger.");
		logger.warning("A message from logger.");
		logger.info("A message from logger.");
		logger.config("A message from logger.");
		logger.fine("A message from logger.");
		logger.finer("A message from logger.");
		logger.finest("A message from logger.");
	}
}
