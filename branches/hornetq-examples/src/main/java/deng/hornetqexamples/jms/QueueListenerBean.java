package deng.hornetqexamples.jms;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class QueueListenerBean implements MessageListener {
	
	/**
	 * Maven can start this up with:
	 * mvn exec:java -Dexec.mainClass=deng.hornetqexamples.jms.QueueListenerBean
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		// Make sure jndi.properties is in classpath!
		Context ctx = new InitialContext();
		ConnectionFactory cf = (ConnectionFactory)ctx.lookup("/ConnectionFactory");
		Queue queue = (Queue)ctx.lookup("/queue/ExampleQueue");
		
		// Setup bean.
		final QueueListenerBean bean = new QueueListenerBean();
		bean.setConnFactory(cf);
		bean.setQueue(queue);

		System.out.println("Starting bean.");
		bean.start();
		
		// Shut it down when CTRL+C is hit.
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				System.out.println("Stopping bean.");
				bean.stop();
			}
		});
		
		while (true) {
			Thread.sleep(5000);
		}
	}
	
	private Log log = LogFactory.getLog(this.getClass());
	private ConnectionFactory connFactory;
	private Queue queue;
	
	private Connection connection = null;
	private MessageConsumer messageConsumer = null;
	
	public void setConnFactory(ConnectionFactory connFactory) {
		this.connFactory = connFactory;
	}
	public void setQueue(Queue queue) {
		this.queue = queue;
	}

	public void onMessage(Message msg) {
		System.out.println("Received message: " + msg);
	}
	
	public void start() {
		try {
			connection = connFactory.createConnection();
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			messageConsumer = session.createConsumer(queue);
			messageConsumer.setMessageListener(this);
			connection.start();
			
			log.info("JMS message listener is ready.");
		} catch (Exception e) {
			log.error("Failed start JMS connection", e);
			closeConn();
		}
	}
	
	public void stop() {
		closeConn();
	}
	
	protected void closeConn() {
		if (connection != null) {
			try {
				connection.close();
			} catch (JMSException e) {
				throw new RuntimeException("Failed close JMS connection", e);
			}
		}
	}
}
