package demo.dengz1.myejbapp.appclient;

import java.util.Properties;
import java.util.UUID;

import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/** Send a text msg into a queue. 
 * Usage: SendTextToQueue [queueName] [textMsg]
 */
public class SendTextToQueue {
	public static void main(String[] args) {

		String queueName = "queue/my-ejb-app_Q1";
		if (args.length >= 1) {
			queueName = args[0];
		}
		String text = "Test msg from appclient SendTextToQueue.";
		if (args.length >= 2) {
			text = args[1];
		}
				
		Context ctx = null;
		QueueConnection queueConnection = null;

		try {
			Properties props = new Properties();
			props.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.jnp.interfaces.NamingContextFactory");
			props.setProperty(Context.PROVIDER_URL, "jnp://localhost:1099");
			
			// Let user override the parameters if exists.
			props.putAll(System.getProperties());
			
			ctx = new InitialContext(props);
			System.out.println("Looking up ConnectionFactory");
			QueueConnectionFactory queueConnectionFactory = (QueueConnectionFactory) ctx
					.lookup("ConnectionFactory");

			System.out.println("Looking up Queue: " + queueName);
			Queue queue = (Queue) ctx.lookup(queueName);
			queueConnection = queueConnectionFactory.createQueueConnection();
			QueueSession queueSession = queueConnection.createQueueSession(
					false, Session.AUTO_ACKNOWLEDGE);
			QueueSender queueSender = queueSession.createSender(queue);

			TextMessage message = queueSession.createTextMessage();
			message.setText(text);
			UUID correlationId = UUID.randomUUID();
			message.setJMSCorrelationID(correlationId.toString());
			System.out.println("Sending a jms text msg with correlationId=" + correlationId + ", text=" + text);
			
			queueSender.send(message);
			System.out.println("message sent");
			
			queueSession.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			if (queueConnection != null) {
				try { 
					queueConnection.close(); 
				} catch (JMSException e){ 
					throw new RuntimeException(e); 
				}
			}
			
			if (ctx != null) {
				try {
					ctx.close();
				} catch (NamingException e) {
					throw new RuntimeException(e);
				}
			}
		}
	}
}
