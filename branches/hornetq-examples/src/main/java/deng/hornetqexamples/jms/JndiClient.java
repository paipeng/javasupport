package deng.hornetqexamples.jms;

import java.util.Date;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;

public class JndiClient {
	
	public static void main(String[] args) throws Exception {
		JndiClient bean = new JndiClient();
		bean.start();
	}
	

	private Context ctx = null;
	private Connection connection = null;
	
	public void start() throws Exception {
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
	
	private Connection getConnection() throws Exception {
		// Make sure jndi.properties is in classpath!
		ctx = new InitialContext();
		ConnectionFactory cf = (ConnectionFactory)ctx.lookup("/ConnectionFactory");
		Connection connection = cf.createConnection();
		return connection;
	}

	private void produceToQueue(Connection connection) throws Exception {
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

	private void consumeFromQueue(Connection connection) throws Exception {
		Queue queue = (Queue)ctx.lookup("/queue/ExampleQueue");
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		MessageConsumer messageConsumer = session.createConsumer(queue);

		connection.start();

		TextMessage messageReceived = (TextMessage) messageConsumer.receive(5000);
		System.out.println("Received message: " + messageReceived.getText());
		session.close();
	}
}
