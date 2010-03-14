package deng.activemqexamples;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import lombok.Setter;

public class SendToQueue extends ConnBase {
	public static void main(String[] args) throws Exception {
		String queueName = args[0];
		String text = args[1];
		SendToQueue bean = new SendToQueue();
		bean.setQueueName(queueName);
		bean.send(text);
	}

	@Setter
	private String queueName = "queue/ExampleQueue"; 
	public void send(String text) {
		try {
			start(); // Initialize JMS conn
			
			logger.debug("Creating a producer");
			Session sess = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Queue queue = sess.createQueue(queueName);
			MessageProducer producer = sess.createProducer(queue);
			Message msg = createMessage(sess, text);
			logger.debug("Sending text: " + text);
			producer.send(msg);			
			logger.info("A msg has been sent to " + queue);
			
			stop(); // Cleanup JMS Conn
		} catch (JMSException e) {
			throw new RuntimeException(e);
		}
	
	}

	private Message createMessage(Session sess, String text) throws JMSException {
		TextMessage msg = sess.createTextMessage();
		msg.setText(text);
		return msg;
	}
}
