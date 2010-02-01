package deng.pojo.jms;

import java.util.Date;
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
 *   java MeasureRate ExampleQueue 100
 *
 * Usage (using using explicit ConnectionFactory class name) 
 *        java MeasureRate ExampleQueue 100 org.apache.activemq.ActiveMQConnectionFactory
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
			cf = Utils.newInstanceFromClassName(args[2]);
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

		System.out.printf("===================================================\n");
		System.out.printf("%d samples completed in %.2f secs\n", numberOfSamples, elapsed);
		System.out.printf("Total average send/receive rate %.2f msg/sec\n", (numberOfSamples /elapsed));
	}

	private void runProducer() {
		Connection connection = null;
		try {
			connection = connectionFactory.createConnection();
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Queue dest = session.createQueue(queueName);
			MessageProducer producer = session.createProducer(dest);
			
			RateMeasurement rateMeasurement = new RateMeasurement("Producer");
			rateMeasurement.start();
			
			for (int i = 0; i < numberOfSamples; i++) {
				Message msg = createSampleMessage(session, i);
				rateMeasurement.sample(msg);
				producer.send(msg);
			}
			rateMeasurement.stop();
			rateMeasurement.printRates();
			
			producer.close();
			session.close();
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
			RateMeasurement rateMeasurement = listener.getRateMeasurement();
			rateMeasurement.start();
			
			connection.start();
			
			// Wait until it's done.
			listener.getLastMsgLatch().await();
			
			// We are done.
			rateMeasurement.stop();
			rateMeasurement.printRates();
			
			consumer.close();
			session.close();
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
		private RateMeasurement rateMeasurement = new RateMeasurement("Consumer");
		
		public RateMeasurement getRateMeasurement() {
			return rateMeasurement;
		}
		
		public CountDownLatch getLastMsgLatch() {
			return lastMsgLatch;
		}
		public void onMessage(Message msg) {
			try {
				TextMessage txtMsg = (TextMessage)msg;
				String txt = txtMsg.getText();				
				if(txt.startsWith("Test msg #")) {
					rateMeasurement.sample(msg);																
					lastMsgLatch.countDown();
				}
			} catch (JMSException e) {
				throw new RuntimeException(e);
			}			
		}
	}
	
	/**
	 * Default will sample rate on every 1000 msgs or 2 seconds, which ever comes first.
	 * 
	 * @author dengz1
	 *
	 */
	private class RateMeasurement {
		private String name;
		private int count;
		private long maxSampleInterval = 2000; // in millis
		private long maxMsgPerSampleInterval = 1000; 
		private long startTime;
		private long stopTime;
		private double maxRate;
		private double currentRate;
		private int currentCount;
		private long lastSampleTime;
		
		public RateMeasurement(String name) {
			this.name = name;
		}
		
		public void start() {
			startTime = System.currentTimeMillis();
			lastSampleTime = startTime;
		}
		public void stop() {
			stopTime = System.currentTimeMillis();
		}
		
		public void sample(Message msg) {		
			count ++;
			currentCount ++;

			// Calculate sample rate
			long currentSampleTime = System.currentTimeMillis();
			if (currentCount >= maxMsgPerSampleInterval || (currentSampleTime - lastSampleTime) >= maxSampleInterval) {				
				double ellapsedSecs = (currentSampleTime - lastSampleTime) / 1000.0;				
				currentRate = currentCount / ellapsedSecs;
				//System.out.printf(name + ": Current sample rate=%.2f msgs/sec, maxRate=%.2f, sampleEllapsedTime=%.2f secs, sampleCount=%d, totalCount=%d\n", currentRate, maxRate, ellapsedSecs, currentCount, count);
				System.out.printf(name + ": Current sample rate=%.2f msgs/sec, maxRate=%.2f, totalCount=%d\n", currentRate, maxRate, count);
				if (currentRate > maxRate) {
					maxRate = currentRate;
				}				
				lastSampleTime = currentSampleTime;
				currentCount = 0;
			}
		}
		public void printRates() {
			double ellapsedSecs = (stopTime - startTime) / 1000.0;
			System.out.printf(name + ": Start time: %s\n", new Date(startTime));
			System.out.printf(name + ": Stop time: %s\n", new Date(stopTime));
			System.out.printf(name + ": Ellapsed time: %.2f secs\n", ellapsedSecs);
			System.out.printf(name + ": Sample interval: %d ms\n", maxSampleInterval);
			System.out.printf(name + ": Message count: %d\n", count);
			System.out.printf(name + ": Max rate %.2f msg/sec\n", maxRate);
		}
	}
}
