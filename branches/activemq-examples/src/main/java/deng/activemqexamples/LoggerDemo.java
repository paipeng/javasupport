package deng.activemqexamples;

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
	private static Log log = LogFactory.getLog(LoggerDemo.class);
	
	public static void main(String[] args) {
		log.trace("I am in trace mode.");
		log.debug("I am in debug mode.");
		log.info("I am in info mode.");
		log.warn("I am in warn mode.");
		log.error("I am in error mode.");
		log.fatal("I am in fatal mode.");
	}

}
