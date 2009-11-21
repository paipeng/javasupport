package deng.hornetqexamples.jms;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;

public class QueueListenerBean implements MessageListener {
	
	public static void main(String[] args) throws Exception {
		new QueueListenerBean().start();
	}
	

	private Context ctx = null;
	private Connection connection = null;
	
	private Connection getConnection() throws Exception {
		// Make sure jndi.properties is in classpath!
		ctx = new InitialContext();
		ConnectionFactory cf = (ConnectionFactory)ctx.lookup("/ConnectionFactory");
		Connection connection = cf.createConnection();
		return connection;
	}

	private void initialize(Connection connection) throws Exception {
		Queue queue = (Queue)ctx.lookup("/queue/ExampleQueue");
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		MessageConsumer messageConsumer = session.createConsumer(queue);
		messageConsumer.setMessageListener(this);
	}

	public void onMessage(Message msg) {
		System.out.println("Received message: " + msg);
	}
	
	public void start() throws Exception {
		connection = getConnection();
		initialize(connection);
		connection.start();
	}
	
	public void stop() throws Exception {
		if (connection != null) {
			connection.close();
		}
	}
}
