package deng.hornetqexamples.jms;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;

public class SendToQueue {
	
	public static void main(String[] args) throws Exception {
		SendToQueue bean = new SendToQueue();
		String queueName = args[0];
		String text = args[1];
		bean.send(queueName, text);
	}
	
	public void send(String queueName, String text) throws Exception {
		Connection connection = null;
		Context ctx = new InitialContext();
		try {
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
			if (connection != null) {
				connection.close();
			}
			if (ctx != null) {
				ctx.close();
			}
		}
	}
}
