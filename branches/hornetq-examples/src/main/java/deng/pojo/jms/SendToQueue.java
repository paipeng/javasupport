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

import lombok.Data;

@Data
public class SendToQueue {	
	public static void main(String[] args) throws Exception {
		SendToQueue main = new SendToQueue();
		main.setQueueName(System.getProperty("queueName", "ExampleQueue"));
		main.setText(System.getProperty("text", "Test Message."));
		main.run();
	}
	
	private String queueName;
	private String text;
	
	public void run() {
		try {
			send();
		} catch (JMSException e) {
			throw new RuntimeException(e);
		} catch (NamingException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void send() throws JMSException, NamingException {
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
		} finally {
			if (ctx != null) {
				try { 
					ctx.close(); 
				} catch (NamingException e) { 
					/* Just log it so next clean up can proceed.*/
					System.out.println("WARN: Can not close JNDI context. " + e.getMessage());
				}
			}
			if (connection != null) {
				connection.close();
			}
		}
	}
}
