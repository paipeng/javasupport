package deng.pojo.jms;

import java.util.Date;
import java.util.concurrent.CountDownLatch;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * Usage (using default JNDI "ConnectionFactory" lookup by jndi.properties) 
 *   java -DqueueName=ExampleQueue -DnumberOfSamples=5000 deng.pojo.jms.MeasureRate
 *
 * Usage (using using explicit ConnectionFactory class name) 
 *        java -DqueueName=ExampleQueue -DnumberOfSamples=5000 -DconnectionFactory=org.apache.activemq.ActiveMQConnectionFactory deng.pojo.jms.MeasureRate
 * 
 * @author Zemian Deng
 *
 */
public class MeasureRate {
	public static void main(String[] args) {		
		MeasureRate main = new MeasureRate();
		main.queueName = System.getProperty("queueName", "ExampleQueue");
		main.numberOfSamples = Integer.parseInt(System.getProperty("numberOfSamples", "5000"));
		main.connectionFactoryClassName = System.getProperty("connectionFactoryClassName");
		main.persistMsg = Boolean.parseBoolean(System.getProperty("persistMsg", "false"));
		main.runConsumerFlag = Boolean.parseBoolean(System.getProperty("runConsumer", "true"));
		main.runProducerFlag = Boolean.parseBoolean(System.getProperty("runProducer", "true"));
		main.run();
	}
	
	private String queueName;
	private int numberOfSamples;
	private String connectionFactoryClassName;
	private boolean persistMsg;
	private boolean runConsumerFlag;
	private boolean runProducerFlag;
	
	private ConnectionFactory connectionFactory;
	
	public MeasureRate() {
		if (connectionFactoryClassName == null) {
			connectionFactory = Utils.lookupJndi("ConnectionFactory"); 
		} else {				
			connectionFactory = Utils.newInstanceFromClassName(connectionFactoryClassName);
		}
	}
	
	private void run() {
		long t1 = System.currentTimeMillis();
		
		// Start queue consumer on new thread.
		Thread consumerThread = new Thread(new Runnable() {
			public void run() {
				runConsumer();
			} 		
		});
		
		if (runConsumerFlag) {
			consumerThread.start();
		}
		
		if (runProducerFlag) {
			// Send burst of messages to queue.
			runProducer();
		}
		
		if (runConsumerFlag) {
			try {
				// Wait for consumer thread to finish.
				consumerThread.join();
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
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
			
			RateSampler rateSampler = new RateSampler("Producer");
			rateSampler.start();
			
			for (int i = 0; i < numberOfSamples; i++) {
				Message msg = createSampleMessage(session, i);
				rateSampler.sample(msg);
				int deliveryMode = persistMsg ? DeliveryMode.PERSISTENT : DeliveryMode.NON_PERSISTENT;
				producer.send(msg, deliveryMode, 5, 0);
			}
			rateSampler.stop();
			rateSampler.printRates();
			
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
			MeasureRateListener listener = new MeasureRateListener(); 
			connection = connectionFactory.createConnection();
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Queue dest = session.createQueue(queueName);
			MessageConsumer consumer = session.createConsumer(dest);
			consumer.setMessageListener(listener);
			RateSampler rateSampler = listener.getRateMeasurement();
			rateSampler.start();
			
			connection.start();
			
			// Wait until it's done.
			listener.getLastMsgLatch().await();
			
			// We are done.
			rateSampler.stop();
			rateSampler.printRates();
			
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
	
	private class MeasureRateListener implements MessageListener {
		private CountDownLatch lastMsgLatch = new CountDownLatch(numberOfSamples);
		private RateSampler rateMeasurement = new RateSampler("Consumer");
		
		public RateSampler getRateMeasurement() {
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
	

	public static class RateSampler {
		private String name;
		private int count;
		private long maxSampleInterval = 5000; // in millis
		private long maxMsgPerSampleInterval = 1000; 
		private long startTime;
		private long stopTime;
		private double maxRate;
		private double currentRate;
		private int currentCount;
		private long lastSampleTime;
		
		public RateSampler(String name) {
			this.name = name;
		}
		
		public void start() {
			startTime = System.currentTimeMillis();
			lastSampleTime = startTime;
		}
		public void stop() {
			stopTime = System.currentTimeMillis();
		}
		
		public void sample(Message data) {		
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
	
	public static class Utils {

		
		@SuppressWarnings("unchecked")
		public static <T> T newInstanceFromClassName(String className) {
			try {
				return (T) Class.forName(className).newInstance();
			} catch (InstantiationException e) {
				throw new RuntimeException(e);
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			} catch (ClassNotFoundException e) {
				throw new RuntimeException(e);
			}
		}
		
		@SuppressWarnings("unchecked")
		public static <T> T lookupJndi(String name) {
			InitialContext context = null;
			try {
				context = new InitialContext();
				Object object = context.lookup(name);
				return (T)object;
			} catch (NamingException e) {
				throw new RuntimeException(e);
			} finally {
				if (context != null) {
					try {
						context.close();
					} catch (NamingException e) {
						throw new RuntimeException(e);
					}
				}
			}
		}

		public static void close(Connection connection) {
			try {
				if (connection != null) {
					connection.stop();   // Stop any consumers that has not stopped on it's own.
					connection.close();
				}
			} catch (JMSException e) {
				throw new RuntimeException(e);
			}		
		}
	}
}
