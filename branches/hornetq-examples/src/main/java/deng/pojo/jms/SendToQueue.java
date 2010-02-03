package deng.pojo.jms;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class SendToQueue {	
	public static void main(String[] args) throws Exception {
		SendToQueue main = new SendToQueue();
		main.queueName = System.getProperty("queueName", "ExampleQueue");
		main.text = System.getProperty("text", "Test Message.");
		main.run();
	}
	
	private String queueName;
	private String text;
	
	public void run() {
		Connection connection = null;
		Context ctx = null;
		try {
			ctx = new InitialContext();
			ConnectionFactory cf = (ConnectionFactory)ctx.lookup("/ConnectionFactory");
			connection = cf.createConnection();
			Queue queue = (Queue)ctx.lookup(queueName);
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			MessageProducer producer = session.createProducer(queue);
			TextMessage message = session.createTextMessage(text);
			System.out.println("Sending text: " + text);
			producer.send(message);		
			session.close();
			System.out.println("Message sent to queue: " + queueName);
		} catch (JMSException e) {
			throw new RuntimeException(e);
		} catch (NamingException e) {
			throw new RuntimeException(e);
		} finally {
			if (ctx != null) {
				try { ctx.close(); } catch (NamingException e) { throw new RuntimeException(e); }
			}
			if (connection != null) {
				try { connection.close(); } catch (JMSException e) { throw new RuntimeException(e); }
			}
		}
	}
}
