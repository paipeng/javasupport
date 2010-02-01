package deng.pojo.jms;

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

/**
 * Usage (using default JNDI "ConnectionFactory" lookup by jndi.properties) 
 *   java MeasureRate ExampleQueue 10000
 *
 * Usage (using using explicit ConnectionFactory class name) 
 *        java MeasureRate ExampleQueue 10000 org.apache.activemq.spring.ActiveMQConnectionFactory
 * 
 * @author Zemian Deng
 *
 */
public class MeasureRate {
	public static void main(String[] args) {
		String qname = "ExampleQueue";
		int nSamples = 100;
		ConnectionFactory cf = null;
		
		if (args.length >= 1) { qname = args[0]; }
		if (args.length >= 2) { nSamples = Integer.parseInt(args[1]); }
		if (args.length >= 3) { 
			cf = Utils.newInstanceFromClassName(args[0]);
		} else {				
			cf = Utils.lookupJndi("ConnectionFactory");
		}
		
		MeasureRate measureRate = new MeasureRate();
		measureRate.setQueueName(qname);
		measureRate.setNumberOfSamples(nSamples);
		measureRate.setConnectionFactory(cf);
		measureRate.run();
	}

	private String queueName;
	private int numberOfSamples;
	private ConnectionFactory connectionFactory;
	
	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}
	public void setNumberOfSamples(int numberOfSamples) {
		this.numberOfSamples = numberOfSamples;
	}
	public void setConnectionFactory(ConnectionFactory connectionFactory) {
		this.connectionFactory = connectionFactory;
	}
	
	private void run() {
		long t1 = System.currentTimeMillis();
		
		// Start queue consumer on new thread.
		Thread consumerThread = new Thread(new Runnable() {
			public void run() {
				runConsumer();
			} 		
		});
		consumerThread.start();
		
		// Send burst of messages to queue.
		runProducer();
		
		try {
			// Wait for consumer thread to finish.
			consumerThread.join();
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		
		long t2 = System.currentTimeMillis();
		double elapsed = (t2 - t1) / 1000.0;
				
		System.out.printf("%d samples completed in %.2f secs\n", numberOfSamples, elapsed);
		System.out.printf("Total average send/receive rate %.2f msg/sec\n", (numberOfSamples /elapsed));
	}

	private void runProducer() {
		Connection connection = null;
		try {
			connection = connectionFactory.createConnection();
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Queue dest = session.createQueue(queueName);
			MessageProducer prodcuer = session.createProducer(dest);
			
			System.out.printf("Sending %d messages to %s\n", numberOfSamples, queueName);
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
		return txtMsg;
	}

	private void runConsumer() {
		Connection connection = null;
		try {
			RateMessageListener listener = new RateMessageListener(); 
			connection = connectionFactory.createConnection();
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Queue dest = session.createQueue(queueName);
			MessageConsumer consumer = session.createConsumer(dest);
			consumer.setMessageListener(listener);
			connection.start();
			
			// Wait until it's done.
			listener.getLastMsgLatch().await();
			connection.stop();
		} catch (JMSException e) {
			throw new RuntimeException(e);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		} finally {
			Utils.close(connection);
		}		
	}	
	
	private class RateMessageListener implements MessageListener {
		private CountDownLatch lastMsgLatch = new CountDownLatch(numberOfSamples);
		public CountDownLatch getLastMsgLatch() {
			return lastMsgLatch;
		}
		public void onMessage(Message msg) {
			try {
				// collect msg rate data.	
				TextMessage txtMsg = (TextMessage)msg;
				String txt = txtMsg.getText();
				//System.out.println(txt);				
				
				lastMsgLatch.countDown();
			} catch (JMSException e) {
				throw new RuntimeException(e);
			}			
		}
	}
}
