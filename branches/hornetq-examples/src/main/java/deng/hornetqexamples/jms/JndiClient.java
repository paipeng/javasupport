package deng.hornetqexamples.jms;

import static org.hornetq.integration.transports.netty.TransportConstants.PORT_PROP_NAME;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.hornetq.core.config.TransportConfiguration;
import org.hornetq.integration.transports.netty.NettyConnectorFactory;
import org.hornetq.jms.client.HornetQConnectionFactory;

public class JndiClient {
	
	public static void main(String[] args) throws Exception {
		new JndiClient().run();
	}
	

	private Context ctx = null;
	
	private void run() throws Exception {
		Connection connection = null;
		try {
			connection = getConnection();
			produceToQueue(connection);
			consumeFromQueue(connection);
		} finally {
			if (connection != null) {
				connection.close();
			}
		}
	}
	
	public Connection getConnection() throws Exception {
		// Make sure jndi.properties is in classpath!
		ctx = new InitialContext();
		ConnectionFactory cf = (ConnectionFactory)ctx.lookup("/ConnectionFactory");
		// System.out.println(connFac);
		Connection connection = cf.createConnection();
		return connection;
	}

	public void produceToQueue(Connection connection) throws Exception {
		Queue queue = (Queue)ctx.lookup("/queue/ExampleQueue");
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		MessageProducer producer = session.createProducer(queue);

		TextMessage message = session
				.createTextMessage("This is a text message. Date: "
						+ new Date());

		producer.send(message);		
		System.out.println("Sent message: " + message.getText());
		session.close();
	}

	public void consumeFromQueue(Connection connection) throws Exception {
		Queue queue = (Queue)ctx.lookup("/queue/ExampleQueue");
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		MessageConsumer messageConsumer = session.createConsumer(queue);

		connection.start();

		TextMessage messageReceived = (TextMessage) messageConsumer.receive(5000);
		System.out.println("Received message: " + messageReceived.getText());
		session.close();
	}
}
