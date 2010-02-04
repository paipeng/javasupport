package deng.pojo.jms.examples;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;

public class SendBlockUntilAck extends ExampleRunner {
	public static void main(String[] args) {
		SendBlockUntilAck main = new SendBlockUntilAck();
		main.run();
	}

	@Override
	protected void runExample(Connection connection) throws JMSException {
		final Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Queue aQueue = session.createQueue("AQueue");
		Queue bQueue = session.createQueue("BQueue");
		
		// Setup a message processor.
		MessageConsumer processer = session.createConsumer(aQueue);
		processer.setMessageListener(new MessageListener() {			
			@Override
			public void onMessage(Message request) {
				try {
					System.out.println("Processing request in AQueue");
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
		System.out.println("Sending a request to AQueue");
		MessageProducer producer = session.createProducer(aQueue);
		Message msg = session.createMessage();
		msg.setJMSReplyTo(bQueue);
		producer.send(msg);
		System.out.println("Request sent to AQueue");
		MessageConsumer consumer = session.createConsumer(bQueue);
		System.out.println("Waiting on receipt from BQueue...");
		Message receipt = consumer.receive();
		System.out.println("Got a reply.");
		print(receipt);
		System.out.println("Done.");
	}
}
