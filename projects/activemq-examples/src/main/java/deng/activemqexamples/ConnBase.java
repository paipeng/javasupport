package deng.activemqexamples;

import javax.jms.Connection;
import javax.jms.JMSException;

import lombok.Setter;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class ConnBase implements Startable {
	protected Log logger = LogFactory.getLog(getClass());
	
	@Setter	
	protected Connection connection;
	
	public Connection createDefaultConnection() throws JMSException {
		logger.debug("Creating default ActiveMQ connection obj");
		
	    String user = ActiveMQConnection.DEFAULT_USER;
	    String password = ActiveMQConnection.DEFAULT_PASSWORD;
	    String url = ActiveMQConnection.DEFAULT_BROKER_URL;
		ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(user, password, url);
		return connectionFactory.createConnection();
	}
	
	@Override
	public void start() {
		try {
			if (connection == null) {
				connection = createDefaultConnection();
			}
		} catch (JMSException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public void stop() {
		if (connection != null) {
			try {
				logger.debug("Closing JMS connection.");
				connection.close();
			} catch (JMSException e) {
				throw new RuntimeException(e);
			}
		}
	}
}
