package deng.pojo.jms.examples;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;

public class ReceiveByPriority extends ExampleRunner {
	public static void main(String[] args) {
		ReceiveByPriority main = new ReceiveByPriority();
		main.run();
	}
	
	private class MyListener implements MessageListener {
		int msgReceived = 0;
		
		@Override
		public void onMessage(Message msg) {
			try {
				System.out.println("Received msg#" + msg.getIntProperty("msgId") + ", pri=" + msg.getJMSPriority());
				msgReceived ++;
			} catch (JMSException e) {
				throw new RuntimeException(e);
			}
		}
	}

	@Override
	protected void runExample(Connection connection) throws JMSException {
		final Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Destination queueA = session.createQueue("queueA");
		
		// Setup a message processor.
		MyListener myListener = new MyListener();
		MessageConsumer processer = session.createConsumer(queueA);
		processer.setMessageListener(myListener);
		connection.start();
		
		// Start sending different set of prioritized messages.
		MessageProducer producer = session.createProducer(queueA);
		int numOfMsg = 100;
		for (int i = 0; i < numOfMsg; i++) {
			int pri = i % 10;
			Message msg = session.createMessage();		
			msg.setIntProperty("msgId", i);
			System.out.println("Sending msgId#" + i + ", pri=" + pri);
			producer.send(msg, DeliveryMode.NON_PERSISTENT, pri, 0);
		}
		
		// Wait until all messages are received.
		while (myListener.msgReceived < numOfMsg) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}
	}
}
