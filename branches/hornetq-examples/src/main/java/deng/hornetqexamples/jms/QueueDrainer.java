package deng.hornetqexamples.jms;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;

public class QueueDrainer {
	
	public static void main(String[] args) throws Exception {
		QueueDrainer bean = new QueueDrainer();
		bean.start();
	}
	

	private Context ctx = null;
	private Connection connection = null;
	
	public void start() throws Exception {
		try {
			connection = getConnection();
			drainQueue(connection);
		} finally {
			if (connection != null) {
				connection.close();
			}
		}
	}
	
	
	protected Connection getConnection() throws Exception {
		// Make sure jndi.properties is in classpath!
		ctx = new InitialContext();
		ConnectionFactory cf = (ConnectionFactory)ctx.lookup("/ConnectionFactory");
		Connection connection = cf.createConnection();
		return connection;
	}

	protected void drainQueue(Connection connection) throws Exception {
		Queue queue = (Queue)ctx.lookup("/queue/ExampleQueue");
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		MessageConsumer messageConsumer = session.createConsumer(queue);
		
		Message msg = null;
		while ((msg = messageConsumer.receive(5000)) != null) {
			System.out.println("Received message: " + msg);
		}
		System.out.println("Done.");
	}
}
