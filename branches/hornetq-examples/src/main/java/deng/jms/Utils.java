package deng.jms;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class Utils {
	
	@SuppressWarnings("unchecked")
	public static <T> T lookupJndi(String name) {
		try {
			InitialContext context = new InitialContext();
			Object object = context.lookup(name);
			return (T)object;
		} catch (NamingException e) {
			throw new RuntimeException(e);
		}
	}

	public static void close(Connection connection) {
		try {
			connection.stop();   // Stop any consumers that has not stopped on it's own.
			connection.close();
		} catch (JMSException e) {
			throw new RuntimeException(e);
		}		
	}
}
