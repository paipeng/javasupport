package deng.pojo.jms.examples;

import java.util.concurrent.CountDownLatch;

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
		
		CountDownLatch countLatch = new CountDownLatch(1);
		long delay = 0;
		int msgReceived = 0;
		
		public int getMsgReceived() {
			return msgReceived;
		}
		
		public void setDelay(long delay) {
			this.delay = delay;
		}
		
		public void resetLatch(int count) {
			countLatch = new CountDownLatch(count);
		}
		
		public void awaitLatch() {
			try {
				countLatch.await();
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}
		
		@Override
		public void onMessage(Message msg) {
			try {
				System.out.println("Received msg#" + msg.getStringProperty("msgId") + ", pri=" + msg.getJMSPriority());
				msgReceived ++;
				Thread.sleep(delay);
				countLatch.countDown();
			} catch (JMSException e) {
				throw new RuntimeException(e);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}
	}

	@Override
	protected void runExample(Connection connection) throws JMSException {
		final Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Destination aQueue = session.createQueue("AQueue");
		
		// Setup a message processor.
		MyListener myListener = new MyListener();
		MessageConsumer processer = session.createConsumer(aQueue);
		processer.setMessageListener(myListener);
		
		MessageProducer producer = session.createProducer(aQueue);
		
//		// Case 1
//		send(1, 0, producer, session);
//		send(1, 1, producer, session);
//		send(1, 2, producer, session);
//		myListener.resetLatch(3);
//		connection.start();
//		myListener.awaitLatch();

//		// Case 2
//		connection.stop();
//		send(2, 2, producer, session);
//		send(2, 0, producer, session);
//		send(2, 1, producer, session);
//		myListener.resetLatch(6);
//		connection.start();
//		myListener.awaitLatch();
		
		// Case 3
		connection.stop();
		send(1, 2, producer, session);
		send(1, 7, producer, session);
		send(1, 1, producer, session);
		myListener.setDelay(1000);
		myListener.resetLatch(6);
		connection.start();
		pause(1000);
		send(1, 8, producer, session);
		send(1, 5, producer, session);
		send(1, 9, producer, session);
		myListener.awaitLatch();
	}
	
	private void pause(long delay) {
		try {
			Thread.sleep(delay);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
	
	int sendBatchNum = 0;
	private void send(int numOfMsg, int priority, MessageProducer producer, Session session) throws JMSException {
		sendBatchNum ++;
		for (int i = 1; i <= numOfMsg; i++) {
			Message msg = session.createMessage();		
			String id = sendBatchNum + "_" + i;
			msg.setStringProperty("msgId", id);
			System.out.println("Sending msgId#" + id + ", pri=" + priority);
			producer.send(msg, DeliveryMode.NON_PERSISTENT, priority, 0);
		}
	}
}
