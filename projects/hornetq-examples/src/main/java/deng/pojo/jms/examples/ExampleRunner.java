package deng.pojo.jms.examples;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public abstract class ExampleRunner {
	public void run() {
		try {
			init();
			runExample();
		} catch (JMSException e) {
			throw new RuntimeException(e);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
		
	ConnectionFactory connectionFactory;
	
	public void init() throws Exception {
		
		// Initialize the connectionFactory field if not already done so.
		if (connectionFactory == null) {
			String className = System.getProperty("connectionFactory");
			if (className != null) {
				connectionFactory = (ConnectionFactory)Class.forName(className).newInstance();
			} else {
				Context ctx = null;
				try { 
					ctx = new InitialContext();
					connectionFactory = (ConnectionFactory)ctx.lookup("/ConnectionFactory");
				} finally {
					if (ctx != null) {
						ctx.close();
					}
				}			
			}
		}
	}
	
	protected void runExample() throws JMSException {
		Connection connection = null;
		try {
			connection = connectionFactory.createConnection();
			addShutdownHook(connection);
			runExample(connection);
		} finally {
			if (connection != null) {
				connection.close();
			}
		}		
	}

	protected void addShutdownHook(final Connection connection) {
		// Add shutdown hook to close connection in case CTL+C hit.
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				try {
					connection.close();
				} catch (JMSException e) {
					throw new RuntimeException(e);
				}
			}
		});
	}

	abstract protected void runExample(Connection connection) throws JMSException;
	
	protected void print(Message msg) {
		String msgStr = ToStringBuilder.reflectionToString(msg, ToStringStyle.MULTI_LINE_STYLE);
		System.out.println(msgStr);
	}
}
