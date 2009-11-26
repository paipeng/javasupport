package deng.hornetqexamples;

import java.util.Arrays;
import java.util.Enumeration;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * Demo of Java Lang and JDK lib usage.
 * 
 * @author Zemian Deng
 *
 * Created on Nov 12, 2009
 */
public class JULDemo {
	
	public static void main(String[] args) {
		JULDemo bean = new JULDemo();
		bean.resetLoggerToFinestLevel();
		bean.logAllLevels();
	}
	
	Logger logger = Logger.getLogger(getClass().getName());
	
	public void showExistingLoggerInfo() {
		// Checking existing handlers
		LogManager lm = LogManager.getLogManager();
		Enumeration<String> names = lm.getLoggerNames();
		while (names.hasMoreElements())
			System.out.println("Logger instance name: " + names.nextElement());


		Handler[] handlers = null;
		handlers = logger.getHandlers();
		System.out.println("This class logger handlers: " + Arrays.asList(handlers));
		handlers = Logger.getLogger("global").getHandlers();
		System.out.println("Global logger handlers: " + Arrays.asList(handlers));
		handlers = Logger.getLogger("").getHandlers();
		System.out.println("Root logger handlers: " + Arrays.asList(handlers));
		
		Logger rootLogger = Logger.getLogger("");
		System.out.println(rootLogger.getLevel());

		Handler rootHandler = rootLogger.getHandlers()[0];
		System.out.println(rootHandler.getLevel());
	}
	
	public void resetLoggerToFinestLevel() {
		// Reset root logger levels
		Logger rootLogger = Logger.getLogger("");
		rootLogger.setLevel(Level.ALL);
		
		Handler rootHandler = rootLogger.getHandlers()[0];
		rootHandler.setLevel(Level.ALL);
		
		//Chanage output formatter
		Formatter newFormatter = new SingleLineFormatter();
		rootHandler.setFormatter(newFormatter);
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
	
	public static class SingleLineFormatter extends Formatter {
		@Override
		public String format(LogRecord record) {
			String out = String.format("%7s | %s\n", record.getLevel().getName(), record.getMessage());
			return out;
		}
	}
}
