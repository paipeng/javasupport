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
 * This example is based on blog entry here:
 * http://hornetq.blogspot.com/2009/09/hornetq-simple-example-using-maven.html
 * 
 * This example client does not need to connect to JNDI server! It connects directly to HornetQ
 * server using Netty transport.
 * 
 * We can use a jconsole to verify message sent in the MBean view!
 * 
 * @author zemian
 * 
 */
public class NettyJmsClient {

	public static void main(String[] args) throws Exception {
		NettyJmsClient bean = new NettyJmsClient();
		bean.start();
	}

	private Connection connection = null;
	public void start() throws Exception {
		try {
			connection = createDefaultConnection();
			produceToQueue(connection);
			consumeFromQueue(connection);
		} finally {
			if (connection != null) {
				connection.close();
			}
		}
	}

	protected Connection createDefaultConnection() throws Exception {
		Map<String, Object> connectionParams = new HashMap<String, Object>();
		connectionParams.put(PORT_PROP_NAME, 5445);
		TransportConfiguration transportConfiguration = new TransportConfiguration(
				NettyConnectorFactory.class.getName(), connectionParams);
		ConnectionFactory cf = new HornetQConnectionFactory(transportConfiguration);
		Connection connection = cf.createConnection();
		return connection;
	}

	protected void produceToQueue(Connection connection) throws Exception {
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

	protected void consumeFromQueue(Connection connection) throws Exception {
		Queue queue = new HornetQQueue("ExampleQueue");
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		MessageConsumer messageConsumer = session.createConsumer(queue);

		connection.start();

		TextMessage messageReceived = (TextMessage) messageConsumer.receive(5000);
		System.out.println("Received message: " + messageReceived.getText());
		session.close();
	}
}
