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

import org.hornetq.core.config.TransportConfiguration;
import org.hornetq.integration.transports.netty.NettyConnectorFactory;
import org.hornetq.jms.HornetQQueue;
import org.hornetq.jms.client.HornetQConnectionFactory;

/**
 * 
 * This example do not use JNDI server! and it connect directly to HornetQ
 * server using Netty transport.
 * 
 * We can use a jconsole to verify message sent in the MBean view!
 * 
 * @author zemian
 * 
 */
public class NettyClient {

	public static void main(String[] args) throws Exception {
		new NettyClient().run();
	}
	
	private void run() throws Exception {
		Connection connection = null;
		try {
			connection = createConnection();
			produceToQueue(connection);
			consumeFromQueue(connection);
		} finally {
			if (connection != null) {
				connection.close();
			}
		}
	}

	public Connection getConnection() throws Exception {
		Map<String, Object> connectionParams = new HashMap<String, Object>();
		connectionParams.put(PORT_PROP_NAME, 5445);
		TransportConfiguration transportConfiguration = new TransportConfiguration(
				NettyConnectorFactory.class.getName(), connectionParams);
		ConnectionFactory cf = new HornetQConnectionFactory(transportConfiguration);
		Connection connection = cf.createConnection();
		return connection;
	}

	public void produceToQueue(Connection connection) throws Exception {
		Queue queue = new HornetQQueue("ExampleQueue");
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
		Queue queue = new HornetQQueue("ExampleQueue");
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		MessageConsumer messageConsumer = session.createConsumer(queue);

		connection.start();

		TextMessage messageReceived = (TextMessage) messageConsumer.receive(5000);
		System.out.println("Received message: " + messageReceived.getText());
		session.close();
	}
}
