package deng.pojo.jms;

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
import javax.naming.NamingException;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class QueueListenerBean implements MessageListener {
	public static void main(String[] args) throws Exception {
		QueueListenerBean main = new QueueListenerBean();
		main.queueName = System.getProperty("queueName", "ExampleQueue");
		main.run();
	}
	
	private String queueName;

	private Connection connection = null;
	private int count = 0;
	
	public void run() {
		// Run
		try {
			init();
			startListener();		
			synchronized(this) { this.wait(); }
		} catch (InterruptedException e) {
			destroy();
			throw new RuntimeException(e);
		} catch (NamingException e) {
			destroy();
			throw new RuntimeException(e);
		}  catch (JMSException e) {
			destroy();
			throw new RuntimeException(e);
		}
	}
	
	private void init() {		
		// Setup shutdown hook
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				System.out.println("Received CTRL+C");
				QueueListenerBean.this.destroy();
			}
		});
		
	}
	public void startListener() throws NamingException, JMSException {
		Context ctx = new InitialContext();
		try {
			ConnectionFactory cf = (ConnectionFactory)ctx.lookup("/ConnectionFactory");
			connection = cf.createConnection();
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Queue queue = session.createQueue(queueName);

			MessageConsumer messageConsumer = session.createConsumer(queue);
			messageConsumer.setMessageListener(this);
			
			System.out.println("Starting msg listener on queue: " + queueName);
			connection.start();
		} finally {
			if (ctx != null) {
				ctx.close();
			}
		}
	}
	
	public void destroy() {
		if (connection != null) {
			try {
				System.out.println("Stopping message listener.");
				connection.close();
			} catch (JMSException e) {
				throw new RuntimeException("Failed close JMS connection", e);
			}
		}
		
		System.out.println(count + " msgs removed from queue.");
	}

	public void onMessage(Message msg) {
		System.out.println();
		String msgStr = ToStringBuilder.reflectionToString(msg, ToStringStyle.MULTI_LINE_STYLE);
		System.out.println("msg#" + (count + 1) + " : " + msgStr);

		count ++;		
	}
}

