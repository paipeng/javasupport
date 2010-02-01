package deng.jms;

import java.util.concurrent.CountDownLatch;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

public class MeasureRate {
	public static void main(String[] args) {
		MeasureRate measureRate = new MeasureRate();
		measureRate.queueName = "ExampleQueue";
		measureRate.numberOfSamples = 3;
		measureRate.connectionFactory = Utils.lookupJndi("ConnectionFactory");
		measureRate.run();
	}

	private String queueName;
	private int numberOfSamples;
	private ConnectionFactory connectionFactory;
	
	private void run() {
		startConsumer();
		startProducer();
	}

	private void startProducer() {
		Connection connection = null;
		try {
			connection = connectionFactory.createConnection();
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Queue dest = session.createQueue(queueName);
			MessageProducer prodcuer = session.createProducer(dest);
			
			for (int i = 0; i < numberOfSamples; i++) {
				prodcuer.send(createSampleMessage(session, i));
			}
		} catch (JMSException e) {
			throw new RuntimeException(e);
		} finally {
			Utils.close(connection);
		}		
	}

	private Message createSampleMessage(Session session, int index) throws JMSException {
		TextMessage txtMsg = session.createTextMessage("Test msg # " + index);
		if (index + 1 == numberOfSamples) {
			txtMsg.setBooleanProperty("lastRateMsg", true);
		}
		return txtMsg;
	}

	private void startConsumer() {
		Connection connection = null;
		RateMessageListener listener = null; 
		try {
			connection = connectionFactory.createConnection();
			listener = new RateMessageListener(connection);
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Queue dest = session.createQueue(queueName);
			MessageConsumer consumer = session.createConsumer(dest);
			consumer.setMessageListener(listener);
			connection.start();
			
			// Wait until it's done.
			listener.getLastMsgLatch().await();
		} catch (JMSException e) {
			throw new RuntimeException(e);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		} finally {
			Utils.close(connection);
		}		
	}	
	
	private class RateMessageListener implements MessageListener {
		private Connection connection = null;
		private CountDownLatch lastMsgLatch = new CountDownLatch(1);
		public RateMessageListener(Connection connection) {
			this.connection = connection;
		}
		public CountDownLatch getLastMsgLatch() {
			return lastMsgLatch;
		}
		public void onMessage(Message msg) {
			try {
				// collect msg rate data.	
				TextMessage txtMsg = (TextMessage)msg;
				String txt = txtMsg.getText();
				System.out.println(txt);
					
				// check for lastRateMsg.
				boolean lastRateMsg = msg.getBooleanProperty("lastRateMsg");
				if (lastRateMsg) {
					System.out.println("Last rate msg received, stopping listener.");
					this.connection.stop();
					lastMsgLatch.countDown();
				}
			} catch (JMSException e) {
				throw new RuntimeException(e);
			}			
		}
	}
}
