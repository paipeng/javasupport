package deng.pojo.jms.examples;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;

public class SendBlockUntilAck extends ExampleRunner {
	public static void main(String[] args) {
		SendBlockUntilAck main = new SendBlockUntilAck();
		main.run();
	}

	@Override
	protected void runExample(Connection connection) throws JMSException {
		final Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Destination queueA = session.createQueue("queueA");
		Destination queueB = session.createQueue("queueB");
		
		// Setup a message processor.
		MessageConsumer processer = session.createConsumer(queueA);
		processer.setMessageListener(new MessageListener() {			
			@Override
			public void onMessage(Message request) {
				try {
					System.out.println("Processing request in queueA");
					Destination replyQ = request.getJMSReplyTo();
					MessageProducer producer = session.createProducer(replyQ);
					producer.send(request);
				} catch (JMSException e) {
					throw new RuntimeException(e);
				}
			}
		});
		connection.start();
		
		// Start testing a async send, then a sync receive to block until receipt is received.
		System.out.println("Sending a request to queueA");
		MessageProducer producer = session.createProducer(queueA);
		Message msg = session.createMessage();
		msg.setJMSReplyTo(queueB);
		producer.send(msg);
		System.out.println("Request sent to queueA");
		MessageConsumer consumer = session.createConsumer(queueB);
		System.out.println("Waiting on receipt from queueB...");
		Message receipt = consumer.receive();
		System.out.println("Got a reply.");
		print(receipt);
		System.out.println("Done.");
	}
}
