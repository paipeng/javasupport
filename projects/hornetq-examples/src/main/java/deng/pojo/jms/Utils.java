package deng.pojo.jms;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class Utils {

	
	@SuppressWarnings("unchecked")
	public static <T> T newInstanceFromClassName(String className) {
		try {
			return (T) Class.forName(className).newInstance();
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T lookupJndi(String name) {
		InitialContext context = null;
		try {
			context = new InitialContext();
			Object object = context.lookup(name);
			return (T)object;
		} catch (NamingException e) {
			throw new RuntimeException(e);
		} finally {
			if (context != null) {
				try {
					context.close();
				} catch (NamingException e) {
					throw new RuntimeException(e);
				}
			}
		}
	}

	public static void close(Connection connection) {
		try {
			if (connection != null) {
				connection.stop();   // Stop any consumers that has not stopped on it's own.
				connection.close();
			}
		} catch (JMSException e) {
			throw new RuntimeException(e);
		}		
	}
}
